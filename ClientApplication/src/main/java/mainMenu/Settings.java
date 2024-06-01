package mainMenu;

import org.example.GameClient;
import prepareShips.MainFrameThree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Settings extends JPanel {
    final MainFrameTwo frame;
    GameClient client;

    JLabel title;
    JButton createGameBtn = new JButton("Create Game");
    JButton joinGameBtn = new JButton("Join Game");

    public Settings(MainFrameTwo frame, GameClient client) {
        this.frame = frame;
        this.client = client;
        init();
    }

    public void init(){
        // pozitie verticala
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel("Battleship Game");
        title.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrare
        add(title);

        // spatiu
        add(Box.createRigidArea(new Dimension(0, 20)));

        createGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrare
        add(createGameBtn);


        add(Box.createRigidArea(new Dimension(0, 20)));

        joinGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(joinGameBtn);

        // box filler pt spatiere sub
        add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

        Font fontTitle = new Font("default", Font.BOLD, 25);
        title.setFont(fontTitle);

        Font newFont = new Font("default", Font.BOLD, 20);

        createGameBtn.setFont(newFont);
        createGameBtn.setPreferredSize(new Dimension(250, 70));
        createGameBtn.setBackground(Color.darkGray);
        createGameBtn.setForeground(Color.WHITE);//culoare text

        joinGameBtn.setFont(newFont);
        joinGameBtn.setPreferredSize(new Dimension(250, 70));
        joinGameBtn.setBackground(Color.darkGray);
        joinGameBtn.setForeground(Color.WHITE);

        //listeners
        createGameBtn.addActionListener(this::listenerAddCreateGameBtn);
        joinGameBtn.addActionListener(this::listenerAddJoinGameBtn);
    }

    private void listenerAddCreateGameBtn(ActionEvent e) {

        String messageToClient = "c";
        frame.client.setAnswer(messageToClient);
        new MainFrameThree(client).setVisible(true); //apare urmatoarea fereastra
        frame.setVisible(false);//inchide fereastra

    }
    private void listenerAddJoinGameBtn(ActionEvent e) {

        String messageToClient = "j";
        frame.client.setAnswer(messageToClient);
        new MainFrameThree(client).setVisible(true); //apare urmatoarea fereastra
        frame.setVisible(false);//inchide fereastra

    }
}
