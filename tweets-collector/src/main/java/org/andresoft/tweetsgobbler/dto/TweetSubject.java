package org.andresoft.tweetsgobbler.dto;

import java.util.List;

public class TweetSubject
{
    private String name;
    private List<String> keyWords;

    public String getName()
    {
        return name;
    }

    public void setName(
                        String name)
    {
        this.name = name;
    }

    public List<String> getKeyWords()
    {
        return keyWords;
    }

    public void setKeyWords(
                            List<String> keyWords)
    {
        this.keyWords = keyWords;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(
                          Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TweetSubject other = (TweetSubject) obj;
        if (name == null)
        {
            if (other.name != null) return false;
        }
        else if (!name.equals(other.name)) return false;
        return true;
    }

}
