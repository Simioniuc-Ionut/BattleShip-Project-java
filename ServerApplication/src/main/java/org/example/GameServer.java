package org.example;

import lombok.Setter;
import org.example.connection.HttpClient;
import org.example.exception.GameException;
import org.example.shipsModels.PatrolBoat;
import org.example.shipsModels.Ships;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;

@Getter
@Setter
public class GameServer {
    //constante
    public static final int BOARD_SIZE = 10;
    private int port;
    private int timerPort;
    private volatile boolean isRunning;
    private ServerSocket serverSocket;
    private ServerSocket timerServerSocket;

    //private LinkedList<ClientThread> waitingPlayers;
    private char[][] serverBoardPlayer1;
    private char[][] serverBoardPlayer2;

    private Map<Integer,ClientThread> clientThreads;

    private AtomicInteger numberOfPlayers ;

    private GameState currentState;
    private boolean player1IsReadyToPlaceShips;
    private boolean player2IsReadyToPlaceShips;
    private  boolean player1IsReadyToStartGame;
    private  boolean player2IsReadyToStartGame;


    private List<Ships> player1Ships;
    private List<Ships> player2Ships;


    //ships

    public GameServer(int port ,int timerPort) {
        this.port = port;
        this.timerPort=timerPort;
        this.isRunning = false;


        clientThreads = new HashMap<>();
        numberOfPlayers = new AtomicInteger(0);

        currentState = GameState.GAME_NOT_CREATED;
        player1IsReadyToPlaceShips=false;
        player2IsReadyToPlaceShips=false;
        player1IsReadyToStartGame = false;
        player2IsReadyToStartGame = false;


        //waitingPlayers = new LinkedList<>();
        this.serverBoardPlayer1 = new char[BOARD_SIZE][BOARD_SIZE];
        this.serverBoardPlayer2 = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard(serverBoardPlayer1);
        initializeBoard(serverBoardPlayer2);

        player1Ships = new ArrayList<>();
        player2Ships = new ArrayList<>();

    }

