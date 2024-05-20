package org.example;

import org.example.Exception.GameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientThread extends Thread {
    //constante
    public static final int CARRIER_LENGTH = 5;
    public static final int BATTLESHIP_LENGTH = 4;
    public static final int DESTROYER_LENGTH = 3;
    public static final int SUBMARINE_LENGTH = 3;
    public static final int PATROL_BOAT_LENGTH = 2;
    private final Socket clientSocket;
    private final GameServer gameServer;

    private PrintWriter out;
    private ClientThread opponent;
    private int playerId;
    private boolean shipsPlaced;
    private boolean moveSubmitted;

    public ClientThread(Socket clientSocket, GameServer gameServer) {
        this.clientSocket = clientSocket;
        this.gameServer = gameServer;
        this.playerId = 0;
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

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server received: " + inputLine);

                if (inputLine.equals("stop") || inputLine.equals("exit")) {
                    out.println("Server stopped");
                    gameServer.stop();
                    break;
                } else if(inputLine.equalsIgnoreCase("create game")){
                    gameServer.addWaitingPlayer(this);

                    if(opponent == null){
                        sendMessage("Waiting for another player to join ...");
                    }
                } else if(inputLine.equalsIgnoreCase("join game")){
                    joinGame(in);
                } else if(inputLine.startsWith("submit move")){
                    submitMove(inputLine);
                } else {
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
        placeShip(in, "Carrier", CARRIER_LENGTH);
        placeShip(in, "Battleship", BATTLESHIP_LENGTH);
        placeShip(in, "Destroyer", DESTROYER_LENGTH);
        placeShip(in, "Submarine", SUBMARINE_LENGTH);
        placeShip(in, "Patrol Boat", PATROL_BOAT_LENGTH);
        shipsPlaced = true;
        checkReadyToStart();
    }
    private void placeShip(BufferedReader in, String shipName, int length) throws IOException{
        sendMessage("Place your " + shipName + " (length = " + length + "): ");
        int placed;
        do {
            try {

                String inputLine = in.readLine();
                placed = gameServer.placeShip(playerId, inputLine, length);

            } catch (GameException e ){
                placed = -1; // ca sa ramana in while

                sendMessage(e.getMessage());

            }
        } while (placed < 0);

    }

    private void submitMove(String inputLine) {
        if (moveSubmitted || !shipsPlaced || opponent == null || !opponent.shipsPlaced) {
            sendMessage("It's not your turn or game is not ready yet.");
        } else {
            String move = inputLine.substring(12).trim();
            gameServer.handleMove(playerId, move);
            moveSubmitted = true;
            opponent.moveSubmitted = false;
            sendMessage("Move submitted: " + move + ". Waiting for opponent's move.");
            opponent.sendMessage("Opponent moved: " + move + ". Your turn.");
        }
    }

    private synchronized void checkReadyToStart() {
        if (shipsPlaced && opponent != null && opponent.shipsPlaced) {
            sendMessage("Both players have placed their ships. Player 1 starts. Type 'submit move <position>' to play.");
            if (playerId == 1) {
                sendMessage("Your turn.");
                opponent.sendMessage("Waiting for Player 1 to move.");
            } else {
                sendMessage("Waiting for Player 1 to move.");
                opponent.sendMessage("Your turn.");
            }
        } else if (opponent != null && !opponent.shipsPlaced) {
            sendMessage("Waiting for opponent to place ships...");
        }
    }
    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    private void handleDisconnection(IOException e) {
        System.out.println("Client disconnected abruptly: " + e.getMessage());
        if (opponent != null) {
            opponent.sendMessage("Opponent disconnected. Waiting for another player...");
            opponent.setOpponent(null);
            gameServer.addWaitingPlayer(opponent);
        }
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
    public void setOpponent(ClientThread opponent) {
        this.opponent = opponent;
        if (opponent != null) {
            this.playerId = 1;
            opponent.setPlayerId(2);
        }
    }
    public void setPlayerId(int id) {
        this.playerId = id;
    }
    public ClientThread getOpponent() {
        return opponent;
    }
    public void startGame() {
        sendMessage("1-Both players connected. Type 'join game' to start.");
        if (opponent != null) {
            opponent.sendMessage("2-Both players connected. Type 'join game' to start.");
        }
    }

    //    private String getErrorMessage(int errorCode) {
//        switch (errorCode) {
//            case -1:
//                return "Error: Incorrect number of positions. Expected 5 positions.";
//            case -2:
//                return "Error: Invalid position format. Use format 'A1', 'B2', etc.";
//            case -3:
//                return "Error: All positions must be empty.";
//            case -4:
//                return "Error: Invalid position format. Must be consecutive.";
//            default:
//                return "Unknown error.";
//        }
//    }
}