package com.pnodder.demo.services;

import com.pnodder.demo.model.Bike;
import com.pnodder.demo.repositories.Repository;

import java.util.List;

public class BikeService implements Service {

    private Repository repository;

    public BikeService(Repository repository) {
        this.repository = repository;
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
    public void insert(Bike bike) {
        repository.insert(bike);
    }

    @Override
    public void delete(Bike bike) {
        repository.delete(bike);
    }
}
