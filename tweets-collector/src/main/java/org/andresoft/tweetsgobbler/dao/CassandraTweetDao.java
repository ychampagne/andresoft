package org.andresoft.tweetsgobbler.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.andresoft.tweetsgobbler.dto.TweetExcerpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSetFuture;

@Repository
@Lazy
public class CassandraTweetDao implements ITweetDao
{

    @Autowired
    CassandraSession cassandraSession;

    // insert tweets prepared statement
    PreparedStatement insertStatement;
    PreparedStatement findTweetByDateStatement;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-DD-MM");

    @PostConstruct
    private void init()
    {
        String query = "INSERT INTO demo.tweets (subject, id, created_at_datetime, created_at_date, source, tweet,  hashtags, user_name, user_screen_name, location,time_zone, raw_tweet,time_loaded) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,now()) ";
        insertStatement = cassandraSession.getSession().prepare(query);
    }

    public ResultSetFuture createTweet(
                                       TweetExcerpt tweetExcerpt)
    {

        BoundStatement bind = insertStatement.bind(tweetExcerpt.getSubject(), tweetExcerpt.getId(), tweetExcerpt
                .getCreatedAtDateTime().toDate(), tweetExcerpt.getCreatedAtDate(), tweetExcerpt.getSource(), tweetExcerpt
                .getTweet(), tweetExcerpt.getHashtags(), tweetExcerpt.getUserName(), tweetExcerpt.getUserScreenName(),
                tweetExcerpt.getLocation(), tweetExcerpt.getTimeZone(), tweetExcerpt.getRawTweet());

        return cassandraSession.getSession().executeAsync(bind);

    }

    public void findTweetByDate(
                                Date date)
    {
        // String dateStr = formatter.format(date);
    }

}
