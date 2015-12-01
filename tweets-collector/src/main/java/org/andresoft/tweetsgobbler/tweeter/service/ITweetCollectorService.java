package org.andresoft.tweetsgobbler.tweeter.service;

import org.andresoft.tweetsgobbler.dto.TweetSubject;

public interface ITweetCollectorService
{
    public void starCollection(
                               TweetSubject tweetSubject);

    public void stopCollection(
                               String subjectName);

}
