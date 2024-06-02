package org.example;



import lombok.Setter;
import org.example.exception.GameException;
import org.example.shipsModels.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import lombok.Getter;

@Getter
@Setter

public class ClientThread extends Thread {
    //constante
    public Ships CARRIER_LENGTH = new Carrier();
    public Ships BATTLESHIP_LENGTH = new Battleship();
    public Ships DESTROYER_LENGTH = new Destroyer();
    public Ships SUBMARINE_LENGTH = new Submarine();
    public Ships PATROL_BOAT_LENGTH = new PatrolBoat();


    private final Socket clientSocket;
    private final Socket timerSocket;
    private PrintWriter out;
    private ClientThread opponent;

    private final GameServer gameServer;
    private static GameState playerTurn;
    private int playerTeamId;
    private boolean shipsPlaced;

    private boolean playerFinishStatus;
    private TimerThread timer;
    private PrintWriter timerOut;
    //timer
    private int minutesTimerPlayer = 3;
    private int secondsTimerPlayer = 5;

    private boolean isTimerThreadRunning=false;




    public ClientThread(Socket clientSocket, Socket timerSocket,GameServer gameServer,Integer playerTeamId) {
        this.clientSocket = clientSocket;
        this.timerSocket = timerSocket;

        this.gameServer = gameServer;
        this.playerTeamId = playerTeamId;

        this.shipsPlaced = false;
        playerFinishStatus=false;


        try {
            this.timerOut = new PrintWriter(timerSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Timer printeWriter error in ClientThread");

        }

        if(!isTimerThreadRunning()) {
            this.timer = new TimerThread(minutesTimerPlayer,secondsTimerPlayer ,playerTeamId, timerOut, gameServer,this);
            timer.start();
            setTimerThreadRunning(true);
        }
        //timer.startTimer();
    }
    public void startTimerThread()
    {
        timer.startTimer();
    }
    public void stopTimerThread(){
        timer.pauseTimer();
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            this.out = out;
            String inputLine;

            out.println("ID: " + playerTeamId); //trimit idiul

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server received: " + inputLine);

                if (inputLine.equals("stop") || inputLine.equals("exit"))
                {
                    out.println("Server stopped");
                    gameServer.stop();
                    break;
                } else if(inputLine.equalsIgnoreCase("c") && gameServer.getCurrentState() == GameState.GAME_NOT_CREATED){

                    gameServer.setCurrentState(GameState.WAITING_FOR_PLAYER);
                    //starea de incepere a jocului
                    startGameMessage();
                    joinGame(in);

                } else if(inputLine.equalsIgnoreCase("j")){
                    if(gameServer.getCurrentState() == GameState.WAITING_FOR_PLAYER) {

                        startGameMessage();
                        joinGame(in);


                    }else {
                        sendMessage("Game isn't created.");
                    }
                } else if(gameServer.getCurrentState() == GameState.GAME_READY_TO_MOVE/*inputLine.startsWith("submit move")*/){
                    System.out.println("Player turn " + playerTurn);
                        if(playerTeamId == playerTurn.getStateCode()) {

                            gameServer.startTimer(playerTeamId);

                            submitMove(inputLine);//fac mutarea

                            System.out.println("Player " + playerTeamId + " moved " + inputLine + " status timer " + this.timer.isStart() + " TIMPUL > " + this.timer.getTimeRemaining());

                            switchTurn();//schimb turul


                        }else{
                            sendMessage("NOT_YOUR_TURN: " + inputLine);
                        }
              }
                else {
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

        waititngPlayersForJoining(); //se asteapta sa dea join,inainte sa plaseze pe mapa
        setOpponent();

        gameServer.setCurrentState(GameState.GAME_READY_TO_MOVE);
        //trebuiesc modificate
//        placeShip(in, CARRIER_LENGTH);
//        placeShip(in, BATTLESHIP_LENGTH);
//        placeShip(in, DESTROYER_LENGTH);
//        placeShip(in, SUBMARINE_LENGTH);
        placeShip(in, PATROL_BOAT_LENGTH);
        shipsPlaced = true;

        listenReadyFromClient(in);
        waitingPlayersToFinishPlacingShips();


        checkReadyToStart();
        if(playerTeamId == 1) {
          //  System.out.println("AM INTRAT PE IF  " + playerTeamId);
            gameServer.startTimer(gameServer.getClientThreads().get(playerTeamId).getOpponent().playerTeamId);
        }else{
           // System.out.println("AM INTRAT PE ELSE " + playerTeamId);
            gameServer.startTimer(gameServer.getClientThreads().get(playerTeamId).playerTeamId);
        }
    }

    private void waitingPlayersToFinishPlacingShips() {
      if(playerTeamId == 1) {

        gameServer.setPlayer1IsReadyToStartGame(true);
        sendMessage("Waiting player 2");
          while (!gameServer.isPlayer2IsReadyToStartGame()) {
              waitingThread();
          }
      }else{
          sendMessage("Waiting player 1");
          gameServer.setPlayer2IsReadyToStartGame(true);
          while (!gameServer.isPlayer1IsReadyToStartGame()) {
              waitingThread();
          }
      }
    }
    private void placeShip(BufferedReader in,Ships ship) throws IOException{
        sendMessage("Place your " + ship.getShipName() + " (length = " + ship.getShipSize() + "): ");
        int placed;
        do {
            try {

                String inputLine = in.readLine();
                placed = gameServer.validateShipPosition(playerTeamId, inputLine, ship);

                sendMessage("Cor:" + "Ship is correctly placed");
            } catch (GameException | StringIndexOutOfBoundsException | NullPointerException e) {
                placed = -1; // ca sa ramana in while

                sendMessage("Err:" + e.getMessage());

            }
        } while (placed < 0);

    }

    private void submitMove(String inputLine) {
        if(isReadyToMove()) {
            String move = inputLine.trim();
            gameServer.handleMove(playerTeamId, move);
           // sendMessage("Move submitted: " + move + ". Waiting for opponent's move.");
            if(gameServer.getCurrentState() != GameState.GAME_OVER){
            opponent.sendMessage("Opponent moved: " + move + ". Your turn.");}
        }
    }
    private boolean isReadyToMove(){
        if ( gameServer.getCurrentState() == GameState.GAME_READY_TO_MOVE && playerTeamId != playerTurn.getStateCode()) {
            sendMessage("It's not your turn or game is not ready yet.");
            return false;
        }else {
            return true;
        }
    }

    private void checkReadyToStart() {
        //  System.out.println("sunt in checlReadyToStart " + gameServer.islayer1IsReadyToPlaceShips() + " al 2 " + gameServer.isPlayer2IsReady());
        if (gameServer.isPlayer2IsReadyToStartGame() && gameServer.isPlayer1IsReadyToStartGame()) {
            //sendMessage(" PlaBoth players have placed their ships.yer 1 starts.");
            //opponent.sendMessage("PlaBoth players have placed their ships.yer 1 starts.");
            gameServer.setCurrentState(GameState.GAME_READY_TO_MOVE);
            playerTurn = GameState.PLAYER1_TURN;

            sendTheGameCouldStart();

        } else {
            // sendMessage("Waiting for opponent to place ships...");
            while (gameServer.getCurrentState() != GameState.GAME_READY_TO_MOVE) {
                waitingThread();
            }
            //sendMessage("waiting is finished");
        }
    }

    private synchronized void switchTurn() {
        if (playerTurn == GameState.PLAYER1_TURN) {
            System.out.println("switchTurn | Player 1 turn ended " + playerTurn);
            //stopGameTimerPlayer1();
            playerTurn = GameState.PLAYER2_TURN;

           // startGameTimerPlayer2();
        } else {
            System.out.println("switchTurn | Player 2 turn ended " + playerTurn);
            //stopGameTimerPlayer2();
            playerTurn = GameState.PLAYER1_TURN;
            //startGameTimerPlayer1();
        }
    }

    private synchronized void listenReadyFromClient(BufferedReader in){

        try {
            String ready = in.readLine();
            System.out.println("mesajul din listenReadyFromClient " + ready + " isReadyplayer1 " + gameServer.isPlayer1IsReadyToStartGame() + " isReadyplayer2 " + gameServer.isPlayer2IsReadyToStartGame());

            if(ready.equals("READY")){
                sendMessage("Player " + playerTeamId + " is ready");

            }else{
                sendMessage("Player is not ready");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waititngPlayersForJoining(){
        if (playerTeamId == 1){
            gameServer.setPlayer1IsReadyToPlaceShips(true);
            while(!gameServer.isPlayer2IsReadyToPlaceShips()){
                waitingThread();
            }
        }else{
            gameServer.setPlayer2IsReadyToPlaceShips(true);
            while(!gameServer.isPlayer1IsReadyToPlaceShips()){
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
//         opponent.sendMessage("Opponent disconnected. Waiting for another player...");
//           opponent.setOpponent(null);
//        }
    }

    private void closeClientSocket() {
        try {
            System.out.println("Socket was closed for player " + playerTeamId);
            gameServer.playerLeft(this);
            clientSocket.close();

        } catch (IOException e) {
            System.out.println("Error when closing client socket: " + e.getMessage());
        }
    }
    public void notifyHit(String move) {
        sendMessage("You hit at position: " + move + ". Waiting for opponent's move ");
    }
    public void notifyMiss(String move) {
        sendMessage("You missed at position: " + move + ". Waiting for opponent's move");
    }
    public void setOpponent() {
        if(playerTeamId == 1) {
            int player2 = playerTeamId + 1;
            this.opponent = gameServer.getPlayer(player2);

        } else {
            int player1 = playerTeamId - 1;
            this.opponent = gameServer.getPlayer(player1);
        }
    }

    public void startGameMessage() {
        sendMessage("1-Both players connected. Type 'join game' to start.");
        if (opponent != null) {
            opponent.sendMessage("2-Both players connected. Type 'join game' to start.");
        }

    }

//    public void gameIsFinished(){
//        System.out.println("func : gameIsFinished() was accessed");
//        if(playerTurn == GameState.PLAYER1_TURN){
//            gameServer.displayServerBoard();
//            gameServer.setCurrentState(GameState.GAME_NOT_CREATED);
//        }else{
//            gameServer.displayServerBoard();
//            gameServer.setCurrentState(GameState.GAME_NOT_CREATED);
//        }
//    }
    public void notifyGameOver() {

                System.out.println("DE CATE ORI SE APELEAZa ?");
                sendMessage("Game over. You won!");
                gameServer.updateInPlayersDb(this,"WIN");
                gameServer.updateInPlayersDb(this,"MATCH");

                gameServer.updateInGameDb(this,"WINNER");

                opponent.sendMessage("Game over. You lost!");
                gameServer.updateInPlayersDb(opponent,"LOSE");
                gameServer.updateInPlayersDb(opponent,"MATCH");

    }

    public void gameReset() {
        //remainingTimePlayer1 = 30;
        //remainingTimePlayer2 = 30;
        //finishTimerThread();
        //Redeschidem timerul
        //this.timer = new TimerThread(timerPlayer,playerTeamId);
        //timer.start();

        minutesTimerPlayer =0;
        secondsTimerPlayer = 10;

        playerTurn = GameState.PLAYER1_TURN;
        //sendMessage("Game has reseted");
           CARRIER_LENGTH = new Carrier();
           BATTLESHIP_LENGTH = new Battleship();
           DESTROYER_LENGTH = new Destroyer();
           SUBMARINE_LENGTH = new Submarine();
           this.PATROL_BOAT_LENGTH = new PatrolBoat();

        System.out.println("Am dat gameReset in ClientThread");


    }
    private void sendTheGameCouldStart(){
        sendMessage("START-MOVE");
    }
}