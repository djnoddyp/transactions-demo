package com.pnodder.demo.repositories;

import com.pnodder.demo.model.Bike;

import java.util.List;

public interface Repository {

    List<Bike> findAll();

    Bike findById(Long id);

    List<Bike> findByMake(String make);

    void insert(Bike bike);

    void delete(Bike bike);

}
