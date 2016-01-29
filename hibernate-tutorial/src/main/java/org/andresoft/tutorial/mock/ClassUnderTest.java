package org.andresoft.tutorial.mock;

public class ClassUnderTest
{
    Collaborator listener;

    // ...
    public void addListener(Collaborator listener)
    {
        this.listener = listener;
        System.out.println("listener added");
    }

    public void addDocument(String title, byte[] document)
    {
        listener.documentAdded(title);

        System.out.println("Document added " + title);

    }

    public boolean removeDocument(String title)
    {
        listener.documentRemoved(title);
        System.out.println("Document removed " + title);
        return true;
    }

    public boolean removeDocuments(String[] titles)
    {
        for (String title : titles)
        {
            removeDocument(title);
        }

        return true;
    }
}
