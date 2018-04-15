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
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TestConfig {

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:~/test");
        dataSource.setUsername("sa");
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return new BikeRepository(jdbcTemplate);
    }

    @Bean
    public Service bikeService() {
        return new BikeService(bikeRepository());
    }

    @Bean
    public ResourceDatabasePopulator databasePopulator() throws Exception {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/test-schema.sql"));
        populator.populate(dataSource().getConnection());
        return populator;
    }

}
