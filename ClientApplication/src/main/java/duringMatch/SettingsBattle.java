package duringMatch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsBattle extends JPanel {
    final MainFrameFour frame;
    JLabel serverResponse;
    JPanel messagePanelBattle = new JPanel(new BorderLayout()); // pentru a afisare mesaje
    public JTextArea messageTextAreaBattle;

    public SettingsBattle(MainFrameFour frame) {
        this.frame = frame;
        init();
    }

    private void init() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Dimension preferredSize = new Dimension(290, 220);
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setMaximumSize(preferredSize);

        messageFromServer();

    }


    public void messageFromServer() {
        // Title pt JPanel
        serverResponse = new JLabel("Message : ");
        Font newFont = new Font("default", Font.BOLD, 16);
        serverResponse.setFont(newFont);

        JPanel serverResponsePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        serverResponsePanel.add(serverResponse);

        add(Box.createVerticalStrut(10));
        add(serverResponsePanel);
        add(Box.createVerticalStrut(10));

        // JTextArea pt afisarea mesajelor in JPanel
        messageTextAreaBattle = new JTextArea();
        messageTextAreaBattle.setWrapStyleWord(true);
        messageTextAreaBattle.setLineWrap(true);
        messageTextAreaBattle.setEditable(false);
        messageTextAreaBattle.setForeground(Color.WHITE);
        messageTextAreaBattle.setBackground(new Color(0, 0, 0, 123));
        messageTextAreaBattle.setBorder(new EmptyBorder(10, 10, 10, 10));
//        String text = "Response from Server";
//        messageTextAreaBattle.setText(text);

        // Add JTextArea in JPanel
        messagePanelBattle.add(messageTextAreaBattle, BorderLayout.CENTER);

        add(messagePanelBattle);
        add(Box.createVerticalStrut(10));
    }


}
