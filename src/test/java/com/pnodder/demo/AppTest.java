package com.pnodder.demo;

import com.pnodder.demo.config.TestConfig;
import com.pnodder.demo.model.Bike;
import com.pnodder.demo.model.Style;
import com.pnodder.demo.services.Service;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AppTest {

    private Service bikeService;
    private JdbcTemplate jdbcTemplate;
    private JmsTemplate jmsTemplate;

    @Autowired
    public void setup(DataSource dataSource, JmsTemplate jmsTemplate, Service bikeService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jmsTemplate = jmsTemplate;
        this.bikeService = bikeService;
    }

    @Test
    public void testApp() {
        Bike bikeOne = Bike.of("GT", "Avalanche 1.0", "Silver", Style.MOUNTAIN);
        Bike bikeTwo = Bike.of("Specialized", "Roubaix", "Red", Style.ROAD);
        bikeService.insert(bikeOne);
        bikeService.insert(bikeTwo);
        jmsTemplate.convertAndSend("messageQueue1", "what's hapnin mate");
        assertFalse(bikeService.findAll().isEmpty());
        assertEquals(2, countRowsInTable("Bikes"));
    }

    protected int countRowsInTable(String tableName) {
        return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
    }

}
