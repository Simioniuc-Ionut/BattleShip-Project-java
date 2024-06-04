package firstFrame;

import mainMenu.MainFrameTwo;
import leaderboard.LeaderboardFrame;
import org.example.GameClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.Socket;

import org.example.connection.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsUser extends JPanel {
    final MainFrameOne frame;
    GameClient client;

    JLabel title;
    JLabel username;

    JTextField writeUsername = new JTextField(20);
    JButton startGameBtn = new JButton("Start");

    public SettingsUser(MainFrameOne frame, GameClient client) {
        this.frame = frame;
        this.client = client;
        init();
    }

    public void init() {
        // pozitie verticala
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel("Start Battleship Game");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        // spatiu
        add(Box.createRigidArea(new Dimension(0, 20)));

        username = new JLabel("Enter your Username :");
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(username);
        add(Box.createRigidArea(new Dimension(0, 5)));

        // Panel pentru centrul textfield
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        writeUsername.setPreferredSize(new Dimension(200, 30));
        writeUsername.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        usernamePanel.add(writeUsername);
        usernamePanel.setPreferredSize(new Dimension(300, 40));

        // usernamePanel.setMaximumSize(new Dimension(250, 40)); // dimensiunea
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(usernamePanel);


        add(Box.createRigidArea(new Dimension(0, 20)));

        startGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(startGameBtn);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // box filler pt spatiere sub
        add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

        title.setFont(new Font("default", Font.BOLD, 25));
        writeUsername.setFont(new Font("default", Font.BOLD, 15));

        Font newFont = new Font("default", Font.BOLD, 20);
        username.setFont(newFont);

        startGameBtn.setFont(newFont);
        startGameBtn.setPreferredSize(new Dimension(250, 75));
        startGameBtn.setBackground(Color.darkGray);
        startGameBtn.setForeground(Color.WHITE); // culoare text

        // configure listeners for all buttons
        startGameBtn.addActionListener(this::listenerAddStartGameBtn);

    }

    private void listenerAddStartGameBtn(ActionEvent e) {

        String username = writeUsername.getText();
        frame.client.setPlayerUsername(username);

        try {
            // Adaug username-ul în BD
            addUsernameInDB();
            // Iau ID-ul unic din BD, corespunzator numelui introdus
            takeUniqIDFromDB();
            // Adaug teamId în BD
            addTeamId();

            //daca operatiile au reusit, deschid fereastra
            new MainFrameTwo(client).setVisible(true);
            frame.setVisible(false);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
        }
    }

    public void addUsernameInDB() {
        System.out.println("Write username is " + writeUsername.getText());
        String jsonInputString = "{\"playerName\":\"" + writeUsername.getText() + "\"}";

        try {
            String response = HttpClient.sendPostRequest("http://localhost:8080/api/players/add", jsonInputString);
            JOptionPane.showMessageDialog(frame, "Response: " + response);
            //iau id ul unic din bd , corespunzator numelui introdus

        } catch (Exception ex) {

            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Player already exist ");
        }
    }

    public void takeUniqIDFromDB() {
        int idFromDB = 0;
        try {
            idFromDB = HttpClient.getPlayerId(writeUsername.getText());
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(frame, "Player dosent exist in db  " + ex.getMessage());
        }
        frame.client.setPlayerIDFromDB(idFromDB);
        // System.out.println("Id from DB " +idFromDB);

    }

    public void addTeamId() {
        Integer playerTeamId = frame.client.getPlayerTeamId();
        Integer playerDBId = frame.client.getPlayerIDFromDB();


        // Construim un obiect JSON care conține playerId și teamId
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("playerId", playerDBId);
            jsonObject.put("playerTeamId", playerTeamId);
        } catch (JSONException ex) {
            System.out.println("Erro to jsonObject put values " + ex.getMessage());
            throw new RuntimeException(ex);
        }


        // Convertim obiectul JSON într-un șir de caractere JSON
        String jsonInputString1 = jsonObject.toString();

        // Construim URL-ul pentru a trimite cererea
        String urlString = "http://localhost:8080/api/players/addTeamId/" + playerDBId + "/" + playerTeamId;

        // Trimitem cererea către server folosind metoda sendPostRequest
        try {
            String response = HttpClient.sendPostRequest(urlString, jsonInputString1);
            System.out.println("Response: " + response);
        } catch (Exception ex) {
            ex.printStackTrace();

            System.out.println("Failed to send request: " + ex.getMessage());
        }
    }
}
