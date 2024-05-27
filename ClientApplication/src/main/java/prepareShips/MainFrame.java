package prepareShips;
import org.example.GameClient;
import prepareShips.ClientBoard;
import prepareShips.ControlPanelBottom;
import prepareShips.SettingsPlaceShip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    public SettingsPlaceShip settingsPlaceShip;
    ControlPanelBottom controlPanelBottom;
    ClientBoard clientBoard;
    public GameClient client;

    public MainFrame(GameClient client) {
        super("prepareShips");
        this.client = client;
        initPrepareShips();
    }

    private void initPrepareShips() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

        //componente pentru fereastra de plasare nave
        settingsPlaceShip = new SettingsPlaceShip(this);
        clientBoard = new ClientBoard(this);
        controlPanelBottom = new ControlPanelBottom(this, settingsPlaceShip,clientBoard);

        //adaugare componente pe anumiote pozitii ale ferestrei
        add(settingsPlaceShip, BorderLayout.EAST);
        add(controlPanelBottom, BorderLayout.SOUTH);
        add(clientBoard, BorderLayout.CENTER);


        //setari vizualizare fereastra
        pack(); // Ajustează dimensiunea ferestrei la dimensiunea preferată a componentelor sale
        setSize(new Dimension(1000, 700));
        setVisible(false);
    }


}
