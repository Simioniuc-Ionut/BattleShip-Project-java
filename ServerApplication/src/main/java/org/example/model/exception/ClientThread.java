package org.example.model.exception;

import org.example.model.exception.shipsModels.Ships;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClientThread extends Thread {
    //constante
    public static final int CARRIER_LENGTH = 5;
    public static final int BATTLESHIP_LENGTH = 4;
    public static final int DESTROYER_LENGTH = 3;
    public static final int SUBMARINE_LENGTH = 3;
    //public static final int PATROL_BOAT_LENGTH = 2;
    public static final Ships PATROL_BOAT_LENGTH = Ships.PATROL_BOAT_LENGTH;


    private final Socket clientSocket;
    private PrintWriter out;
    private ClientThread opponent;

    private final GameServer gameServer;
    private static GameState playerTurn;
    private int playerId;
    private boolean shipsPlaced;
    private boolean moveSubmitted;


    //timer
    private long remainingTimePlayer1 = 30; // timpul initializat pentru player1 (60sec)
    private long remainingTimePlayer2 = 30; // timpul initializat pentru player2 (60sec)
    private final Object lock = new Object();//pt sincronizarea time player1 si player2

    private ScheduledExecutorService timerPlayer1;
    private ScheduledExecutorService timerPlayer2;
    private ScheduledFuture<?> timerTaskPlayer1;
    private ScheduledFuture<?> timerTaskPlayer2;

    //private AtomicInteger state;


    public ClientThread(Socket clientSocket, GameServer gameServer) {
        this.clientSocket = clientSocket;
        this.gameServer = gameServer;
        this.playerId = gameServer.getNumberOfPlayers().get(); //l am refacut/

        this.shipsPlaced = false;
        this.moveSubmitted = false;
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
                } else if(inputLine.equalsIgnoreCase("c") && gameServer.getCurrentState() == GameState.GAME_NOT_CREATED){
                    gameServer.addWaitingPlayer(this);
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

                        //gameServer.setCurrentState(GameState.PLAYER1_TURN);
                        // playerTurn = GameState.PLAYER1_TURN; //punem randul playerului 1

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

        gameServer.setCurrentState(GameState.GAME_READY_TO_MOVE);
        //trebuiesc modificate
        //placeShip(in, "Carrier", CARRIER_LENGTH);
       // placeShip(in, "Battleship", BATTLESHIP_LENGTH);
       // placeShip(in, "Destroyer", DESTROYER_LENGTH);
        //placeShip(in, "Submarine", SUBMARINE_LENGTH);
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
            //opponent.sendMessage("Opponent moved: " + move + ". Your turn.");
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
//        if (shipsPlaced && opponent != null && opponent.shipsPlaced) {
//            sendMessage("Both players have placed their ships. Player 1 starts. Type 'submit move <position>' to play.");
//            if (playerId == 1) {
//                sendMessage("Your turn.");
//                opponent.sendMessage("Waiting for Player 1 to move.");
//            } else {
//                sendMessage("Waiting for Player 1 to move.");
//                opponent.sendMessage("Your turn.");
//            }
//        } else if (opponent != null && !opponent.shipsPlaced) {
//            sendMessage("Waiting for opponent to place ships...");
//        }
        if(gameServer.isPlayer1IsReady() && gameServer.isPlayer2IsReady()) {
            sendMessage(" PlaBoth players have placed their ships.yer 1 starts.");
            //opponent.sendMessage("PlaBoth players have placed their ships.yer 1 starts.");
            gameServer.setCurrentState(GameState.GAME_READY_TO_MOVE);
            playerTurn = GameState.PLAYER1_TURN;
//            if (playerId == 1) {
//                sendMessage("Your turn.");
//                opponent.sendMessage("Waiting for Player 1 to move.");
//            } else {
//                sendMessage("Waiting for Player 1 to move.");
//                opponent.sendMessage("Your turn.");
//            }

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
                if (remainingTimePlayer1 <= 0) {
                    sendMessage("Game over. Your time ran out.");
                    opponent.sendMessage("Game over. You win because your opponent's time ran out.");
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
                if (remainingTimePlayer2 <= 0) {
                    sendMessage("Game over. Your time ran out.");
                    opponent.sendMessage("Game over. You win because your opponent's time ran out.");
                    //stopGameTimerPlayer2();
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
//    public void setOpponent(ClientThread opponent) {
//        this.opponent = opponent;
//        if (opponent != null) {
//            this.playerId = 1;
//            opponent.setPlayerId(2);
//        }
//    }

    public void setPlayerId(int id) {
        this.playerId = id;
    }

    public ClientThread getOpponent() {
        return opponent;
    }

    public void startGame() {
        sendMessage("1-Both players connected. Type 'join game' to start.");
//        if (opponent != null) {
//            opponent.sendMessage("2-Both players connected. Type 'join game' to start.");
//        }

    }

    private void gameIsFinished(){
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
//        if (opponent != null) {
//            opponent.sendMessage("Game over. You lost!");
//        }
    }
}