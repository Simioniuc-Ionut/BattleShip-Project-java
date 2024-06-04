package firstFrame;

import org.example.GameClient;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class MainFrameOne extends JFrame {
    public GameClient client;
    public Socket socketTimer;
    SettingsUser settingsUser;

    public MainFrameOne(GameClient client, Socket socketTimer) {
        super("User");
        this.client = client;
        this.socketTimer = socketTimer;
        initUser();
    }

    private void initUser() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

        settingsUser = new SettingsUser(this, client);

        add(settingsUser, BorderLayout.CENTER);

        pack();
        setSize(new Dimension(500, 250));
        setLocationRelativeTo(null);
        setVisible(true); // Arată fereastra cu butonul
    }


}
