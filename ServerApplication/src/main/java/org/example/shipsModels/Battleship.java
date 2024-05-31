package org.example.shipsModels;

public class Battleship extends Ships {

    public Battleship() {
        super(4, 4);
    }

    @Override
    public String getShipName() {
        return "Battleship";
    }
}
