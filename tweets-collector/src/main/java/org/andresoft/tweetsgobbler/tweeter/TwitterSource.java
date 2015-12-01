package org.andresoft.tweetsgobbler.tweeter;

import java.util.HashMap;
import java.util.Map;

import org.andresoft.tweetsgobbler.dto.TweetContext;
import org.andresoft.tweetsgobbler.dto.TweetEvent;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSource implements Runnable
{

    private static final Logger logger = LoggerFactory.getLogger(TwitterSource.class);

    /** Information necessary for accessing the Twitter API */
    @Value("${twitter.consumerKey}")
    private String consumerKey;

    @Value("${twitter.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.accessToken}")
    private String accessToken;

    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;

    private String[] keywords;

    private String subject;

    /** The actual Twitter stream. It's set up to collect raw JSON data */
    private TwitterStream twitterStream;

    private TweetEventQueue eventQueue;

    private String keywordString;

    public TwitterSource(TweetEventQueue eventQueue)
    {
        this.eventQueue = eventQueue;
    }

    /**
     * The initialization method for the Source. The context contains all the
     * configuration info, and can be used to retrieve any configuration values
     * necessary to set up the Source.
     */
    public void configure(
                          TweetContext context)
    {

        keywordString = context.getString(TwitterSourceConstants.KEYWORDS_KEY, "");
        keywords = keywordString.split(",");
        for (int i = 0; i < keywords.length; i++)
        {
            keywords[i] = keywords[i].trim();
        }

        subject = context.getString(TwitterSourceConstants.SUBJECT,
                keywords != null && !StringUtil.isBlank(keywords[0]) ? keywords[0] : "tweets");

        ConfigurationBuilder confbuilder = new ConfigurationBuilder();
        confbuilder.setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret)
                .setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret).setJSONStoreEnabled(true);

        twitterStream = new TwitterStreamFactory(confbuilder.build()).getInstance();
    }

    /**
     * Start processing events. This uses the Twitter Streaming API to sample
     * Twitter, and process tweets.
     */
    public void start()
    {

        // The StatusListener is a twitter4j API, which can be added to a
        // Twitter
        // stream, and will execute methods every time a message comes in
        // through
        // the stream.
        StatusListener listener = new StatusListener()
        {
            // The onStatus method is executed every time a new tweet comes in.
            public void onStatus(
                                 Status status)
            {
                Map<String, String> headers = new HashMap<String, String>();
                // The EventBuilder is used to build an event using the headers
                // and
                // the raw JSON of a tweet
                logger.debug(status.getUser().getScreenName() + ": " + status.getText());

                headers.put("timestamp", String.valueOf(status.getCreatedAt().getTime()));
                headers.put(TwitterSourceConstants.SUBJECT, subject);
                String tweet = TwitterObjectFactory.getRawJSON(status);

                TweetEvent event = new TweetEvent(tweet, headers);

                eventQueue.put(event);

            }

            // This listener will ignore everything except for new tweets
            public void onDeletionNotice(
                                         StatusDeletionNotice statusDeletionNotice)
            {
            }

            public void onTrackLimitationNotice(
                                                int numberOfLimitedStatuses)
            {
            }

            public void onScrubGeo(
                                   long userId,
                                   long upToStatusId)
            {
            }

            @Override
            public void onStallWarning(
                                       StallWarning warning)
            {
            }

            public void onException(
                                    Exception ex)
            {
                logger.debug(ex.getMessage());
            }
        };

        logger.debug(String.format(
                "I am Setting up Twitter sample stream using consumer key {%s} and  access token {%s} and keywords {%s}",
                consumerKey, accessToken, keywordString));
        // Set up the stream's listener (defined above), and set any necessary
        // security information.
        twitterStream.addListener(listener);
        // twitterStream.setOAuthConsumer(consumerKey, consumerSecret);

        // AccessToken token = new AccessToken(accessToken, accessTokenSecret);
        // twitterStream.setOAuthAccessToken(token);

        // Set up a filter to pull out industry-relevant tweets
        if (keywords.length == 0)
        {
            logger.debug("Starting up Twitter sampling...");
            twitterStream.sample();
        }
        else
        {
            logger.debug("Starting up Twitter filtering...");
            FilterQuery query = new FilterQuery().track(keywords).language(new String[] { "en", "en-US" });

            twitterStream.filter(query);
        }
    }

    /**
     * Stops the Source's event processing and shuts down the Twitter stream.
     */
    public void stop()
    {
        logger.debug("Shutting down Twitter sample stream...");
        twitterStream.shutdown();

    }

    @Override
    public void run()
    {
        start();
    }
}
