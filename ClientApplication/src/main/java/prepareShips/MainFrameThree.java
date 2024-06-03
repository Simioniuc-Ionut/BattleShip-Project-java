package prepareShips;

import firstFrame.SettingsUser;
import org.example.GameClient;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;


public class MainFrameThree extends JFrame {
    public SettingsPlaceShip settingsPlaceShip;
    ControlPanelBottom controlPanelBottom;
    ClientBoard clientBoard;

    public GameClient client;

    public MainFrameThree(GameClient client) {
        super("prepareShips");
        this.client = client;
        initPrepareShips();
    }

    private void initPrepareShips() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

        //componente pentru fereastra de plasare navegames
        settingsPlaceShip = new SettingsPlaceShip(this);
        clientBoard = new ClientBoard(this);
        controlPanelBottom = new ControlPanelBottom(this, settingsPlaceShip, clientBoard);

        //adaugare componente pe anumiote pozitii ale ferestrei
        add(settingsPlaceShip, BorderLayout.EAST);
        add(controlPanelBottom, BorderLayout.SOUTH);
        add(clientBoard, BorderLayout.CENTER);

        // Setarea teamId și username
        clientBoard.updatePlayerInfoLabel();

        pack();
        setSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setVisible(false);

    }


}
