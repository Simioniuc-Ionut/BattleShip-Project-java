package leaderboard;

import com.example.demo_battleship.model.Player;
import firstFrame.MainFrameOne;
import org.example.GameClient;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LeaderboardFrame extends JFrame {
    private final DefaultTableModel tableModel;
   GameClient client;

    public LeaderboardFrame(GameClient client) {

        this.client = client;

        setTitle("Leaderboard");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Username");
        tableModel.addColumn("Score");
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        updateTableWithPlayers();
        setLocationRelativeTo(null);
        //  butonului Back
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LeaderboardFrame.this.setVisible(false);
                new MainFrameOne(client).setVisible(true);
            }
        });


        add(backButton, BorderLayout.SOUTH);
    }

    private void updateTableWithPlayers() {
        List<Player> players = HttpClient.getPlayersList();
        for (Player player : players) {
            tableModel.addRow(new Object[]{player.getPlayerName(), player.getWinsCount()});
        }
    }



}

