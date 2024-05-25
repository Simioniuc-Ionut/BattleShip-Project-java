package org.example.model.exception;

import org.example.model.exception.shipsModels.Ships;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//import lombok.Getter;
//
//@Getter
//@Setter
public class GameServer {
    //constante
    public static final int BOARD_SIZE = 10;
    private int port;
    private volatile boolean isRunning;
    private ServerSocket serverSocket;

    private LinkedList<ClientThread> waitingPlayers;
    private char[][] serverBoardPlayer1;
    private char[][] serverBoardPlayer2;

    private List<Thread> clientThreads;

    private AtomicInteger numberOfPlayers ;

    private GameState currentState;
    private boolean player1IsReady;
    private boolean player2IsReady;


    private List<Ships> player1Ships;
    private List<Ships> player2Ships;

    //ships

    public GameServer(int port) {
        this.port = port;
        this.isRunning = false;

        clientThreads = new ArrayList<>();
        numberOfPlayers = new AtomicInteger(0);

        currentState = GameState.GAME_NOT_CREATED;
        player1IsReady=false;
        player2IsReady=false;

        waitingPlayers = new LinkedList<>();
        this.serverBoardPlayer1 = new char[BOARD_SIZE][BOARD_SIZE];
        this.serverBoardPlayer2 = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard(serverBoardPlayer1);
        initializeBoard(serverBoardPlayer2);

        //setam sizeul la 1 pt a testa mai usor


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




    public synchronized void handleMove(int playerId, String move) {

        int rowMove = move.charAt(0) - 'A';
        int colMove = Integer.parseInt(move.substring(1)) - 1;


        char[][] board = playerId == 1 ? serverBoardPlayer2 : serverBoardPlayer1;
        ClientThread player = playerId == 1 ? waitingPlayers.getFirst() : waitingPlayers.getLast();

        if (board[rowMove][colMove] == '5' || board[rowMove][colMove] == '4' || board[rowMove][colMove] == '3' || board[rowMove][colMove] == '2' || board[rowMove][colMove] == '1') {
            char shipTypeFromBoard = board[rowMove][colMove];
            board[rowMove][colMove] = 'X';

            //We chose the list of ships of the opponent
            List<Ships> ships ;
            if(playerId == 1){
                System.out.println("Am instantiati playershil2 cu player1");
                ships = player2Ships;}
                else{
                System.out.println("Am instantiati playershil1 cu player2");
                    ships = player1Ships;
                }

            System.out.println("||||||| ships SIZE ||| " + ships.size() + " For playerID " + playerId + " ships " + ships);
            Iterator<Ships> iterator = ships.iterator();

            //we iterate throught the list of ships and we decrease the size of the ship that was hit .
            //If the size of the ship is 0 we remove it from the list of ships
            //If the list of ships is empty we notify the player that he won
            while (iterator.hasNext()) {

                Ships ship = iterator.next();
                //debug
                System.out.println("ship char code " +  ship.getShipCodeInChar() + " board code " + shipTypeFromBoard + " ship size" + ship.getShipSize());

                if (ship.getShipCodeInChar() == shipTypeFromBoard) {
                    ship.decreaseShipSize();
                    //debug
                    System.out.println("||||||| AICI " + ship.getShipSize());
                    player.notifyHit(move);
                    if (ship.getShipSize() == 0) {
                        iterator.remove();
                        //debug
                        System.out.println("Player id : " + playerId + " SHIP SIZE " + ship.getShipSize() +" fro, if ship size == " + ships.size());

                        if (ships.isEmpty()) {
                            System.out.println("ships is empty  : player id is " + playerId);

                            player.notifyGameOver();
                            currentState = GameState.GAME_OVER;
                        }
                    }
                }
            }
            System.out.println("Player " + playerId + " hit at position: " + move);
        } else {
            board[rowMove][colMove] = '?';
            System.out.println("Player " + playerId + " missed at position: " + move);
            player.notifyMiss(move);
        }
        Ships ship = Ships.PATROL_BOAT_LENGTH;
        System.out.println("BARCA NOUA : size " + ship.getShipSize());
        displayServerBoard();
    }


    public synchronized void addWaitingPlayer(ClientThread player) {
        waitingPlayers.add(player);
        if (waitingPlayers.size() == 2) {
            ClientThread player1 = waitingPlayers.removeFirst();
            ClientThread player2 = waitingPlayers.removeFirst();
//             player1.setOpponent(player2);
//            player2.setOpponent(player1);
            player1.startGame();
        }
    }


    public synchronized int validateShipPosition(int playerId, String move, Ships ship) throws GameException{
        char[][] board = playerId == 1 ? serverBoardPlayer1 : serverBoardPlayer2;
        String[] positions = move.split(" ");

        int[] shipLengthRows = new int[ship.getShipSize()];
        int[] shipLengthCols = new int[ship.getShipSize()];

        //validateShipPositions(board, shipLengthRows, shipLengthCols,ship,positions);

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
        displayServerBoard();
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
            isRunning = true;
            System.out.println("Server started. Waiting for clients...");

            while (isRunning) {
                try {
                    if(clientThreads.size() < 2) {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("New client connected");
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println("Connection Completed");

                        numberOfPlayers.incrementAndGet();
                        Thread client = new ClientThread(clientSocket, this);

                        client.start();
                        clientThreads.add(client);


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

    public void playerLeft(Thread t){
        clientThreads.remove(t);
        numberOfPlayers.decrementAndGet();
    }

    public AtomicInteger getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public boolean isPlayer2IsReady() {
        return player2IsReady;
    }

    public boolean isPlayer1IsReady() {
        return player1IsReady;
    }

    public void setPlayer2IsReady(boolean player2IsReady) {
        this.player2IsReady = player2IsReady;
    }

    public void setPlayer1IsReady(boolean player1IsReady) {
        this.player1IsReady = player1IsReady;
    }

    public static void main(String[] args) {
        int serverPort = 12345;
        GameServer server = new GameServer(serverPort);
        server.start();
    }

}