package prepareShips;

import javax.swing.*;
import java.awt.*;

public class ClientBoard extends JPanel {
    private final MainFrameThree frame;
    int cellSize = 40; // dimensiune fiecare celula
    int startX = 40; // pozitie start pe axa x a ferestrei
    int startY = 110; // pozitie start pe axa y a ferestrei
    Color[][] cellColorsShips = new Color[10][10];// pentru a stii starea fiecarei celule din matrice
    private JLabel playerIdNameLabel;

    public ClientBoard(MainFrameThree frame) {
        this.frame = frame;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cellColorsShips[i][j] = Color.WHITE;
            }
        }
        init();
    }

    void updatePlayerInfoLabel() {
        playerIdNameLabel.setText("Team ID: " + frame.client.getPlayerTeamId() + " | Username: " + frame.client.getPlayerUsername());
    }

    final void init() {
        this.setLayout(null);
        playerIdNameLabel = new JLabel("Player id Name", SwingConstants.LEFT);
        playerIdNameLabel.setBounds(10, 10, 430, 30);
        Font newFont = new Font("default", Font.BOLD, 20);
        playerIdNameLabel.setFont(newFont);
        playerIdNameLabel.setOpaque(true);
        playerIdNameLabel.setBackground(Color.LIGHT_GRAY);
        this.add(playerIdNameLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D board = (Graphics2D) g;//convertire pentru a utiliza functionalitati mai avansate

        // Deseneaza etichetele coloanelor (1-10)
        for (int i = 1; i <= 10; i++) {
            String writeColumn = Integer.toString(i);
            int xWriteColumn = -5 + i * cellSize + cellSize / 2;
            int ytWriteColumn = startY - cellSize / 2;
            board.drawString(writeColumn, xWriteColumn, ytWriteColumn);
        }

        // Deseneaza etichetele randurilor (A-J)
        for (int i = 0; i < 10; i++) {
            char writeRow = (char) ('A' + i);
            int xWriteRow = startX - cellSize / 2;
            int yWriteRow = 5 + startY + i * cellSize + cellSize / 2;
            board.drawString(Character.toString(writeRow), xWriteRow, yWriteRow);
        }

        // colorez fiecare celula in functie de selectarea pozitiei navei
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board.setColor(cellColorsShips[i][j]);
                board.fillRect(startX + j * cellSize, startY + i * cellSize, cellSize, cellSize);
            }
        }


        // Deseneaza grila
        board.setColor(Color.BLACK); // Seteaza culoarea grilei la negru
        for (int i = 0; i <= 10; i++) {
            // Linii orizontale
            board.drawLine(startX, startY + i * cellSize, startX + 10 * cellSize, startY + i * cellSize);
            // Linii verticale
            board.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + 10 * cellSize);
        }


    }


}