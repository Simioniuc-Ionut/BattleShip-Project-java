package org.example;

public class Main {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Adresa serverului
        int serverPort = 12345; // Portul serverului

        GameClient client = new GameClient(serverAddress, serverPort);
        client.start();
    }
}
