package prepareShips;

import javax.swing.*;
import java.awt.*;

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
    JPanel messagePanel = new JPanel(new BorderLayout());
    // afisarea mesajului de la server dupa apasarea butonului
    JLabel messageLabel = new JLabel();

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

        // Creare panouri pentru a grupa labelul și spinerele
        JPanel fromPanel = new JPanel();
        fromPanel.setLayout(new FlowLayout());
        fromPanel.add(placeShipFrom);
        fromPanel.add(fromRow);
        fromPanel.add(fromColumn);

        JPanel toPanel = new JPanel();
        toPanel.setLayout(new FlowLayout());
        toPanel.add(placeShipTo);
        toPanel.add(toRow);
        toPanel.add(toColumn);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(nameShip);
        namePanel.add(textNameShip);

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new FlowLayout());
        sizePanel.add(sizeShip);
        sizePanel.add(textSizeShip);

        // Setare layout pentru acest JPanel ca BoxLayout pe verticală
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Adăugare panouri la acest JPanel
        add(namePanel);
        add(Box.createVerticalStrut(0)); // Adăugați un strut vertical între panouri
        add(sizePanel);
        add(Box.createVerticalStrut(0)); // Adăugați un strut vertical între panouri
        add(fromPanel);
        add(Box.createVerticalStrut(0)); // Adăugați un strut vertical între panouri
        add(toPanel);

        //mesaj de la server
        messagePanel.setBackground(new Color(0, 0, 0, 123));//setare backgorund
        messageLabel.setForeground(Color.WHITE); // Setează culoarea textului la alb
        messageLabel.setHorizontalAlignment(JLabel.CENTER); // Aliniază textul la centru
//        messagePanel.setPreferredSize(new Dimension(messagePanel.getPreferredSize().width, 10)); //nu merge


        // Adăugarea JLabel la JPanel
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        // Adăugarea JPanel la JFrame
        add(messagePanel);


//mesaj de la server
        messagePanel.setBackground(new Color(0, 0, 0, 123));//setare backgorund
        messageLabel.setForeground(Color.WHITE); // Setează culoarea textului la alb
        messageLabel.setHorizontalAlignment(JLabel.CENTER); // Aliniază textul la centru
//        messagePanel.setPreferredSize(new Dimension(messagePanel.getPreferredSize().width, 10)); //nu merge


        // Adăugarea JLabel la JPanel
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        // Adăugarea JPanel la JFrame
        add(messagePanel);
    }
}