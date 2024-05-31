package org.example.shipsModels;

public class Carrier extends Ships {

    public Carrier() {
        super(5, 5);
    }

    @Override
    public String getShipName() {
        return "Carrier";
    }

}
