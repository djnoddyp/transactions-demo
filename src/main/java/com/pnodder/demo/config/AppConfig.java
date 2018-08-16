package com.pnodder.demo.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import com.pnodder.demo.listeners.DemoListener;
import com.pnodder.demo.repositories.BikeRepository;
import com.pnodder.demo.repositories.Repository;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.activemq.spring.ActiveMQXAConnectionFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.util.Properties;

@Configuration
@PropertySource("classpath:/app.properties")
@ComponentScan(basePackages = "com.pnodder.demo")
@EnableTransactionManagement
@EnableJms
public class AppConfig {

    public static final String QUEUE = "messageQueue1";

    @Autowired
    Environment env;

    @Bean(destroyMethod = "close")
    public AtomikosDataSourceBean dataSource() {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("postgres");
        dataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        Properties p = new Properties();
        p.setProperty ("user", env.getProperty("jdbc.user"));
        p.setProperty ("password", env.getProperty("jdbc.password"));
        p.setProperty ("serverName", env.getProperty("atomikos.server.name"));
        p.setProperty ("portNumber", env.getProperty("atomikos.server.port"));
        p.setProperty ("databaseName", env.getProperty("atomikos.database.name"));
        dataSource.setXaProperties(p);
        dataSource.setPoolSize(5);
        return dataSource;
    }
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager atomikosTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }
    
    @Bean
    public UserTransactionImp atomikosUserTransaction() {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        return userTransactionImp;
    }

    @Bean
    public JtaTransactionManager transactionManager() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManager(atomikosTransactionManager());
        transactionManager.setUserTransaction(atomikosUserTransaction());
        return transactionManager;
    }

    @Bean
    public Repository bikeRepository() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return new BikeRepository(jdbcTemplate);
    }

    @Bean
    public ResourceDatabasePopulator databasePopulator() throws Exception {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/test-schema.sql"));
        populator.populate(dataSource().getConnection());
        return populator;
    }

    @Bean
    public ActiveMQXAConnectionFactory activeMQConnectionFactory() {
        ActiveMQXAConnectionFactory activeMQConnectionFactory = new ActiveMQXAConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");
        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
        jmsTemplate.setReceiveTimeout(10000);
        jmsTemplate.setDefaultDestinationName(QUEUE);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory());
        factory.setSessionTransacted(true);
        factory.setConcurrency("5");
        factory.setTransactionManager(transactionManager());
        return factory;
    }

    @Bean
    public DemoListener demoListener() {
        return new DemoListener();
    }
    
}