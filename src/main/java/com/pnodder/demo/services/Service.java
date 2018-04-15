package com.pnodder.demo.services;

import com.pnodder.demo.model.Bike;

import java.util.List;

public interface Service {

    List<Bike> findAll();

    Bike findById(Long id);

    List<Bike> findByMake(String make);

    void insert(Bike bike);

    void delete(Bike bike);

    List<Bike> findByColour(String colour);

}
