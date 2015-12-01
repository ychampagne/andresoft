package org.andresoft.tweetsgobbler.tweeter;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.andresoft.tweetsgobbler.dao.ITweetDao;
import org.andresoft.tweetsgobbler.dto.TweetContext;
import org.andresoft.tweetsgobbler.dto.TweetEvent;
import org.andresoft.tweetsgobbler.dto.TweetExcerpt;
import org.andresoft.tweetsgobbler.util.TweetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CassandraTwitterSink implements Runnable
{
    private static final Logger logger = LoggerFactory.getLogger(CassandraTwitterSink.class);

    @Autowired
    ITweetDao tweetDao;

    private TweetEventQueue eventQueue;

    public CassandraTwitterSink(TweetEventQueue eventQueue)
    {
        this.eventQueue = eventQueue;
    }

    @PostConstruct
    private void init()
    {

    }

    @PreDestroy
    private void destroy()
    {

    }

    public void configure(
                          TweetContext context)
    {
        // do nothing;
    }

    public void start()
    {
        // do nothing;
    }

    public void stop()
    {
        // do nothing
    }

    public void process()
            throws Exception
    {
        List<TweetEvent> events = eventQueue.get(eventQueue.getCurrentSize(), 1000);
        for (TweetEvent event : events)
        {
            String jsonTweet = new String(event.getBody());
            String subject = event.getHeaders().get(TwitterSourceConstants.SUBJECT);
            TweetExcerpt te = TweetUtil.getTweetExcerptFromTweet(jsonTweet);
            te.setSubject(subject);
            tweetDao.createTweet(te);
        }

    }

    @Override
    public void run()
    {
        try
        {
            process();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
    }
}
