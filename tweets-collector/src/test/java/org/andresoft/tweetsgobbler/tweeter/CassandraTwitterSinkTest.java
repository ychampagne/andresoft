package org.andresoft.tweetsgobbler.tweeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.andresoft.tweetsgobbler.dao.CassandraSession;
import org.andresoft.tweetsgobbler.dao.CassandraTweetDao;
import org.andresoft.tweetsgobbler.dao.ITweetDao;
import org.andresoft.tweetsgobbler.dto.TweetEvent;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CassandraTwitterSinkTest.ContextConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class CassandraTwitterSinkTest
{

    @Autowired
    ApplicationContext ctx;

    @Configuration
    public static class ContextConfiguration
    {
        @Bean
        public static PropertyPlaceholderConfigurer placeHolderConfigurer()
        {
            final PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
            configurer.setIgnoreUnresolvablePlaceholders(true);
            configurer.setIgnoreResourceNotFound(true);
            configurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);

            final List<Resource> resourceLst = new ArrayList<Resource>();

            resourceLst.add(new ClassPathResource("application.properties"));
            configurer.setLocations(resourceLst.toArray(new Resource[] {}));

            return configurer;
        }

        @Bean
        @Lazy
        public ITweetDao tweetDao()
        {
            ITweetDao dao = new CassandraTweetDao();
            return dao;

        }

        @Bean
        @Lazy
        public CassandraSession cassandraSession()
        {
            return new CassandraSession();
        }

        @Bean
        @Scope("prototype")
        CassandraTwitterSink cassandraTwitterSink(
                                                  TweetEventQueue eventQueue)
        {
            return new CassandraTwitterSink(eventQueue);
        }
    }

    @Test
    public void testOneEvent()
            throws Exception
    {
        String tweet = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("test_tweets.json"));

        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(TwitterSourceConstants.SUBJECT, "auto industry");

        TweetEventQueue eventQueue = new TweetEventQueue();

        TweetEvent event = new TweetEvent(tweet, headers);

        eventQueue.put(event);

        CassandraTwitterSink sink = ctx.getBean(CassandraTwitterSink.class, eventQueue);

        ScheduledExecutorService sinkRunner = Executors.newSingleThreadScheduledExecutor();
        sinkRunner.scheduleAtFixedRate(sink, 0, 1, TimeUnit.SECONDS);
        Thread.sleep(5000);
        sinkRunner.shutdown();

    }

}
