package org.andresoft.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.andresoft.model.FoodViolationSummary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FoodEstablishmentDaoTest.ContextConfiguration.class,
        loader = AnnotationConfigContextLoader.class)
public class FoodEstablishmentDaoTest
{

    static String mySqlDriverClassName;
    static String mySqlUrl;
    static String mySqlUsername;
    static String mySqlPassword;

    @Value("${jdbc.mysql.driverClassName:com.mysql.jdbc.Driver}")
    String driverClassName;

    @Value("${jdbc.mysql.url:localhost}")
    String url;


    @Value("${jdbc.mysql.username:consumer}")
    String user;

    @Value("${jdbc.mysql.password:consumer}")
    String credential;

    @Autowired
    ApplicationContext ctx;

    @Configuration
    public static class ContextConfiguration
    {
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
        public JdbcTemplate mysqlJdbcTemplate()
        {
            return new JdbcTemplate(mysqlDataSource());
        }

        @Bean
        @Lazy
        public FoodEstablishmentDao foodEstablishmentDao()
        {
            return new FoodEstablishmentDaoImpl();
        }
    }

    @Before
    public void setUp() throws Exception
    {
        Field driverClassNameField = FoodEstablishmentDaoTest.class.getDeclaredField("mySqlDriverClassName");
        driverClassNameField.setAccessible(true);
        driverClassNameField.set(null, driverClassName);

        Field urlField = FoodEstablishmentDaoTest.class.getDeclaredField("mySqlUrl");
        urlField.setAccessible(true);
        urlField.set(null, url);

        Field userNameField = FoodEstablishmentDaoTest.class.getDeclaredField("mySqlUsername");
        userNameField.setAccessible(true);
        userNameField.set(null, user);

        Field credentialField = FoodEstablishmentDaoTest.class.getDeclaredField("mySqlPassword");
        credentialField.setAccessible(true);
        credentialField.set(null, credential);
    }

    @Test
    public void testgetEstablishmentWithAtLeastNViolations()
    {

        FoodEstablishmentDao foodEstablishmentDao = ctx.getBean(FoodEstablishmentDao.class);
        List<FoodViolationSummary> fvs = foodEstablishmentDao.getEstablishmentWithAtLeastNViolations(10);
        assertNotNull(fvs);
        assertTrue(!fvs.isEmpty());
    }


}
