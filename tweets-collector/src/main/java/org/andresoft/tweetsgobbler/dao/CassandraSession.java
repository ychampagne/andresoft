package org.andresoft.tweetsgobbler.dao;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@Component
@Lazy
public class CassandraSession
{

    private Session session;
    private Cluster cluster;

    @Value("${cassandra.contact.points}")
    private String contactPoint;

    @PostConstruct
    private void init()
    {
        cluster = Cluster.builder().addContactPoint(contactPoint).build();
        session = cluster.newSession();
    }

    @PreDestroy
    public void close()
    {
        session.close();
        cluster.close();
    }

    public Session getSession()
    {
        return session;
    }
}
