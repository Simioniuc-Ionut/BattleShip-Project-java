import org.example.Exception.GameException;
import org.example.GameServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class GameServerTest {
    private GameServer gameServer;

    @BeforeEach
    public void setup() {
    gameServer = new GameServer(12345);
    }

    @Test
    public void testPlaceShips() {
        // Player 1
        try {
            gameServer.placeShip(1, "B2 C2 D2 E2 F2", 5);
            gameServer.placeShip(1, "F4 F5 F6 F7", 4); // aici e o greseala
            gameServer.placeShip(1, "G4 H4 I4", 3);
            gameServer.placeShip(1, "G7 H7 I7", 3);
            gameServer.placeShip(1, "A8 A9", 2);
        } catch (GameException e) {
            System.out.println(e.getMessage());
            fail("Ship placement failed for Player 1 with error: " + e.getMessage());
        }

        // Player 2
        try {
            gameServer.placeShip(2, "D6 D7 D8 D9 D10", 5);
            gameServer.placeShip(2, "C5 D5 E5 F5", 4);
            gameServer.placeShip(2, "J2 J3 J4", 3);
            gameServer.placeShip(2, "A2 A3 A4", 3);
            gameServer.placeShip(2, "E9 E10", 2);
        } catch (GameException e) {
            System.out.println(e.getMessage());
            fail("Ship placement failed for Player 2 with error: " + e.getMessage());
        }
    }



}
