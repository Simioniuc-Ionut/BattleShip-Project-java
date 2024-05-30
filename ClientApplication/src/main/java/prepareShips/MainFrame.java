package prepareShips;
import org.example.GameClient;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {
    public SettingsPlaceShip settingsPlaceShip;
    ControlPanelBottom controlPanelBottom;
    ClientBoard clientBoard;

    public GameClient client;
    private ImageIcon img;
    private JLabel background;

    public MainFrame(GameClient client) {
        super("prepareShips");
        this.client = client;
        initPrepareShips();
    }

    private void initPrepareShips() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);//inchidere fereastra

//        img = new ImageIcon(this.getClass().getResource("/battleship.jpg"));
//        background = new JLabel(img);
//        background.setSize(800,500);
//        add(background);

        //componente pentru fereastra de plasare nave
        settingsPlaceShip = new SettingsPlaceShip(this);
        clientBoard = new ClientBoard(this);
        controlPanelBottom = new ControlPanelBottom(this, settingsPlaceShip,clientBoard);

        //adaugare componente pe anumiote pozitii ale ferestrei
        add(settingsPlaceShip, BorderLayout.EAST);
        add(controlPanelBottom, BorderLayout.SOUTH);
        add(clientBoard, BorderLayout.CENTER);

        //fundal

        //setari vizualizare fereastra
        pack(); // ajusteaza dimensiunea ferestrei la dimensiunea preferată a componentelor sale
        setSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setVisible(false);

    }

}