    private void initializeBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = '.';
            }
        }
    }
    public synchronized void startTimer(int playerId){
        if(playerId == 1){
            clientThreads.get(2).startTimerThread();
            clientThreads.get(1).stopTimerThread();
        }
        else {
            clientThreads.get(1).startTimerThread();
            clientThreads.get(2).stopTimerThread();
        }
    }

    public synchronized void handleMove(int playerId, String move) {

        int rowMove = move.charAt(0) - 'A';
        int colMove = Integer.parseInt(move.substring(1)) - 1;


        char[][] board = playerId == 1 ? serverBoardPlayer2 : serverBoardPlayer1;
        ClientThread player = clientThreads.get(playerId);

        if (board[rowMove][colMove] == '5' || board[rowMove][colMove] == '4' || board[rowMove][colMove] == '3' || board[rowMove][colMove] == '2' || board[rowMove][colMove] == '1') {
            //you hit
            char shipTypeFromBoard = board[rowMove][colMove];
            board[rowMove][colMove] = 'X';

            //We chose the list of ships of the opponent
            List<Ships> ships = playerId == 1 ? player2Ships : player1Ships;

          //  System.out.println("||||||| ships SIZE ||| " + ships.size() + " For playerID " + playerId + " ships " + ships);
            Iterator<Ships> iterator = ships.iterator();

            //we iterate throught the list of ships and we decrease the size of the ship that was hit .
            //If the size of the ship is 0 we remove it from the list of ships
            //If the list of ships is empty we notify the player that he won
            while (iterator.hasNext()) {

                Ships ship = iterator.next();
                //debug
                //System.out.println("ship char code " +  ship.getShipCodeInChar() + " board code " + shipTypeFromBoard + " ship size" + ship.getShipSize());

                if (ship.getShipCodeInChar() == shipTypeFromBoard) {
                    ship.decreaseShipSize();
                    //debug
                    //System.out.println("||||||| AICI " + ship.getShipSize());
                    player.notifyHit(move);
                    updateInPlayersDb(player,"HIT");

                    if (ship.getShipSize() == 0) {
                        iterator.remove();
                        //debug
                        //System.out.println("Player id : " + playerId + " SHIP SIZE " + ship.getShipSize() +" fro, if ship size == " + ships.size());

                        if (ships.isEmpty()) {
                            System.out.println("ships is empty  : player id is " + playerId);

                            makeGameOver(player);
                        }
                    }
                }else{
                    //ratezi ?
                }

            }
            System.out.println("Player " + playerId + " hit at position: " + move);
        } else {
            board[rowMove][colMove] = '?';
            System.out.println("Player " + playerId + " missed at position: " + move);
            player.notifyMiss(move);
            updateInPlayersDb(player,"MIS");
        }
        Ships ship = new PatrolBoat();

        //System.out.println("BARCA NOU " + ship.getShipSize());
        //displayServerBoard();

    }

    private void resetGame() {
        System.out.println("Server is reseted");
        currentState = GameState.GAME_NOT_CREATED;
        player1IsReadyToPlaceShips=false;
        player2IsReadyToPlaceShips=false;
        player1IsReadyToStartGame = false;
        player2IsReadyToStartGame = false;

        //waitingPlayers = new LinkedList<>();
        this.serverBoardPlayer1 = new char[BOARD_SIZE][BOARD_SIZE];
        this.serverBoardPlayer2 = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard(serverBoardPlayer1);
        initializeBoard(serverBoardPlayer2);

        player1Ships = new ArrayList<>();
        player2Ships = new ArrayList<>();

        clientThreads.get(1).gameReset();
        clientThreads.get(2).gameReset();

    }


    public synchronized int validateShipPosition(int playerId, String move, Ships ship) throws GameException,StringIndexOutOfBoundsException,NullPointerException{
        char[][] board = playerId == 1 ? serverBoardPlayer1 : serverBoardPlayer2;
        String[] positions = move.split(" ");

        int[] shipLengthRows = new int[ship.getShipSize()];
        int[] shipLengthCols = new int[ship.getShipSize()];

        //validare pos : san nu fie ex : A11 sau X10;
        for (String position : positions) {
            if (position.charAt(0) < 'A' || position.charAt(0) > 'J' || Integer.parseInt(position.substring(1)) < 1 || Integer.parseInt(position.substring(1)) > 10) {
                throw new GameException(GameException.ErrorCode.INVALID_POSITION_FORMAT);
            }
        }


        //Verify if the length of pos = with the length of the ship . ex: A1 A2 A3 , ship.length = 3;
        if (positions.length != ship.getShipSize()) {
            System.out.println("Incorect pos ");
            throw new GameException(GameException.ErrorCode.INCORRECT_POSITIONS);
        }

        //validate posittions ex: A1
        // Not A11
        for (int i = 0; i < ship.getShipSize(); i++) {
            try {
                shipLengthRows[i] = positions[i].charAt(0) - 'A';
                shipLengthCols[i] = Integer.parseInt(positions[i].substring(1)) - 1;
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                System.out.println("Incorrect position format");
                throw new GameException(GameException.ErrorCode.INVALID_POSITION_FORMAT);
            }
        }

        for (int i = 0; i < shipLengthRows.length; i++) {
            if (board[shipLengthRows[i]][shipLengthCols[i]] != '.') {
                throw new GameException(GameException.ErrorCode.POSITIONS_NOT_EMPTY);
            }
            if (i > 0) {
                if (!((shipLengthRows[i] == shipLengthRows[i - 1] && Math.abs(shipLengthCols[i] - shipLengthCols[i - 1]) == 1) || (shipLengthCols[i] == shipLengthCols[i - 1] && Math.abs(shipLengthRows[i] - shipLengthRows[i - 1]) == 1))) {
                    throw new GameException(GameException.ErrorCode.POSITIONS_NOT_CONSECUTIVE);
                }
            }
        }


        //afisez doar ca sa vad eu mai bine; o sa sterg
        setShipOnBoard(playerId,board, shipLengthRows, shipLengthCols,ship);
        //displayServerBoard();
        return 0;
    }


    public void displayServerBoard() {
        System.out.println("Server Board Player 1:");
        displayBoard(serverBoardPlayer1);
        System.out.println("Server Board Player 2:");
        displayBoard(serverBoardPlayer2);
    }
    private void displayBoard(char[][] board) {
        System.out.println("      1 2 3 4 5 6 7 8 9 BOARD_SIZE");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((char)('A' + i) + "    ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            timerServerSocket = new ServerSocket(timerPort);
            isRunning = true;
            System.out.println("Server started. Waiting for clients...");

            while (isRunning) {
                try {
                      if(clientThreads.size() < 2) {
                        Socket clientSocket = serverSocket.accept();
                        Socket timerSocket = timerServerSocket.accept();
                        System.out.println("New client connected");
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println("Connection Completed");
                        numberOfPlayers.incrementAndGet();

                        ClientThread client = new ClientThread(clientSocket,timerSocket, this,numberOfPlayers.get());
                        clientThreads.put(numberOfPlayers.get(),client);


                        client.start();

                    }else{
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Cannot connect");

                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println("-10");//Server is Full
                        clientSocket.close();
                    }

                } catch (SocketException e) {
                    if (isRunning) {
                        System.out.println("Error in client connection");
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.out.println("Error in client connection");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error in client connection");
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error when closing server socket");
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
        System.out.println("Server stopped.");

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error when closing server socket");
            e.printStackTrace();
        }
    }
    private void setShipOnBoard(int playerId,char[][] board, int[] rows, int[] cols,Ships ship) {
        for (int i = 0; i < rows.length; i++) {
            board[rows[i]][cols[i]] = Integer.toString(ship.getShipCode()).charAt(0);
        }
        if(playerId == 1){
            player1Ships.add(ship);
        }else{
            player2Ships.add(ship);
        }
    }

    public void playerLeft(ClientThread t){
        clientThreads.remove(t.getPlayerTeamId());
        numberOfPlayers.decrementAndGet();

        //dupa ce se deconectaza playerul ,ii stergem teamid ul din db
       // System.out.println("Sunt in playerLeft ,team id :" + t.getPlayerTeamId());
        updateInPlayersDb(t,"DELETE-TEAMID");
      //  System.out.println("reset : team id :" + t.getPlayerTeamId());
    }

    public static void main(String[] args) {
        int serverPort = 12345;
        int serverTimerPort= 12346;
        GameServer server = new GameServer(serverPort,serverTimerPort);
        server.start();
    }

    public void setClientThreads(int i, ClientThread mockClient) {
        clientThreads.put(i,mockClient);
    }

    public ClientThread getPlayer(int i) {
        return clientThreads.get(i);
    }

    public void makeGameOver(ClientThread player) {
        player.notifyGameOver();
        currentState = GameState.GAME_OVER;
        resetGame();

    }

    //partea de relationare cu bd
    public void updateInGameDb(ClientThread player,String command){
        if(command.equals("WINNER")) {
            System.out.println("Sa apelat updateInGameDb");
            try {
                int playerIdFromDb = HttpClient.getPlayerIdWithPlayerTeamId(player.getPlayerTeamId());
                setWinner(playerIdFromDb);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error in updateInGameDb WINNER");
            }
        }

    }
    public void setWinner(Integer playerIdFromDb){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("playerTeamId", playerIdFromDb);
        } catch (JSONException ex) {
            System.out.println("Erro to jsonObject put values " + ex.getMessage());
            throw new RuntimeException(ex);
        }

        String urlString = "http://localhost:8080/api/games/update/winnerId/" + playerIdFromDb;
        String jsonInputString = jsonObject.toString();
        try {
            String response = HttpClient.sendPostRequest(urlString,jsonInputString);
            System.out.println("Response: " + response);
        } catch (Exception e) {
           System.out.println("Error in setWinner in Game db");
        }

    }


    public void updateInPlayersDb(ClientThread player,String command){
        JSONObject jsonObject = new JSONObject();
        Integer playerTeamId = player.getPlayerTeamId();

        try {
            jsonObject.put("playerTeamId", playerTeamId);
        } catch (JSONException ex) {
            System.out.println("Erro to jsonObject put values " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        String jsonInputString = jsonObject.toString();
        if(command.equals("HIT")){
            hitCountMethode(playerTeamId,jsonInputString);
        }else if(command.equals("DELETE-TEAMID")){
            deleteTeamIdFromDbMethode(playerTeamId,jsonInputString);
        }else if(command.equals("MISS")){
            missCountMethode(playerTeamId,jsonInputString);
        }else if(command.equals("WIN")){
            winCountMethode(playerTeamId,jsonInputString);
        }else if(command.equals("LOSE")){
            loseCountMethode(playerTeamId,jsonInputString);
        }else if(command.equals("MATCH")){
            matchCountMethode(playerTeamId,jsonInputString);
        }

    }
    //endpoint uri
    public void hitCountMethode(Integer playerTeamId,String jsonInputString){

        // Construim URL-ul pentru a trimite cererea
        String urlString = "http://localhost:8080/api/players/update/hitCounts/" + playerTeamId;
        try {
            String response = HttpClient.sendPostRequest(urlString, jsonInputString);
            System.out.println("Response: " + response);

        }catch (Exception ex){

            ex.printStackTrace();
            System.out.println("Failed to send request: " + ex.getMessage() + " in deleteTeamIdFrommDb from GameServer");
        }
    }
    public void deleteTeamIdFromDbMethode(Integer playerTeamId,String jsonInputString){
        // Construim URL-ul pentru a trimite cererea
        String urlString = "http://localhost:8080/api/players/delete/playerTeamId/" + playerTeamId;
        try {
            String response = HttpClient.sendPostRequest(urlString, jsonInputString);
            System.out.println("Response: " + response);

        }catch (Exception ex){

            ex.printStackTrace();
            System.out.println("Failed to send request: " + ex.getMessage() + " in deleteTeamIdFrommDb from GameServer");
        }
    }
    public void missCountMethode(Integer playerTeamId,String jsonInputString){
        // Construim URL-ul pentru a trimite cererea
        String urlString = "http://localhost:8080/api/players/update/missCounts/" + playerTeamId;
        try {
            String response = HttpClient.sendPostRequest(urlString, jsonInputString);
            System.out.println("Response: " + response);

        }catch (Exception ex){

            ex.printStackTrace();
            System.out.println("Failed to send request: " + ex.getMessage() + " in missCountMethode from GameServer");
        }
    }
    public void winCountMethode(Integer playerTeamId,String jsonInputString){
        // Construim URL-ul pentru a trimite cererea
        String urlString = "http://localhost:8080/api/players/update/wins/" + playerTeamId;
        try {
            String response = HttpClient.sendPostRequest(urlString, jsonInputString);
            System.out.println("Response: " + response);

        }catch (Exception ex){

            ex.printStackTrace();
            System.out.println("Failed to send request: " + ex.getMessage() + " in winCountMethode from GameServer");
        }
    }
    public void loseCountMethode(Integer playerTeamId,String jsonInputString){
        // Construim URL-ul pentru a trimite cererea
        String urlString = "http://localhost:8080/api/players/update/loses/" + playerTeamId;
        try {
            String response = HttpClient.sendPostRequest(urlString, jsonInputString);
            System.out.println("Response: " + response);

        }catch (Exception ex){

            ex.printStackTrace();
            System.out.println("Failed to send request: " + ex.getMessage() + " in loseCountMethode from GameServer");
        }
    }
    public void matchCountMethode(Integer playerTeamId,String jsonInputString){
        // Construim URL-ul pentru a trimite cererea
        String urlString = "http://localhost:8080/api/players/update/matches/" + playerTeamId;
        try {
            String response = HttpClient.sendPostRequest(urlString, jsonInputString);
            System.out.println("Response: " + response);

        }catch (Exception ex){

            ex.printStackTrace();
            System.out.println("Failed to send request: " + ex.getMessage() + " in matchCountMethode from GameServer");
        }
    }
}