package org.andresoft.tweetsgobbler.configuration;

import java.util.ArrayList;
import java.util.List;

import org.andresoft.tweetsgobbler.tweeter.CassandraTwitterSink;
import org.andresoft.tweetsgobbler.tweeter.TweetEventQueue;
import org.andresoft.tweetsgobbler.tweeter.TwitterSource;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@ComponentScan("org.andresoft.tweetsgobbler")
public class AppConfig
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
                                       TweetEventQueue tweetEventQueue)
    {
        return new TwitterSource(tweetEventQueue);
    }

    @Bean
    @Scope("prototype")
    public CassandraTwitterSink cassandraTwitterSink(
                                                     TweetEventQueue tweetEventQueue)
    {
        return new CassandraTwitterSink(tweetEventQueue);
    }
}
