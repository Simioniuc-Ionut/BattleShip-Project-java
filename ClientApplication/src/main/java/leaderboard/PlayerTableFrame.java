package leaderboard;

import com.example.demo_battleship.model.Player;
import org.example.GameClient;
import org.example.connection.HttpClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

public class PlayerTableFrame extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTable table;
    GameClient client;

    public PlayerTableFrame(GameClient client) {
        this.client = client;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Leaderboard");
        setSize(700, 400);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        updateTableWithPlayers();

        //  butonului Back
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.darkGray);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PlayerTableFrame.this.setVisible(false);
                new LeaderboardFrame(client).setVisible(true);
            }
        });


        add(backButton, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    private void updateTableWithPlayers() {
        // adaugare coloane
        tableModel.addColumn("Player ID");
        tableModel.addColumn("Player Name");
        tableModel.addColumn("Hits");
        tableModel.addColumn("Misses");
        tableModel.addColumn("Wins");
        tableModel.addColumn("Losses");
        tableModel.addColumn("Matches");

        // opbtinere lista
        java.util.List<Player> players = (List<Player>) HttpClient.getPlayersList();
        for (Player player : players) {
            tableModel.addRow(new Object[]{
                    player.getPlayerId(),
                    player.getPlayerName(),
                    player.getHitsCount(),
                    player.getMissesCount(),
                    player.getWinsCount(),
                    player.getLossesCount(),
                    player.getMatchesCount(),
            });
        }

        // notificare schimbare tabel
        tableModel.fireTableDataChanged();

        // crearea sortarii
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Definirea comparatorului pentru coloana "Wins"
        sorter.setComparator(4, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1); // Ordine descrescatoare
            }
        });

        // Aplicarea sortarii pe baza coloanei "Wins"
        sorter.toggleSortOrder(4);
    }


}

