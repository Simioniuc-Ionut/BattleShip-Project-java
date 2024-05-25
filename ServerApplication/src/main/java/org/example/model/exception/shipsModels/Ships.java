package org.example.model.exception.shipsModels;

public enum Ships {
     CARRIER_LENGTH  (5 , 5),
     BATTLESHIP_LENGTH (4,4),
     DESTROYER_LENGTH  (3,3),
     SUBMARINE_LENGTH (2,3),
     PATROL_BOAT_LENGTH (1,2);

   private int shipCode;
   private int shipSize;

   Ships(int shipCode, int shipSize) {
       this.shipCode = shipCode;
       this.shipSize = shipSize;
   }

    public int getShipCode() {
         return this.shipCode;
    }

    public char getShipCodeInChar(){
         return Integer.toString(this.shipCode).charAt(0);
    }
    public int getShipSize(){
       return this.shipSize;
    }
    public String getShipName(){
         switch (this.shipCode){
              case 5:
                return "Carrier";
              case 4:
                return "Battleship";
              case 3:
                return "Destroyer";
              case 2:
                return "Submarine";
              case 1:
                return "Patrol Boat";
              default:
                return "Invalid Ship";
         }
    }
    public void decreaseShipSize(){
       this.shipSize--;
    }

}
