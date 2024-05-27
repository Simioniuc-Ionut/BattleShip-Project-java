package prepareShips;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.concurrent.Semaphore;

public class SettingsPlaceShip extends JPanel {
    final MainFrame frame;
    JLabel placeShipFrom;
    JLabel placeShipTo;
    JLabel nameShip;
    JLabel sizeShip;
    JLabel textNameShip;
    JLabel textSizeShip;
    JSpinner fromRow;
    JSpinner fromColumn;
    JSpinner toRow;
    JSpinner toColumn;
    JLabel serverResponse;
    JPanel messagePanel = new JPanel(new BorderLayout());
    // afisarea mesajului de la server dupa apasarea butonului
    JTextArea messageTextArea;

    public SettingsPlaceShip(MainFrame frame) {
        this.frame = frame;
        init();
    }
    private void init() {

        nameShip = new JLabel("Name Ship:");
        textNameShip = new JLabel("Carrier");

        sizeShip = new JLabel("Size Ship:");
        textSizeShip = new JLabel("5");
        //actualizare folosind  folosind metoda setText()?

        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        placeShipFrom = new JLabel("From:");
        fromRow= new JSpinner(new SpinnerListModel(letters));
        fromColumn = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        placeShipTo = new JLabel("To:");
        toRow = new JSpinner(new SpinnerListModel(letters));
        toColumn = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        // Creare un nou font
        Font newFont = new Font("default", Font.BOLD, 25);

        //variabila cu date ex :A1 A2 A3 .... " STring
        //VARIBLA o trimiti la server ,
        // Setare font pentru JLabel
        placeShipFrom.setFont(newFont);
        placeShipTo.setFont(newFont);
        nameShip.setFont(newFont);
        sizeShip.setFont(newFont);

        // Setare font pentru JSpinner
        fromRow.setFont(newFont);
        fromColumn.setFont(newFont);
        toRow.setFont(newFont);
        toColumn.setFont(newFont);

        textSizeShip.setFont(newFont);
        textNameShip.setFont(newFont);

        serverResponse = new JLabel("Message : ");
        serverResponse.setFont(newFont);

        // Creare panouri pentru a grupa labelul și spinerele
        JPanel fromPanel = new JPanel();
        fromPanel.setLayout(new FlowLayout());
        fromPanel.add(placeShipFrom);
        fromPanel.add(fromRow);
        fromPanel.add(fromColumn);
        fromPanel.setBorder(new EmptyBorder(0, 0, 0, 100));

        JPanel toPanel = new JPanel();
        toPanel.setLayout(new FlowLayout());
        toPanel.add(placeShipTo);
        toPanel.add(toRow);
        toPanel.add(toColumn);
        toPanel.setBorder(new EmptyBorder(0,0,0,100));

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(nameShip);
        namePanel.add(textNameShip);
        namePanel.setBorder(new EmptyBorder(0,0,0,100));

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new FlowLayout());
        sizePanel.add(sizeShip);
        sizePanel.add(textSizeShip);
        sizePanel.setBorder(new EmptyBorder(0,0,0,100));

        // Create a new panel to hold the serverResponse label and add padding to it
        JPanel serverResponsePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        serverResponsePanel.add(serverResponse);
        serverResponsePanel.setBorder(new EmptyBorder(0, 0, 0, 100)); // Add padding to the left


        // Setare layout pentru acest JPanel ca BoxLayout pe verticală
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Adăugare panouri la acest JPanel
        add(Box.createVerticalStrut(20));
        add(namePanel);
        add(Box.createVerticalStrut(4)); // Adăugați un strut vertical între panouri
        add(sizePanel);
        add(Box.createVerticalStrut(40)); // Adăugați un strut vertical între panouri
        add(fromPanel);
        add(Box.createVerticalStrut(4)); // Adăugați un strut vertical între panouri
        add(toPanel);
        add(Box.createVerticalStrut(80));
        add(serverResponsePanel);
        add(Box.createVerticalStrut(4));


        //mesaj de la server
        messagePanel.setBorder(new EmptyBorder(0,0,0,100));// nu vreau ca jpanelul sa fie lipit de margine dar nu functioneaza

// Crearea unui nou JTextArea
        messageTextArea = new JTextArea();
        messageTextArea.setWrapStyleWord(true); //la sfarsitul cuv daca e nevoie trece la randul urm
        messageTextArea.setLineWrap(true); //cand ajunge la capat trece la urm rand
        messageTextArea.setEditable(false); //nu poate fi modificat
        messageTextArea.setForeground(Color.WHITE);
        messageTextArea.setBackground(new Color(0, 0, 0, 123));
        messageTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

// Adăugați textul primit de la server
        String text = "Response Serverdrhgrdhbbbfd bfhdtht dgnhxjyjyymjhxj ymgmgymtgynmgjmyjmgy";
        messageTextArea.setText(text);

// Adăugarea JTextArea la JPanel
        messagePanel.add(messageTextArea, BorderLayout.CENTER);


// Adăugarea JPanel la JFrame
        add(messagePanel);
        add(Box.createVerticalStrut(70));


    }


}