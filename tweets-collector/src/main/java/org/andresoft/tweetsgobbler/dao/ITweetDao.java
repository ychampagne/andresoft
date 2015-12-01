package org.andresoft.tweetsgobbler.dao;

import java.util.Date;

import org.andresoft.tweetsgobbler.dto.TweetExcerpt;

import com.datastax.driver.core.ResultSetFuture;

public interface ITweetDao
{
    public ResultSetFuture createTweet(
                                       TweetExcerpt tweetExcerpt);

    public void findTweetByDate(
                                Date date);

}
