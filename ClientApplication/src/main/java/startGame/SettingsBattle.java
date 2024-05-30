package startGame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsBattle extends JPanel {
    final MainFrameBattle frame;
    JLabel serverResponse;
    JPanel messagePanelBattle = new JPanel(new BorderLayout()); // for displaying server message
    JTextArea messageTextAreaBattle;

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
        messageTextAreaBattle = new JTextArea();
        messageTextAreaBattle.setWrapStyleWord(true);
        messageTextAreaBattle.setLineWrap(true);
        messageTextAreaBattle.setEditable(false);
        messageTextAreaBattle.setForeground(Color.WHITE);
        messageTextAreaBattle.setBackground(new Color(0, 0, 0, 123));
        messageTextAreaBattle.setBorder(new EmptyBorder(10, 10, 10, 10));
        String text = "Response from Server";
        messageTextAreaBattle.setText(text);

        // Add JTextArea to JPanel
        messagePanelBattle.add(messageTextAreaBattle, BorderLayout.CENTER);

        // Add message panel to the main panel
        add(messagePanelBattle);
        add(Box.createVerticalStrut(10));
    }
}
