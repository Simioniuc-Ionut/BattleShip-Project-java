package startGame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsBattle extends JPanel {
    final MainFrameBattle frame;
    JLabel serverResponse;
    JPanel messagePanel = new JPanel(new BorderLayout()); // for displaying server message
    JTextArea messageTextArea;

    public SettingsBattle(MainFrameBattle frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        // Configure the layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Set preferred size to make it a small rectangle
        Dimension preferredSize = new Dimension(290, 120);
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setMaximumSize(preferredSize);

        // Message from Server
        messageFromServer();
    }


    public void messageFromServer() {
        // Title for JPanel
        serverResponse = new JLabel("Message : ");
        Font newFont = new Font("default", Font.BOLD, 16);
        serverResponse.setFont(newFont);

        JPanel serverResponsePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        serverResponsePanel.add(serverResponse);

        // Add vertical space and server response panel
        add(Box.createVerticalStrut(10));
        add(serverResponsePanel);
        add(Box.createVerticalStrut(10));

        // JTextArea for displaying message from server
        messageTextArea = new JTextArea();
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setLineWrap(true);
        messageTextArea.setEditable(false);
        messageTextArea.setForeground(Color.WHITE);
        messageTextArea.setBackground(new Color(0, 0, 0, 123));
        messageTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        messageTextArea.setText("Response from Server");

        // Add JTextArea to JPanel
        messagePanel.add(messageTextArea, BorderLayout.CENTER);

        // Add message panel to the main panel
        add(messagePanel);
        add(Box.createVerticalStrut(10));
    }
}
