package com.pnodder.demo.repositories;

import com.pnodder.demo.model.Bike;
import com.pnodder.demo.model.Style;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class BikeRepository implements Repository {

    private JdbcTemplate jdbcTemplate;

    public BikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Bike> bikeMapper = (resultSet, rowNum) -> {
        Bike b = new Bike();
        b.setId(resultSet.getLong("id"));
        b.setMake(resultSet.getString("make"));
        b.setModel(resultSet.getString("model"));
        b.setColour(resultSet.getString("colour"));
        b.setStyle(Style.valueOf(resultSet.getString("style")));
        return b;
    };

    @Override
    public List<Bike> findAll() {
        return jdbcTemplate.query("SELECT * FROM Bikes", bikeMapper);
    }

    public Bike findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM Bikes WHERE id = ?",
                new Object[]{id},
                bikeMapper);
    }

    public List<Bike> findByMake(String make) {
        return jdbcTemplate.query(
                "SELECT * FROM Bikes WHERE make = ?",
                new Object[]{make},
                bikeMapper);
    }

    public void insert(Bike bike) {
        jdbcTemplate.update("INSERT INTO Bikes (make, model, colour, style) " +
                        "VALUES (?, ?, ?, ?)", bike.getMake(), bike.getModel(), bike.getColour(), bike.getStyle().name());
    }

    public void delete(Bike bike) {
        jdbcTemplate.update("DELETE FROM Bikes WHERE id = ?", bike.getId());
    }
}
