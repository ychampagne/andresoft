package org.andresoft.tweetsgobbler.dto;

import java.util.Map;

public class TweetEvent
{
    private String body;
    private Map<String, String> headers;

    public String getBody()
    {
        return body;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public TweetEvent(String body, Map<String, String> headers)
    {
        super();
        this.body = body;
        this.headers = headers;
    }

}
