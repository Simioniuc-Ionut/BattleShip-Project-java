package createOrJoinGame;

import org.example.GameClient;

import javax.swing.*;
import java.awt.*;

public class MainFrameOne extends JFrame {
    public GameClient client;
    Settings settingsCreate;
    public MainFrameOne(GameClient client) {
        super("createOrJoinGame");
        this.client = client;
        initCreateGame();
    }

    private void initCreateGame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

        settingsCreate = new Settings(this,client);

        add(settingsCreate, BorderLayout.CENTER);

        pack();
        setSize(new Dimension(600, 200));
        setLocationRelativeTo(null);
        setVisible(true); // Arată fereastra cu butonul
    }


}
