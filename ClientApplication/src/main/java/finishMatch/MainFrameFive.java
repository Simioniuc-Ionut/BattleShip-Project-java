package finishMatch;

import org.example.GameClient;

import javax.swing.*;
import java.awt.*;

public class MainFrameFive extends JFrame {
    public GameClient client;
    SettingsGameOver settingsGameOver;

    public MainFrameFive(GameClient client, String msg) {
        super("gameOver");
        this.client = client;
        initGameOver(msg);
    }

    private void initGameOver(String msg) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

        settingsGameOver = new SettingsGameOver(this, client, msg);

        add(settingsGameOver, BorderLayout.CENTER);

        settingsGameOver.updatePlayerInfoLabel();

        pack();
        setSize(new Dimension(600, 200));
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
