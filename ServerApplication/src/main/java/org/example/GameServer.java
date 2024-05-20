package org.example;

import org.example.Exception.GameException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


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
    private volatile boolean isStarted = false;

    public GameServer(int port) {
        this.port = port;
        this.isRunning = false;

        clientThreads = new ArrayList<>();

        waitingPlayers = new LinkedList<>();
        this.serverBoardPlayer1 = new char[BOARD_SIZE][BOARD_SIZE];
        this.serverBoardPlayer2 = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard(serverBoardPlayer1);
        initializeBoard(serverBoardPlayer2);
    }

    private void initializeBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = '.';
            }
        }
    }

    public synchronized void addWaitingPlayer(ClientThread player) {
        waitingPlayers.add(player);
        if (waitingPlayers.size() == 2) {
            ClientThread player1 = waitingPlayers.removeFirst();
            ClientThread player2 = waitingPlayers.removeFirst();
            player1.setOpponent(player2);
            player2.setOpponent(player1);
            player1.startGame();
        }
    }

    private void validateShipPositions(char[][] board, int[] rows, int[] cols) throws GameException{
        for (int i = 0; i < rows.length; i++) {
            if (board[rows[i]][cols[i]] != '.') {
                throw new GameException(GameException.ErrorCode.POSITIONS_NOT_EMPTY);
            }
            if (i > 0) {
                if (!((rows[i] == rows[i - 1] && Math.abs(cols[i] - cols[i - 1]) == 1) || (cols[i] == cols[i - 1] && Math.abs(rows[i] - rows[i - 1]) == 1))) {
                    throw new GameException(GameException.ErrorCode.POSITIONS_NOT_CONSECUTIVE);
                }
            }
        }
    }

    private void placeShipOnBoard(char[][] board, int[] rows, int[] cols) {
        for (int i = 0; i < rows.length; i++) {
            if (i == 0 || i == rows.length - 1) {
                board[rows[i]][cols[i]] = '#'; // First and last positions of the ship
            } else {
                board[rows[i]][cols[i]] = 'o'; // Intermediate positions of the ship
            }
        }
    }
    public synchronized int placeShip(int playerId, String input, int length) throws GameException{
        char[][] board = playerId == 1 ? serverBoardPlayer1 : serverBoardPlayer2;
        String[] positions = input.split(" ");
        if (positions.length != length) {
            System.out.println("Incorect pos ");
            throw new GameException(GameException.ErrorCode.INCORRECT_POSITIONS);
        }

        int[] rows = new int[length];
        int[] cols = new int[length];

        for (int i = 0; i < length; i++) {
            try {
                rows[i] = positions[i].charAt(0) - 'A';
                cols[i] = Integer.parseInt(positions[i].substring(1)) - 1;
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                System.out.println("Incorect pos format ");
                throw new GameException(GameException.ErrorCode.INVALID_POSITION_FORMAT);
            }
        }

        validateShipPositions(board, rows, cols);
        placeShipOnBoard(board, rows, cols);

        displayServerBoard();
        return 0;
    }

    public synchronized void handleMove(int playerId, String move) {
        int row = move.charAt(0) - 'A';
        int col = Integer.parseInt(move.substring(1)) - 1;

        char[][] board = playerId == 1 ? serverBoardPlayer2 : serverBoardPlayer1;

        if (board[row][col] == 'S') {
            board[row][col] = 'X';
            System.out.println("Player " + playerId + " hit at position: " + move);
            ClientThread player = playerId == 1 ? waitingPlayers.getFirst() : waitingPlayers.getLast();
            player.notifyHit(move);
            player.getOpponent().notifyHit(move);
        } else {
            board[row][col] = 'O';
            System.out.println("Player " + playerId + " missed at position: " + move);
            ClientThread player = playerId == 1 ? waitingPlayers.getFirst() : waitingPlayers.getLast();
            player.notifyMiss(move);
            player.getOpponent().notifyMiss(move);
        }

        displayServerBoard();
    }

    private void displayServerBoard() {
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


    public void playerLeft(Thread t){
        clientThreads.remove(t);
    }

    public static void main(String[] args) {
        int serverPort = 12345;
        GameServer server = new GameServer(serverPort);
        server.start();
    }
}