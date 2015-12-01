package org.andresoft.tweetsgobbler.dto;

import java.util.List;

import org.joda.time.DateTime;

public class TweetExcerpt
{
    private String subject;
    private long id;
    private DateTime createdAtDateTime;
    private String createdAtDate;
    private String source;
    private String tweet;
    private List<String> hashtags;
    private String userName;
    private String userScreenName;
    private String location;
    private String timeZone;
    private String timeLoaded;
    private String rawTweet;

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(
                           String subject)
    {
        this.subject = subject;
    }

    public long getId()
    {
        return id;
    }

    public void setId(
                      long id)
    {
        this.id = id;
    }

    public DateTime getCreatedAtDateTime()
    {
        return createdAtDateTime;
    }

    public void setCreatedAtDateTime(
                                     DateTime createdAtDateTime)
    {
        this.createdAtDateTime = createdAtDateTime;
    }

    public String getCreatedAtDate()
    {
        return createdAtDate;
    }

    public void setCreatedAtDate(
                                 String createdAtDate)
    {
        this.createdAtDate = createdAtDate;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(
                          String source)
    {
        this.source = source;
    }

    public String getTweet()
    {
        return tweet;
    }

    public void setTweet(
                         String tweet)
    {
        this.tweet = tweet;
    }

    public List<String> getHashtags()
    {
        return hashtags;
    }

    public void setHashtags(
                            List<String> hashtags)
    {
        this.hashtags = hashtags;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(
                            String userName)
    {
        this.userName = userName;
    }

    public String getUserScreenName()
    {
        return userScreenName;
    }

    public void setUserScreenName(
                                  String userScreenName)
    {
        this.userScreenName = userScreenName;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(
                            String location)
    {
        this.location = location;
    }

    public String getTimeZone()
    {
        return timeZone;
    }

    public void setTimeZone(
                            String timeZone)
    {
        this.timeZone = timeZone;
    }

    public String getTimeLoaded()
    {
        return timeLoaded;
    }

    public void setTimeLoaded(
                              String timeLoaded)
    {
        this.timeLoaded = timeLoaded;
    }

    public String getRawTweet()
    {
        return rawTweet;
    }

    public void setRawTweet(
                            String rawTweet)
    {
        this.rawTweet = rawTweet;
    }

}
