package org.andresoft.datastore;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataStoreConfiguration
{
    @Value("${jdbc.mysql.driverClassName:com.mysql.jdbc.Driver}")
    String mySqlDriverClassName;

    @Value("${jdbc.mysql.url}")
    String mySqlUrl;

    @Value("${jdbc.mysql.username}")
    String mySqlUsername;

    @Value("${jdbc.mysql.password}")
    String mySqlPassword;

    @Bean
    @Lazy
    public DataSource mysqlDataSource()
    {
        org.apache.tomcat.jdbc.pool.DataSource poolDataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        poolDataSource.setUsername(mySqlUsername);
        poolDataSource.setPassword(mySqlPassword);
        poolDataSource.setUrl(mySqlUrl);
        poolDataSource.setDriverClassName(mySqlDriverClassName);

        poolDataSource.setRemoveAbandoned(true);
        poolDataSource.setDefaultAutoCommit(true);
        poolDataSource.setValidationQuery("select 1"); //
        poolDataSource.setValidationQueryTimeout(10); // in s
        poolDataSource.setValidationInterval(10000); // in ms
        poolDataSource.setTestOnBorrow(true);

        return poolDataSource;
    }

    @Bean
    @Lazy
    public JdbcTemplate mysqlJdbcTemplate()
    {
        return new JdbcTemplate(mysqlDataSource());
    }

}
