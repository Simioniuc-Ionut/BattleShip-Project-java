import org.example.ClientThread;
import org.example.GameState;
import org.example.exception.GameException;
import org.example.GameServer;
import org.example.shipsModels.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class GameServerTest {
    private GameServer gameServer;

    @BeforeEach
    public void setup() {
    gameServer = new GameServer(12345);
    }

    @Test
    public void testValidateShipPosition() {
        // Player 1
        try {
            gameServer.validateShipPosition(1, "B2 C2 D2 E2 F2", new Carrier());
            gameServer.validateShipPosition(1, "F4 F5 F6 F7", new Battleship()); // aici e o greseala
            gameServer.validateShipPosition(1, "G4 H4 I4", new Destroyer());
            gameServer.validateShipPosition(1, "G7 H7 I7", new Submarine());
            gameServer.validateShipPosition(1, "A8 A9", new PatrolBoat());
        } catch (GameException e) {
            System.out.println(e.getMessage());
            fail("Ship placement failed for Player 1 with error: " + e.getMessage());
        }

        // Player 2
        try {
            gameServer.validateShipPosition(2, "D6 D7 D8 D9 D10",  new Carrier());
            gameServer.validateShipPosition(2, "C5 D5 E5 F5", new Battleship());
            gameServer.validateShipPosition(2, "J2 J3 J4", new Destroyer());
            gameServer.validateShipPosition(2, "A2 A3 A4", new Submarine());
            gameServer.validateShipPosition(2, "E9 E10", new PatrolBoat());
        } catch (GameException e) {
            System.out.println(e.getMessage());
            fail("Ship placement failed for Player 2 with error: " + e.getMessage());
        }
    }
@Test
public void testGamePlayWithMockClient() {
    // Creăm un mock pentru ClientThread
    ClientThread mockClient1 = Mockito.mock(ClientThread.class);
    ClientThread mockClient2 = Mockito.mock(ClientThread.class);

    gameServer.setCurrentState(GameState.WAITING_FOR_PLAYER);
    //mockClient1.startGame();

    if(GameState.WAITING_FOR_PLAYER == gameServer.getCurrentState()){
        //mockClient2.startGame();
    }

    //metodele din join
    gameServer.setCurrentState(GameState.GAME_READY_TO_MOVE);

    // Plasăm navele pentru fiecare jucător
    try {
        gameServer.validateShipPosition(1, "A1 A2", new PatrolBoat());
        gameServer.validateShipPosition(2, "B1 B2", new PatrolBoat());
    } catch (GameException e) {
        fail("Ship placement failed with error: " + e.getMessage());
    }
    //make playres ready
    gameServer.setPlayer1IsReadyToStartGame(true);
    gameServer.setPlayer2IsReadyToStartGame(true);

    //ready to start function
    gameServer.setCurrentState(GameState.GAME_READY_TO_MOVE);

    //setam clients threadss
    gameServer.setClientThreads(1, mockClient1);
    gameServer.setClientThreads(2, mockClient2);
    // Simulăm o rundă de joc
    gameServer.handleMove(1, "B1");
    gameServer.handleMove(2, "A1");
    //runda 2
    gameServer.handleMove(1, "B2");
    // Set the game state to GAME_OVER to trigger gameIsFinished()
    gameServer.setCurrentState(GameState.GAME_OVER);

    // Verificăm dacă jocul s-a terminat
    assertEquals(GameState.GAME_OVER, gameServer.getCurrentState());


    // Verificăm dacă metodele gameIsFinished() și notifyGameOver() au fost apelate
    verify(mockClient1, times(1)).notifyHit("B1");
    verify(mockClient2, times(1)).notifyHit("A1");
    verify(mockClient1, times(1)).notifyHit("B2");

    verify(mockClient1, times(1)).notifyGameOver();
}


}
