package finishMatch;

import createOrJoinGame.MainFrameTwo;
import org.example.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SettingsGameOver extends JPanel {
    final MainFrameFive frame;
    GameClient client;

    JLabel title;
    JButton exitBtn = new JButton("Exit");
    JButton playAgainBtn = new JButton("Play Again");

    public SettingsGameOver(MainFrameFive frame, GameClient client) {
        this.frame = frame;
        this.client = client;
        init();
    }

    public void init(){
        // pozitie verticala
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel("Game Over");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createRigidArea(new Dimension(0, 20)));

        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(exitBtn);

        add(Box.createRigidArea(new Dimension(0, 20)));

        playAgainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(playAgainBtn);

        // box filler pt spatiere sub
        add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

        Font fontTitle = new Font("default", Font.BOLD, 25);
        title.setFont(fontTitle);

        Font newFont = new Font("default", Font.BOLD, 20);

        exitBtn.setFont(newFont);
        exitBtn.setPreferredSize(new Dimension(250, 70));
        exitBtn.setBackground(Color.darkGray);
        exitBtn.setForeground(Color.WHITE);//culoare text

        playAgainBtn.setFont(newFont);
        playAgainBtn.setPreferredSize(new Dimension(250, 70));
        playAgainBtn.setBackground(Color.darkGray);
        playAgainBtn.setForeground(Color.WHITE);

        //listeners
        exitBtn.addActionListener(this::listenerAddExitBtn);
        playAgainBtn.addActionListener(this::listenerAddPlayAgainBtn);
    }

    private void listenerAddExitBtn(ActionEvent e) {

        String messageToClient = "exit";
        frame.client.setAnswer(messageToClient);
        frame.setVisible(false);//inchide fereastra

    }
    private void listenerAddPlayAgainBtn(ActionEvent e) {

//        String messageToClient = "again";
//        frame.client.setAnswer(messageToClient);
        new MainFrameTwo(client).setVisible(true); //apare pagina de inceput
        frame.setVisible(false);//inchide fereastra

    }
}
