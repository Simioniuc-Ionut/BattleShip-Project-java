package firstFrame;

import org.example.GameClient;

import javax.swing.*;
import java.awt.*;

public class MainFramePlay extends JFrame {
    public GameClient client;
    SettingsUser settingsUser;
    public MainFramePlay() {
        super("User");

        initUser();
    }

    private void initUser() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

        settingsUser = new SettingsUser(this,client);

        add(settingsUser, BorderLayout.CENTER);

        pack();
        setSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
        setVisible(true); // Arată fereastra cu butonul
    }


}
