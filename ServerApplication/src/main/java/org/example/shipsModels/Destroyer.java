package org.example.shipsModels;

public class Destroyer extends Ships {

    public Destroyer() {
        super(3, 3);
    }

    @Override
    public String getShipName() {
        return "Destroyer";
    }
}
