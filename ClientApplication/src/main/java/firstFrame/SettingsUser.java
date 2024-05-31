package firstFrame;

import createOrJoinGame.MainFrameTwo;
import org.example.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SettingsUser extends JPanel {
    final MainFrameOne frame;
    GameClient client;

    JLabel title;
    JLabel username;

    JTextField writeUsername = new JTextField(20);
    JButton startGameBtn = new JButton("Start");
    JButton viewScoresBtn = new JButton("Check the leaderboard");

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
        usernamePanel.add(writeUsername);
        usernamePanel.setMaximumSize(new Dimension(250, 40)); // dimensiunea
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(usernamePanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        startGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(startGameBtn);
        add(Box.createRigidArea(new Dimension(0, 20)));

        viewScoresBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(viewScoresBtn);

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

        viewScoresBtn.setFont(newFont);
        viewScoresBtn.setPreferredSize(new Dimension(250, 75));
        viewScoresBtn.setBackground(Color.darkGray);
        viewScoresBtn.setForeground(Color.WHITE); // culoare text

        // listeners
        startGameBtn.addActionListener(this::listenerAddStartGameBtn);
        viewScoresBtn.addActionListener(this::listenerAddViewTableBtn);
    }

    private void listenerAddStartGameBtn(ActionEvent e) {
        new MainFrameTwo(client).setVisible(true); // apare urmatoarea fereastra
        frame.setVisible(false); // inchide fereastra
    }

    private void listenerAddViewTableBtn(ActionEvent e) {
        // new MainFrame(client).setVisible(true); // apare urmatoarea fereastra
        // frame.setVisible(false); // inchide fereastra
    }
}
