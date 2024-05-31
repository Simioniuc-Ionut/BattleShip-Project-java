package org.example.shipsModels;

public abstract class Ships {
//     CARRIER_LENGTH  (5 , 5),
//     BATTLESHIP_LENGTH (4,4),
//     DESTROYER_LENGTH  (3,3),
//     SUBMARINE_LENGTH (2,3),
//     PATROL_BOAT_LENGTH (1,2);

    private int shipCode;
    private int shipSize;

    public Ships(int shipCode, int shipSize) {
        this.shipCode = shipCode;
        this.shipSize = shipSize;
    }

    public int getShipCode() {
        return this.shipCode;
    }

    public char getShipCodeInChar() {
        return Integer.toString(this.shipCode).charAt(0);
    }

    public int getShipSize() {
        return this.shipSize;
    }

    public abstract String getShipName();

    public void decreaseShipSize() {
        this.shipSize--;
    }

}
