package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private final String serverAddress;
    private final int serverPort;

    public GameClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
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

                String userInputLine;
                while ((userInputLine = userInput.readLine()) != null) {
                    out.println(userInputLine);

                    if (userInputLine.equalsIgnoreCase("exit")) {
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

    private Thread createServerListenerThread(BufferedReader in) {
        return new Thread(() -> {
            try {
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println("Server response: " + serverResponse);
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
