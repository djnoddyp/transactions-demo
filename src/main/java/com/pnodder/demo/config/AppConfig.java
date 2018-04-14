package com.pnodder.demo.config;

import com.pnodder.demo.repositories.BikeRepository;
import com.pnodder.demo.repositories.Repository;
import com.pnodder.demo.services.BikeService;
import com.pnodder.demo.services.Service;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class AppConfig {

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/tdemo?useSSL=false");
        dataSource.setUsername("patrick");
        dataSource.setPassword("trance");
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public Repository bikeRepository() {
        return new BikeRepository(new JdbcTemplate(dataSource()));
    }

    @Bean
    public Service bikeService() {
        return new BikeService(bikeRepository());
    }

    @Bean
    public ResourceDatabasePopulator databasePopulator() throws Exception {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/schema.sql"));
        populator.populate(dataSource().getConnection());
        return populator;
    }

}
