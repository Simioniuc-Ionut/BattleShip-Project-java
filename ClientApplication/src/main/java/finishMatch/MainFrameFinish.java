package finishMatch;

import org.example.GameClient;

import javax.swing.*;
import java.awt.*;

public class MainFrameFinish extends JFrame {
    public GameClient client;
    SettingsGameOver settingsGameOver;
    public MainFrameFinish(GameClient client) {
        super("createOrJoinGame");
        this.client = client;
        initCreateGame();
    }

    private void initCreateGame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

        settingsGameOver = new SettingsGameOver(this,client);

        add(settingsGameOver, BorderLayout.CENTER);

        pack();
        setSize(new Dimension(600, 200));
        setLocationRelativeTo(null);
        setVisible(true); // Arată fereastra cu butonul
    }


}
