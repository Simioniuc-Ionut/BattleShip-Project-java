package org.example;

import createOrJoinGame.MainFrameOne;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;
@Getter
@Setter
public class GameClient {
    private final String serverAddress;
    private final int serverPort;
    private String answer;
    private Semaphore ansewerSemaphore = new Semaphore(0);
    private  Semaphore positionIsCorrectlock = new Semaphore(0);
    private Semaphore gameCouldStartlock = new Semaphore(0);
    private boolean positionConfirmed = true;
    private String message;
    private Semaphore messageLock = new Semaphore(0);
    public GameClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        new MainFrameOne(this).setVisible(true);
    }

    public Semaphore getLock() {
        return positionIsCorrectlock;
    }

    public void start() {
        try (
                Socket socket = new Socket(serverAddress, serverPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        ) {
            // Verifică mesajul de stare inițial
            String StatusResponse = in.readLine();
            if (StatusResponse.equals("-10")) {
                System.out.println("Server Is Full");
            } else {
                System.out.println(StatusResponse + "\nConnected to server. Type 'exit' to quit.");

                // Thread pentru a asculta răspunsurile serverului
                Thread serverListener = createServerListenerThread(in);
                serverListener.start();

//                String userInputLine;
//                while ((userInputLine = userInput.readLine()) != null) {
//                    out.println(userInputLine);
//
//                    if (userInputLine.equalsIgnoreCase("exit")) {
//                        System.out.println("Client response: Client exit");
//                        break;
//                    }
//                }
                while (true){
                    ansewerSemaphore.acquire();
                    out.println(answer);
                    if (answer.equalsIgnoreCase("exit")) {
                        System.out.println("Client response: Client exit");
                        break;
                    }
                }

                serverListener.join(); // Așteaptă ca thread-ul de ascultare să se termine
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setAnswer(String answer) {
        System.out.println("Client response: " + answer);
        this.answer = answer;
        ansewerSemaphore.release();
    }
    private Thread createServerListenerThread(BufferedReader in) {
        return new Thread(() -> {
            try {
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println("Server response: " + serverResponse);
                    verifyIfTheGameCouldStartToMove(serverResponse);

                    verifyPositionMove(serverResponse);//primim confirmarea positiei ,daca este valida

                    setMessage(serverResponse);//TRIMIT mesajul catre interfata;
                    messageLock.release();  //las lacatul pt a putea fi citit mesajul

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void verifyIfTheGameCouldStartToMove(String serverResponse) {
        if(serverResponse.startsWith("START-MOVE")){
            gameCouldStartlock.release();
        }
    }

    private void verifyPositionMove(String serverResponse){
        if (serverResponse.startsWith("Err:")){
            positionConfirmed=false;
            //putem notifica
            positionIsCorrectlock.release();

        }else if(serverResponse.startsWith("Cor:")){
            positionConfirmed=true;
            //putem notifica
            positionIsCorrectlock.release();
        }
    }
}
