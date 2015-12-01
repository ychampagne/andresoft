package org.andresoft.tweetsgobbler.tweeter.service;

import java.util.ArrayList;
import java.util.List;

import org.andresoft.tweetsgobbler.dao.CassandraSession;
import org.andresoft.tweetsgobbler.dao.CassandraTweetDao;
import org.andresoft.tweetsgobbler.dao.ITweetDao;
import org.andresoft.tweetsgobbler.dto.TweetSubject;
import org.andresoft.tweetsgobbler.tweeter.CassandraTwitterSink;
import org.andresoft.tweetsgobbler.tweeter.TweetEventQueue;
import org.andresoft.tweetsgobbler.tweeter.TwitterSource;
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
@ContextConfiguration(classes = { TweetCollectorServiceTest.ContextConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class TweetCollectorServiceTest
{
    @Autowired
    TweetCollectorService tweetCollectorService;

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

        @Bean
        @Lazy
        TweetCollectorService tweetCollectorService()
        {
            return new TweetCollectorService();
        }

        @Bean
        @Scope("prototype")
        public TwitterSource twitterSource(
                                           TweetEventQueue eventQueue)
        {
            return new TwitterSource(eventQueue);
        }
    }

    @Test
    public void testTweetCollectorService()
            throws Exception
    {

        TweetSubject tweetSubject = new TweetSubject();
        tweetSubject.setName("health care");
        List<String> keyWordsList = new ArrayList<String>();
        keyWordsList.add("National Institutes of Health");
        keyWordsList.add("nih.gov");
        keyWordsList.add("health care");
        keyWordsList.add("medicare");
        keyWordsList.add("medicaid");
        keyWordsList.add("dental insurance");
        keyWordsList.add("health insurance");

        tweetSubject.setKeyWords(keyWordsList);

        tweetCollectorService.starCollection(tweetSubject);
        Thread.sleep(60000 * 10);

        tweetCollectorService.stopCollection("health care");

    }
}
