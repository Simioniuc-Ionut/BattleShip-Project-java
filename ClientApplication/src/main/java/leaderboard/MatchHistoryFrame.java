package leaderboard;

import com.example.demo_battleship.model.Game;
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

public class MatchHistoryFrame extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTable table;
    GameClient client;

    public MatchHistoryFrame(GameClient client) {
        this.client = client;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Match History");
        setSize(700, 400);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        updateTableWithGames();

        //  butonului Back
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.darkGray);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MatchHistoryFrame.this.setVisible(false);
                new LeaderboardFrame(client).setVisible(true);
            }
        });


        add(backButton, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    private void updateTableWithGames() {
        // Adăugare coloane
        tableModel.addColumn("Game ID");
        tableModel.addColumn("Player1 ID");
        tableModel.addColumn("Player2 ID");
        tableModel.addColumn("Winner ID");
        tableModel.addColumn("Created At");

        // Obținere lista de jocuri
        List<Game> games = (List<Game>) HttpClient.getGameList();
        for (Game game : games) {
            tableModel.addRow(new Object[]{
                    game.getGameId(),
                    game.getPlayer1Id(),
                    game.getPlayer2Id(),
                    game.getWinnerId(),
                    game.getCreatedAt()
            });
        }

        // Notificare schimbare tabel
        tableModel.fireTableDataChanged();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        int clientId = client.getPlayerIDFromDB();

        // Creare RowFilter pentru a filtra randurile
        RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {

                Integer player1Id = (Integer) entry.getValue(1);
                Integer player2Id = (Integer) entry.getValue(2);
                return player1Id.equals(clientId) || player2Id.equals(clientId);
            }
        };

        // Aplicare filtru la sorter
        sorter.setRowFilter(filter);
    }


}

