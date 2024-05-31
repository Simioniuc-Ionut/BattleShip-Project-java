package org.example.shipsModels;

public class Submarine extends Ships {

    public Submarine() {
        super(2, 3);
    }

    @Override
    public String getShipName() {
        return "Submarine";
    }
}
