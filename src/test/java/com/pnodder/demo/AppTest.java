package com.pnodder.demo;

import com.google.common.collect.ImmutableSet;
import com.pnodder.demo.config.AppConfig;
import com.pnodder.demo.model.Bike;
import com.pnodder.demo.model.Style;
import com.pnodder.demo.services.Service;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.net.URI;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class AppTest {

    private Service bikeService;
    private static BrokerService brokerService;
    private JdbcTemplate jdbcTemplate;
    Bike bikeOne = Bike.of(1L, "GT", "Avalanche 1.0", "Silver", Style.MOUNTAIN);
    Bike bikeTwo = Bike.of(2L, "Specialized", "Roubaix", "Red", Style.ROAD);
    Logger log = LoggerFactory.getLogger(AppTest.class);
    private static final String BROKER_URL = "broker:(tcp://localhost:61616)";

    @Autowired
    public void setup(Service bikeService, DataSource dataSource) {
        this.bikeService = bikeService;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @BeforeClass
    public static void startBroker() {
        try {
            brokerService = BrokerFactory.createBroker(new URI(BROKER_URL));
            brokerService.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void clearTables() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "Bikes");
    }
    
    @AfterClass
    public static void tearDown() {
        try {
            brokerService.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testApp() throws Exception {
        bikeService.insertAndSendMessage(ImmutableSet.of(bikeOne, bikeTwo), "Bikes saved");
        // allow time for message to be received
        //Thread.sleep(10000);
        assertEquals(2, countRowsInTable("Bikes"));
    }

    /**
     * BikeService#insertAndSendMessage is transactional. Test that
     * when one operation fails (e.g. sending the message) the database
     * operation is rolled back.
     */
    @Test
    public void testRollback() {
        try {
            brokerService.stop();
            bikeService.insertAndSendMessage(ImmutableSet.of(bikeOne, bikeTwo), "Bikes saved");
        } catch (Exception e) {
            // purposely do nothing
            log.info("#### Sending message failed for some reason... ####");
        }
        assertEquals(0, countRowsInTable("Bikes"));
    }

    /**
     * BikeService#delete is transactional with a rule to not
     * rollback when an UncategorizedJmsException exception occurs.
     */
    @Test
    public void testNoRollbackRule() {
        try {
            brokerService.stop();
            bikeService.insert(bikeOne);
            bikeService.delete(bikeOne);
        } catch (Exception e) {
            // purposely do nothing
            log.info("#### Sending message failed for some reason... ####");
        }
        assertEquals(0, countRowsInTable("Bikes"));
    }
    
    

    protected int countRowsInTable(String tableName) {
        return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
    }

}