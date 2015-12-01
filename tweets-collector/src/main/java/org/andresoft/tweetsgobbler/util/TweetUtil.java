package org.andresoft.tweetsgobbler.util;

import java.util.ArrayList;
import java.util.List;

import org.andresoft.tweetsgobbler.dto.TweetExcerpt;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TweetUtil
{

    private static ObjectMapper mapper;

    static
    {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static TweetExcerpt getTweetExcerptFromTweet(
                                                        String jsonTweet)
            throws Exception
    {

        JsonNode jsonRootNode = mapper.readTree(jsonTweet);
        JsonNode jsonUserNode = jsonRootNode.path("user");

        String timestampMsStr = jsonRootNode.get("timestamp_ms").textValue();
        DateTime timeLoaded = new DateTime(Long.valueOf(timestampMsStr));

        List<String> hashtags = new ArrayList<String>(0);
        JsonNode hashtagsNode = jsonRootNode.path("entities").path("hashtags");
        for (JsonNode tagNode : hashtagsNode)
        {
            String tagText = tagNode.get("text").textValue();
            hashtags.add(tagText);
        }

        TweetExcerpt te = new TweetExcerpt();

        te.setId(jsonRootNode.get("id").longValue());
        te.setCreatedAtDateTime(timeLoaded);
        te.setCreatedAtDate(timeLoaded.toString("yyyy-MM-dd"));
        te.setSource(jsonRootNode.get("source").textValue());
        te.setTweet(jsonRootNode.get("text").textValue());
        te.setHashtags(hashtags);
        te.setUserName(jsonUserNode.path("name").textValue());
        te.setUserScreenName(jsonUserNode.path("screen_name").textValue());
        te.setLocation(jsonUserNode.path("location").textValue());
        te.setTimeZone(jsonUserNode.path("time_zone").textValue());
        te.setRawTweet(jsonTweet);
        return te;

    }

}
