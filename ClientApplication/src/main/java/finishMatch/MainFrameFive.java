package finishMatch;

import org.example.GameClient;

import javax.swing.*;
import java.awt.*;

public class MainFrameFive extends JFrame {
    public GameClient client;
    SettingsGameOver settingsGameOver;
    public MainFrameFive(GameClient client) {
        super("gameOver");
        this.client = client;
        initGameOver();
    }

    private void initGameOver() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

        settingsGameOver = new SettingsGameOver(this,client);

        add(settingsGameOver, BorderLayout.CENTER);

        pack();
        setSize(new Dimension(600, 200));
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
