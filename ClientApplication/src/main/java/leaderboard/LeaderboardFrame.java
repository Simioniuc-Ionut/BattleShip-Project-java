package leaderboard;

import mainMenu.MainFrameTwo;
import org.example.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class LeaderboardFrame extends JFrame {
    JButton leaderboard = new JButton("Leaderboard");
    JButton history = new JButton("Match History");
    JButton statistic = new JButton("View Statistic");
    JButton back = new JButton("Back");
    JLabel title;
    GameClient client;

    public LeaderboardFrame(GameClient client) {

        this.client = client;
        init();
    }

    public void init() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        title = new JLabel("Scoreboards");
        title.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrare
        title.setFont(new Font("default", Font.BOLD, 20));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        initButton(leaderboard, panel);
        initButton(history, panel);
        initButton(statistic, panel);
        initButton(back, panel);


        leaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LeaderboardFrame.this.setVisible(false);
                new PlayerTableFrame(client).setVisible(true);
            }
        });
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LeaderboardFrame.this.setVisible(false);
                new MatchHistoryFrame(client).setVisible(true);
            }
        });
        statistic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LeaderboardFrame.this.setVisible(false);
                new ViewStatisticFrame(client).setVisible(true);
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LeaderboardFrame.this.setVisible(false);
                new MainFrameTwo(client).setVisible(true);
            }
        });


        this.add(panel);
        this.pack();
        this.setVisible(true);
        setLocationRelativeTo(null);


    }

    private void initButton(JButton button, JPanel panel) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrare
        button.setFont(new Font("default", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(250, 70));
        button.setBackground(Color.darkGray);
        button.setForeground(Color.WHITE); // culoare text
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }


}

