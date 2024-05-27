package org.example;

import lombok.Setter;
import org.example.exception.GameException;
import org.example.shipsModels.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
@Getter
@Setter

public class ClientThread extends Thread {
    //constante
    public   Ships CARRIER_LENGTH = new Carrier();
    public  Ships BATTLESHIP_LENGTH = new Battleship();
    public  Ships DESTROYER_LENGTH = new Destroyer();
    public  Ships SUBMARINE_LENGTH = new Submarine();
    public  Ships PATROL_BOAT_LENGTH = new PatrolBoat();


    private final Socket clientSocket;
    private PrintWriter out;
    private ClientThread opponent;

    private final GameServer gameServer;
    private static GameState playerTurn;
    private int playerId;
    private boolean shipsPlaced;



    //timer
    private long remainingTimePlayer1 = 30; // timpul initializat pentru player1 (60sec)
    private long remainingTimePlayer2 = 30; // timpul initializat pentru player2 (60sec)
    private final Object lock = new Object();//pt sincronizarea time player1 si player2

    private ScheduledExecutorService timerPlayer1;
    private ScheduledExecutorService timerPlayer2;
    private ScheduledFuture<?> timerTaskPlayer1;
    private ScheduledFuture<?> timerTaskPlayer2;

    //private AtomicInteger state;


    public ClientThread(Socket clientSocket, GameServer gameServer,Integer playerId) {
        this.clientSocket = clientSocket;
        this.gameServer = gameServer;
        this.playerId = playerId;

        this.shipsPlaced = false;
    ;
    }
    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            this.out = out;
            String inputLine;

            out.println("Player id " + playerId);

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server received: " + inputLine);

