package org.andresoft.tutorial.mock;

import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

public class ExampleTest
{

    private ClassUnderTest classUnderTest;
    private Collaborator mock;

    @Before
    public void setUp()
    {

        mock = createMock(Collaborator.class);

        classUnderTest = new ClassUnderTest();
        classUnderTest.addListener(mock);
    }

    @Test
    public void testRemoveNonExistingDocument()
    {
        replay(mock); // 3
        // This call should not lead to any notification
        // of the Mock Object:
        classUnderTest.removeDocument("Does not exist");
    }
}
