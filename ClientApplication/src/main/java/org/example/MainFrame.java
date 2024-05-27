package org.example;
import PrepareShips.ClientBoard;
import PrepareShips.ControlPanelBottom;
import PrepareShips.SettingsPlaceShip;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public SettingsPlaceShip settingsPlaceShip;
    ControlPanelBottom controlPanelBottom;
    ClientBoard clientBoard;

    public MainFrame() {
        super("PrepareShips");
        init();
    }

    private void init() {
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
        setVisible(true);
    }


}
