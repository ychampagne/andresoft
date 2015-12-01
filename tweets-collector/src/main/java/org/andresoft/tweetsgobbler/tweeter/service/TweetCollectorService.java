package org.andresoft.tweetsgobbler.tweeter.service;

import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.andresoft.tweetsgobbler.dto.TweetContext;
import org.andresoft.tweetsgobbler.dto.TweetSubject;
import org.andresoft.tweetsgobbler.tweeter.CassandraTwitterSink;
import org.andresoft.tweetsgobbler.tweeter.TweetEventQueue;
import org.andresoft.tweetsgobbler.tweeter.TwitterSource;
import org.andresoft.tweetsgobbler.tweeter.TwitterSourceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TweetCollectorService implements ITweetCollectorService
{
    @Value("${twitter.consumerKey}")
    private String consumerKey;

    @Value("${twitter.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.accessToken}")
    private String accessToken;

    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;

    @Autowired
    ApplicationContext ctx;

    private ConcurrentHashMap<String, TweetCollectorContext> tweeterSourceMap = new ConcurrentHashMap<String, TweetCollectorContext>();

    @Override
    public void starCollection(
                               TweetSubject tweetSubject)
    {
        TweetCollectorContext collectorContext = tweeterSourceMap.get(tweetSubject.getName());

        if (collectorContext == null)
        {
            TweetEventQueue eventQueue = new TweetEventQueue();

            TwitterSource source = ctx.getBean(TwitterSource.class, eventQueue);
            TweetContext context = new TweetContext();
            StringJoiner sj = new StringJoiner(",");
            for (String kw : tweetSubject.getKeyWords())
            {
                sj.add(kw);
            }
            context.put(TwitterSourceConstants.KEYWORDS_KEY, sj.toString());
            context.put(TwitterSourceConstants.SUBJECT, tweetSubject.getName());
            source.configure(context);

            CassandraTwitterSink sink = ctx.getBean(CassandraTwitterSink.class, eventQueue);

            ScheduledExecutorService sinkRunner = Executors.newSingleThreadScheduledExecutor();

            collectorContext = new TweetCollectorContext(source, sink, sinkRunner);
            tweeterSourceMap.put(tweetSubject.getName(), collectorContext);
            source.start();
            sinkRunner.scheduleAtFixedRate(sink, 0, 1, TimeUnit.SECONDS);

        }

    }

    @Override
    public void stopCollection(
                               String subjectName)
    {
        TweetCollectorContext collectorContext = tweeterSourceMap.get(subjectName);
        if (collectorContext != null)
        {
            collectorContext.source.stop();
            collectorContext.sinkRunner.shutdown();
            try
            {
                collectorContext.sink.process();
            }
            catch (Exception e)
            {
                // do nothing
            }

            tweeterSourceMap.remove(subjectName);
        }

    }

    @PreDestroy
    private void stop()
    {
        for (TweetCollectorContext collectorContext : tweeterSourceMap.values())
        {
            collectorContext.source.stop();
            collectorContext.sinkRunner.shutdown();
            try
            {
                collectorContext.sink.process();
            }
            catch (Exception e)
            {
                // do nothing
            }
        }
        tweeterSourceMap.clear();
    }

    private class TweetCollectorContext
    {
        TwitterSource source;
        CassandraTwitterSink sink;
        ExecutorService sinkRunner;

        private TweetCollectorContext(TwitterSource source, CassandraTwitterSink sink, ExecutorService sinkRunner)
        {
            this.source = source;
            this.sink = sink;
            this.sinkRunner = sinkRunner;
        }
    }
}