                if (inputLine.equals("stop") || inputLine.equals("exit")) {
                    out.println("Server stopped");
                    gameServer.stop();
                    break;
                }else if(inputLine.equals("t")){
                     gameServer.setCurrentState(GameState.GAME_TOURNAMENT);
                      gameServer.startTournament();

                } else if(inputLine.equalsIgnoreCase("c") && gameServer.getCurrentState() == GameState.GAME_NOT_CREATED){

                    gameServer.setCurrentState(GameState.WAITING_FOR_PLAYER);
                    //starea de incepere a jocului

                    startGame();
                    joinGame(in);
//                    if(opponent == null){
//                        sendMessage("Waiting for another player to join ...");
//                    }

                } else if(inputLine.equalsIgnoreCase("j")){
                    if(gameServer.getCurrentState() == GameState.WAITING_FOR_PLAYER) {

                        startGame();
                        joinGame(in);


                    }else {
                        sendMessage("Game isn't created.");
                    }
                } else if(gameServer.getCurrentState() == GameState.GAME_READY_TO_MOVE/*inputLine.startsWith("submit move")*/){
                    System.out.println("Player turn " + playerTurn);
                        if(playerId == playerTurn.getStateCode()) {

                            submitMove(inputLine);
                            System.out.println("Player " + playerId + " moved " + inputLine);

                            switchTurn();//schimb turul

                        }else{
                            sendMessage("It's not your turn.");
                        }
                } else if(gameServer.getCurrentState() == GameState.GAME_OVER) {
                    //jocul s a terminat.
                       gameIsFinished();
                       gameReset();
                }else {
                    out.println("Server received the request: " + inputLine);
                }
            }
        } catch (IOException e) {
            handleDisconnection(e);
        }  finally {
            closeClientSocket();

        }
    }

    private void joinGame(BufferedReader in ) throws IOException{
        sendMessage("Game started. PLace your ships.");

        waititngPlayersForJoining();

        setOpponent();

        gameServer.setCurrentState(GameState.GAME_READY_TO_MOVE);
        //trebuiesc modificate
        //placeShip(in, CARRIER_LENGTH);
       // placeShip(in, BATTLESHIP_LENGTH);
       // placeShip(in, DESTROYER_LENGTH);
        //placeShip(in, SUBMARINE_LENGTH);
        placeShip(in, PATROL_BOAT_LENGTH);
        shipsPlaced = true;

        //setam daca playerul este ready
        makePlayerReady();

        checkReadyToStart();
    }
    private void placeShip(BufferedReader in,Ships ship) throws IOException{
        sendMessage("Place your " + ship.getShipName() + " (length = " + ship.getShipSize() + "): ");
        int placed;
        do {
            try {

                String inputLine = in.readLine();
                placed = gameServer.validateShipPosition(playerId, inputLine, ship);

            } catch (GameException e ){
                placed = -1; // ca sa ramana in while

                sendMessage(e.getMessage());

            }
        } while (placed < 0);

    }

    private void submitMove(String inputLine) {
       if(isReadyToMove()){
            String move = inputLine.trim();
            gameServer.handleMove(playerId, move);
            sendMessage("Move submitted: " + move + ". Waiting for opponent's move.");
            opponent.sendMessage("Opponent moved: " + move + ". Your turn.");
        }
    }
    private boolean isReadyToMove(){
        if ( gameServer.getCurrentState() == GameState.GAME_READY_TO_MOVE && playerId != playerTurn.getStateCode()) {
            sendMessage("It's not your turn or game is not ready yet.");
            return false;
        }else {
            return true;
        }
    }
    private synchronized void checkReadyToStart() {
        if(gameServer.isPlayer1IsReady() && gameServer.isPlayer2IsReady()) {
            sendMessage(" PlaBoth players have placed their ships.yer 1 starts.");
            //opponent.sendMessage("PlaBoth players have placed their ships.yer 1 starts.");
            gameServer.setCurrentState(GameState.GAME_READY_TO_MOVE);
            playerTurn = GameState.PLAYER1_TURN;


        }else{
            sendMessage("Waiting for opponent to place ships...");
            while( gameServer.getCurrentState() != GameState.GAME_READY_TO_MOVE){
               waitingThread();
            }
        }
    }

    private  void startGameTimerPlayer1() {
        timerPlayer1 = Executors.newSingleThreadScheduledExecutor();
        timerTaskPlayer1 = timerPlayer1.scheduleAtFixedRate(() -> {
            synchronized (lock) {
                if(playerTurn == GameState.PLAYER1_TURN) {
                    remainingTimePlayer1--;
                   // System.out.println("Remaining time for Player 1: " + remainingTimePlayer1 + " seconds");
                }
                if (remainingTimePlayer1 == 0) {
                    sendMessage("Game over. Your time ran out.");
                    opponent.sendMessage("Game over. You win because your opponent's time ran out.");
                    remainingTimePlayer1--;
                    //stopGameTimerPlayer1();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

//        waitingThread();
//        stopGameTimerPlayer1();
    }

    private  void startGameTimerPlayer2() {
        timerPlayer2 = Executors.newSingleThreadScheduledExecutor();
        timerTaskPlayer2 = timerPlayer2.scheduleAtFixedRate(() -> {
            synchronized (lock) {
                if(playerTurn == GameState.PLAYER2_TURN) {//OPRESC timerul daca nu e randul lui
                    remainingTimePlayer2--;
                  //  System.out.println("Remaining time for Player 2: " + remainingTimePlayer2 + " seconds");
                }
                if (remainingTimePlayer2 == 0) {
                    sendMessage("Game over. Your time ran out.");
                    //stopGameTimerPlayer2();
                    remainingTimePlayer2--;
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
//        waitingThread();
//        stopGameTimerPlayer1();
    }

    private synchronized void switchTurn() {
        if (playerTurn == GameState.PLAYER1_TURN) {
            System.out.println("switchTurn | Player 1 turn ended " + playerTurn);
            //stopGameTimerPlayer1();
            playerTurn = GameState.PLAYER2_TURN;
            startGameTimerPlayer2();
        } else {
            System.out.println("switchTurn | Player 2 turn ended " + playerTurn);
            //stopGameTimerPlayer2();
            playerTurn = GameState.PLAYER1_TURN;
            startGameTimerPlayer1();
        }
    }

    private void makePlayerReady(){
        if(playerId == 1)  {
            gameServer.setPlayer1IsReady(true);
        }else{
            gameServer.setPlayer2IsReady(true);
        }
    }
    private void waititngPlayersForJoining(){
        if (playerId == 1){
            gameServer.setPlayer1IsReady(true);
            while(!gameServer.isPlayer2IsReady()){
                waitingThread();
            }
        }else{
            gameServer.setPlayer2IsReady(true);
            while(!gameServer.isPlayer1IsReady()){
                waitingThread();
            }
        }
    }
    private void waitingThread(){
        try {
            Thread.sleep(1000);
            sendMessage("wait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    private void handleDisconnection(IOException e) {
        System.out.println("Client disconnected abruptly: " + e.getMessage());
//        if (opponent != null) {
//            opponent.sendMessage("Opponent disconnected. Waiting for another player...");
//            //opponent.setOpponent(null);
//            //gameServer.addWaitingPlayer(opponent);
//        }
    }
    private void closeClientSocket() {
        try {
            gameServer.playerLeft(this);
            clientSocket.close();

        } catch (IOException e) {
            System.out.println("Error when closing client socket: " + e.getMessage());
        }
    }
    public void notifyHit(String move) {
        sendMessage("You hit at position: " + move);
    }
    public void notifyMiss(String move) {
        sendMessage("You missed at position: " + move);
    }
    public void setOpponent() {
        if(playerId == 1) {
            int player2 = playerId + 1;
            this.opponent = gameServer.getPlayer(player2);

        }
        else {
            int player1 = playerId - 1;
            this.opponent = gameServer.getPlayer(player1);
        }
    }

//    public void setPlayerId(int id) {
//        this.playerId = id;
//    }
//
//    public ClientThread getOpponent() {
//        return opponent;
//    }

    public void startGame() {
        sendMessage("1-Both players connected. Type 'join game' to start.");
        if (opponent != null) {
            opponent.sendMessage("2-Both players connected. Type 'join game' to start.");
        }

    }

    public void gameIsFinished(){
        System.out.println("func : gameIsFinished() was accessed");
        if(playerTurn == GameState.PLAYER1_TURN){
            gameServer.displayServerBoard();
            gameServer.setCurrentState(GameState.GAME_NOT_CREATED);
        }else{
            gameServer.displayServerBoard();
            gameServer.setCurrentState(GameState.GAME_NOT_CREATED);
        }
    }
    public void notifyGameOver() {
           sendMessage("Game over. You won!");
            opponent.sendMessage("Game over. You lost!");

    }

    private void gameReset() {
        remainingTimePlayer1 = 30;
        remainingTimePlayer2 = 30;
        playerTurn = GameState.PLAYER1_TURN;
        sendMessage("Game has reseted");
           CARRIER_LENGTH = new Carrier();
           BATTLESHIP_LENGTH = new Battleship();
           DESTROYER_LENGTH = new Destroyer();
           SUBMARINE_LENGTH = new Submarine();
           this.PATROL_BOAT_LENGTH = new PatrolBoat();

    }

}