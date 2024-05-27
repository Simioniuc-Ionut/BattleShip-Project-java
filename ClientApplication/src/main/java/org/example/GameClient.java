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

public class GameClient {
    private final String serverAddress;
    private final int serverPort;
    private String answer;
    private Semaphore semaphore = new Semaphore(0);
    private  Semaphore lock = new Semaphore(0);
    @Getter
    private boolean positionConfirmed = true;
    @Getter
    @Setter
    private String message;
    @Getter
    private Semaphore messageLock = new Semaphore(0);
    public GameClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        new MainFrameOne(this).setVisible(true);
    }

    public Semaphore getLock() {
        return lock;
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
                    semaphore.acquire();
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
        semaphore.release();
    }
    private Thread createServerListenerThread(BufferedReader in) {
        return new Thread(() -> {
            try {
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println("Server response: " + serverResponse);
                    if (serverResponse.startsWith("Err:")){
                        positionConfirmed=false;
                        //putem notifica
                        lock.release();

                    }else if(serverResponse.startsWith("Cor:")){
                        positionConfirmed=true;
                        //putem notifica
                       lock.release();
                    }
                    setMessage(message);
                    messageLock.release();

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

}
