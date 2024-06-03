package prepareShips;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.Socket;

public class SettingsPlaceShip extends JPanel {
    final MainFrameThree frame;
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
    JPanel messagePanel = new JPanel(new BorderLayout());// afisarea mesajului de la server dupa apasarea butonului
    JTextArea messageTextArea;

    public SettingsPlaceShip(MainFrameThree frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        //Ship name+size
        setNameAndSizeShip();

        //Place ship from -> to
        setFromShip();
        setToShip();

        //Message from Server
        setMessageFromServer();
    }

    public void setNameAndSizeShip() {
        nameShip = new JLabel("Name Ship:");
        textNameShip = new JLabel("Carrier");

        sizeShip = new JLabel("Size Ship:");
        textSizeShip = new JLabel("5");

        Font newFont = new Font("default", Font.BOLD, 25);
        nameShip.setFont(newFont);
        sizeShip.setFont(newFont);
        textSizeShip.setFont(newFont);
        textNameShip.setFont(newFont);

        //grupare
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(nameShip);
        namePanel.add(textNameShip);
        namePanel.setBorder(new EmptyBorder(0, 0, 0, 100));

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new FlowLayout());
        sizePanel.add(sizeShip);
        sizePanel.add(textSizeShip);
        sizePanel.setBorder(new EmptyBorder(0, 0, 0, 100));

        //add
        add(Box.createVerticalStrut(20));
        add(namePanel);
        add(Box.createVerticalStrut(4)); //spatiere verticala intre
        add(sizePanel);
        add(Box.createVerticalStrut(40));

    }

    public void setFromShip() {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        placeShipFrom = new JLabel("From:");
        fromRow = new JSpinner(new SpinnerListModel(letters));
        fromColumn = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        Font newFont = new Font("default", Font.BOLD, 25);
        placeShipFrom.setFont(newFont);
        fromRow.setFont(newFont);
        fromColumn.setFont(newFont);

        JPanel fromPanel = new JPanel();
        fromPanel.setLayout(new FlowLayout());
        fromPanel.add(placeShipFrom);
        fromPanel.add(fromRow);
        fromPanel.add(fromColumn);
        fromPanel.setBorder(new EmptyBorder(0, 0, 0, 90));

        add(fromPanel);
        add(Box.createVerticalStrut(4));

    }

    public void setToShip() {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        placeShipTo = new JLabel("To:");
        toRow = new JSpinner(new SpinnerListModel(letters));
        toColumn = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        Font newFont = new Font("default", Font.BOLD, 25);
        placeShipTo.setFont(newFont);
        toRow.setFont(newFont);
        toColumn.setFont(newFont);

        JPanel toPanel = new JPanel();
        toPanel.setLayout(new FlowLayout());
        toPanel.add(placeShipTo);
        toPanel.add(toRow);
        toPanel.add(toColumn);
        toPanel.setBorder(new EmptyBorder(0, 0, 0, 90));

        add(toPanel);
        add(Box.createVerticalStrut(80));

    }

    public void setMessageFromServer() {
        // Title for JPanel
        serverResponse = new JLabel("Message : ");

        Font newFont = new Font("default", Font.BOLD, 25);
        serverResponse.setFont(newFont);

        JPanel serverResponsePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        serverResponsePanel.add(serverResponse);

        add(serverResponsePanel);
        add(Box.createVerticalStrut(4));

        // JPanel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        messagePanel.setBorder(new EmptyBorder(0, 0, 0, 100));

        messageTextArea = new JTextArea();
        messageTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        messageTextArea.setLineWrap(true); // Wrap lines at the edge of the JTextArea
        messageTextArea.setEditable(false); // Non-editable
        messageTextArea.setForeground(Color.WHITE);
        messageTextArea.setBackground(new Color(0, 0, 0, 123));
        messageTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        //TextArea
        messageTextArea.setPreferredSize(new Dimension(300, 100));
        messageTextArea.setMinimumSize(new Dimension(300, 100));
        messageTextArea.setMaximumSize(new Dimension(300, 200));

        // messageTextArea
        JScrollPane scrollPane = new JScrollPane(messageTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //JScrollPane
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setMinimumSize(new Dimension(300, 100));
        scrollPane.setMaximumSize(new Dimension(300, 200));

        messagePanel.add(scrollPane, BorderLayout.CENTER);

        add(messagePanel);
        add(Box.createVerticalStrut(70));
    }


}