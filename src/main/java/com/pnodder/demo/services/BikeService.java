package com.pnodder.demo.services;

import com.pnodder.demo.config.AppConfig;
import com.pnodder.demo.model.Bike;
import com.pnodder.demo.repositories.Repository;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
@Transactional(readOnly = true)
public class BikeService implements Service {

    private Repository repository;
    private JmsTemplate jmsTemplate;

    public BikeService(Repository repository, JmsTemplate jmsTemplate) {
        this.repository = repository;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public List<Bike> findAll() {
        return repository.findAll();
    }

    @Override
    public Bike findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Bike> findByMake(String make) {
        return repository.findByMake(make);
    }

    @Override
    @Transactional(readOnly = false)
    public void insert(Bike bike) {
        repository.insert(bike);
    }

    @Override
    @Transactional(readOnly = false, noRollbackFor = UncategorizedJmsException.class)
    public void delete(Bike bike) {
        repository.delete(bike);
        jmsTemplate.convertAndSend(AppConfig.QUEUE, "Bike deleted");
    }

    @Override
    public List<Bike> findByColour(String colour) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(readOnly = false)
    public void insertAndSendMessage(Set<Bike> bikes, String message) {
        bikes.forEach(this::insert);
        jmsTemplate.convertAndSend(AppConfig.QUEUE, message);
    }
}