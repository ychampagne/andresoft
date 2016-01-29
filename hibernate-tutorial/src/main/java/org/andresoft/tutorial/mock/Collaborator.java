package org.andresoft.tutorial.mock;

public interface Collaborator
{
    void documentAdded(String title);

    void documentChanged(String title);

    void documentRemoved(String title);

    byte voteForRemoval(String title);

    byte[] voteForRemovals(String[] title);
}
