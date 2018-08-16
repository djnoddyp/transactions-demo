package com.pnodder.demo.services;

import com.pnodder.demo.model.Bike;

import java.util.List;
import java.util.Set;

public interface Service {

    List<Bike> findAll();

    Bike findById(Long id);

    List<Bike> findByMake(String make);

    void insert(Bike bike);

    void delete(Bike bike);

    List<Bike> findByColour(String colour);
    
    default void insertAndSendMessage(Set<Bike> bikes, String meesage) {
        throw new UnsupportedOperationException();
    }

}
