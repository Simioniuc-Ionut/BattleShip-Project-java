package org.example.shipsModels;

public class PatrolBoat extends Ships {

    public PatrolBoat() {
        super(1, 2);
    }

    @Override
    public String getShipName() {
        return "PatrolBoat";
    }
}
