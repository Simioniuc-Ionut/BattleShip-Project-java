package leaderboard;

import com.example.demo_battleship.model.Game;
import com.example.demo_battleship.model.Move;
import com.example.demo_battleship.model.Ship;
import org.example.GameClient;
import org.example.connection.HttpClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ViewStatisticFrame extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final GameClient client;

    public ViewStatisticFrame(GameClient client) {
        this.client = client;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Match History");
        setSize(800, 600);

        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton, BorderLayout.EAST);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().toLowerCase();
                try {
                    searchAndFilterTable(searchText,client.getPlayerIDFromDB());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        try {
            updateTableWithGames(client.getPlayerIDFromDB());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.darkGray);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewStatisticFrame.this.setVisible(false);
                new LeaderboardFrame(client).setVisible(true);
            }
        });

        add(backButton, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    private void updateTableWithGames(int playerId) throws Exception {
        tableModel.setColumnIdentifiers(new String[]{
                "Game ID", "Player1 ID", "Player2 ID", "Winner ID", "Created At",
                "Move ID", "Move", "Is Hit", "Move Created At",
                "Ship ID", "Ship Type", "Start Position", "End Position", "Is Sunk"
        });

        tableModel.setRowCount(0); // Clear the table before updating

        List<Game> games = (List<Game>) HttpClient.getGameList();
        List<Move> moves = (List<Move>) HttpClient.getMoveList();
        List<Ship> ships = (List<Ship>) HttpClient.getShipList();


        for (Game game : games) {
            if (game.getPlayer1Id() == playerId || game.getPlayer2Id() == playerId) {
                int gameId = game.getGameId();
                for (Move move : moves) {
                    if (move.getGameId().getGameId() == gameId && move.getPlayerId().getPlayerId() == playerId) {
                        tableModel.addRow(new Object[]{
                                gameId, game.getPlayer1Id(), game.getPlayer2Id(), game.getWinnerId(), game.getCreatedAt(),
                                move.getMoveId(), move.getMove(), move.getIsHit(), move.getCreatedAt(),
                                "", "", "", "", ""
                        });
                    }
                }

                for (Ship ship : ships) {
                    if (ship.getGame().getGameId() == gameId && ship.getPlayer().getPlayerId() == playerId) {
                        tableModel.addRow(new Object[]{
                                gameId, game.getPlayer1Id(), game.getPlayer2Id(), game.getWinnerId(), game.getCreatedAt(),
                                "", "", "", "",
                                ship.getShipId(), ship.getShipType(), ship.getStartPosition(), ship.getEndPosition(), ship.isSunk()
                        });
                    }
                }
            }
        }

        tableModel.fireTableDataChanged();
    }
    private void updateTableWithGamesSearch(int playerId , int playerSerch) throws Exception {
        tableModel.setColumnIdentifiers(new String[]{
                "Game ID", "Player1 ID", "Player2 ID", "Winner ID", "Created At",
                "Move ID", "Move", "Is Hit", "Move Created At",
                "Ship ID", "Ship Type", "Start Position", "End Position", "Is Sunk"
        });

        tableModel.setRowCount(0); // Clear the table before updating

        List<Game> games = (List<Game>) HttpClient.getGameList();
        List<Move> moves = (List<Move>) HttpClient.getMoveList();
        List<Ship> ships = (List<Ship>) HttpClient.getShipList();


        for (Game game : games) {
            if ((game.getPlayer1Id() == playerId && game.getPlayer2Id() == playerSerch) ||
                    (game.getPlayer1Id() == playerSerch && game.getPlayer2Id() == playerId) ) {
                int gameId = game.getGameId();
                for (Move move : moves) {
                    if (move.getGameId().getGameId() == gameId && move.getPlayerId().getPlayerId() == playerId) {
                        tableModel.addRow(new Object[]{
                                gameId, game.getPlayer1Id(), game.getPlayer2Id(), game.getWinnerId(), game.getCreatedAt(),
                                move.getMoveId(), move.getMove(), move.getIsHit(), move.getCreatedAt(),
                                "", "", "", "", ""
                        });
                    }
                }

                for (Ship ship : ships) {
                    if (ship.getGame().getGameId() == gameId && ship.getPlayer().getPlayerId() == playerId) {
                        tableModel.addRow(new Object[]{
                                gameId, game.getPlayer1Id(), game.getPlayer2Id(), game.getWinnerId(), game.getCreatedAt(),
                                "", "", "", "",
                                ship.getShipId(), ship.getShipType(), ship.getStartPosition(), ship.getEndPosition(), ship.isSunk()
                        });
                    }
                }
            }
        }

        tableModel.fireTableDataChanged();
    }

    private void searchAndFilterTable(String searchText, int playerId) throws Exception {
        int playerSerch = HttpClient.getPlayerId(searchText); // Fetch the player ID by username
        if (playerSerch != -1) { // If a valid player ID is found, update the table
            updateTableWithGamesSearch(playerId,playerSerch);
        }
    }
}
