package firstFrame;

import org.example.GameClient;

import javax.swing.*;
import java.awt.*;

public class MainFrameOne extends JFrame {
    public GameClient client;
    SettingsUser settingsUser;
    public MainFrameOne(GameClient client) {
        super("User");
        this.client=client;
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
