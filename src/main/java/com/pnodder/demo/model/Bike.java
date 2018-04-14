package com.pnodder.demo.model;

public class Bike {

    private Long id;
    private String make;
    private String model;
    private String colour;
    private Style style;

    public Bike() {
    }

    private Bike(String make, String model, String colour, Style style) {
        this.make = make;
        this.model = model;
        this.colour = colour;
        this.style = style;
    }

    public static Bike of(String make, String model, String colour, Style style) {
        return new Bike(make, model, colour, style);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return String.format("Make: %s  Model: %s  Colour: %s  Style: %s", make, model, colour, style.name());
    }

}
