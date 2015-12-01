package org.andresoft.tweetsgobbler.tweeter;

import java.util.ArrayList;
import java.util.List;

import org.andresoft.tweetsgobbler.dto.TweetContext;
import org.andresoft.tweetsgobbler.dto.TweetEvent;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TwitterSourceTest.ContextConfiguration.class }, loader = AnnotationConfigContextLoader.class)
public class TwitterSourceTest
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

    TwitterSource source;

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
        @Scope("prototype")
        public TwitterSource twitterSource(
                                           TweetEventQueue eventQueue)
        {
            return new TwitterSource(eventQueue);
        }
    }

    @Before
    public void setUp()
    {
        System.setProperty("flume.root.logger", "INFO,console");
    }

    @After
    public void teardown()
    {
        if (source != null)
        {
            source.stop();
        }
    }

    @Test
    public void testTwitterSourceBasic()
            throws Exception
    {

        Assume.assumeNotNull(consumerKey);
        Assume.assumeNotNull(consumerSecret);
        Assume.assumeNotNull(accessToken);
        Assume.assumeNotNull(accessTokenSecret);

        TweetEventQueue eventQueue = new TweetEventQueue();
        TweetContext context = new TweetContext();

        context.put(TwitterSourceConstants.SUBJECT, "health care");
        context.put(TwitterSourceConstants.KEYWORDS_KEY, "nih,health care,medicare,medicaid,dental insurance,health insurance");

        source = ctx.getBean(TwitterSource.class, eventQueue);
        source.configure(context);

        source.start();
        Thread.sleep(20000);
        source.stop();

        List<TweetEvent> events = eventQueue.get(eventQueue.getCurrentSize(), 5000);
        for (TweetEvent event : events)
        {
            String jsonTweet = new String(event.getBody());
            System.out.println(jsonTweet);
        }
    }

}
