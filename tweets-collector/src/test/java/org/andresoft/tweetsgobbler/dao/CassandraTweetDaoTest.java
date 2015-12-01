package org.andresoft.tweetsgobbler.dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.andresoft.tweetsgobbler.dto.TweetExcerpt;
import org.andresoft.tweetsgobbler.util.TweetUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CassandraTweetDaoTest.ContextConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class CassandraTweetDaoTest
{

    @Autowired
    ITweetDao tweetDao;

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
    }

    @Test
    public void testInsertTweetExcerpt()
            throws Exception
    {
        String jsonString = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("test_tweets.json"));

        TweetExcerpt te = TweetUtil.getTweetExcerptFromTweet(jsonString);
        assertNotNull(te);
        te.setSubject("auto industry");

        ResultSetFuture future = tweetDao.createTweet(te);
        ResultSet rs = future.getUninterruptibly();
        assertNotNull(rs);

    }

    @Test
    public void testGetTweetExcerpt()
            throws Exception
    {
        String jsonString = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("test_tweets.json"));

        TweetExcerpt te = TweetUtil.getTweetExcerptFromTweet(jsonString);
        assertNotNull(te);

    }
}
