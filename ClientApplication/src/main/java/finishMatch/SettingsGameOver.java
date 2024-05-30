package finishMatch;

import org.example.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SettingsGameOver extends JPanel {
    final MainFrameFinish frame;
    GameClient client;

    JLabel title;
    JButton exitBtn = new JButton("Exit");
    JButton playAgainBtn = new JButton("Play Again");

    public SettingsGameOver(MainFrameFinish frame,GameClient client) { // Modificați constructorul pentru a include ClientBoard
        this.frame = frame;
        this.client = client;
        init();
    }

    public void init(){
        // pozitie verticala
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel("Game Over");
        title.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrare
        add(title);

        // spatiu
        add(Box.createRigidArea(new Dimension(0, 20)));

        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrare
        add(exitBtn);

        // spatiu
        add(Box.createRigidArea(new Dimension(0, 20)));

        playAgainBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrare
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
        playAgainBtn.setForeground(Color.WHITE);//culoare text

        //configure listeners for all buttons
        exitBtn.addActionListener(this::listenerAddExitBtn);
        playAgainBtn.addActionListener(this::listenerAddPlayAgainBtn);
    }

    private void listenerAddExitBtn(ActionEvent e) {

//        String messageToClient = "exit";
//        frame.client.setAnswer(messageToClient);
//        frame.setVisible(false);//inchide fereastra

    }
    private void listenerAddPlayAgainBtn(ActionEvent e) {

//        String messageToClient = "";
//        frame.client.setAnswer(messageToClient);
//        new MainFrameOne(client).setVisible(true); //apare pagina de inceput
//        frame.setVisible(false);//inchide fereastra

    }
}
