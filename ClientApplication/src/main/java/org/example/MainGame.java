package org.example;

public class MainGame {
    public static void main(String[] args) {
       // new MainFrame().setVisible(true);
        GameClient client = new GameClient("localhost",12345,12346);
        client.start();
    }
}
