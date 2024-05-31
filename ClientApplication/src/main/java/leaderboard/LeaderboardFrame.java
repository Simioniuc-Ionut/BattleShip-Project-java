package leaderboard;

import com.example.demo_battleship.model.Player;
import firstFrame.MainFrameOne;
import org.example.GameClient;
import org.example.connection.HttpClient;

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
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        updateTableWithPlayers();
        setLocationRelativeTo(null);
        //  butonului Back
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.darkGray);
        backButton.setForeground(Color.WHITE);
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
        // Adăugarea tuturor coloanelor necesare în modelul tabelului
        tableModel.addColumn("Player ID");
        tableModel.addColumn("Player Name");
        tableModel.addColumn("Hits Count");
        tableModel.addColumn("Misses Count");
        tableModel.addColumn("Wins Count");
        tableModel.addColumn("Losses Count");
        tableModel.addColumn("Matches Count");
        tableModel.addColumn("Player Team Id");

        // Obținerea listei de jucători
            List<Player> players = (List<Player>) HttpClient.getPlayersList();
        for (Player player : players) {
            // Adăugarea unui rând nou în tabel pentru fiecare jucător
            tableModel.addRow(new Object[]{
                    player.getPlayerId(),
                    player.getPlayerName(),
                    player.getHitsCount(),
                    player.getMissesCount(),
                    player.getWinsCount(),
                    player.getLossesCount(),
                    player.getMatchesCount(),
                    player.getPlayerTeamId()
            });
        }
    }




}

