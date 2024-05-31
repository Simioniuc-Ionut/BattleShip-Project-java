package duringMatch;

import javax.swing.*;
import java.awt.*;

public class ClientBoardBattle extends JPanel {
    private final MainFrameBattle frame;
    int cellSize = 40; // dimensiune fiecare celula
    int startX = 40; // pozitie start pe axa x a ferestrei
    int startY = 110; // pozitie start pe axa y a ferestrei
    Color[][] cellColorsShips = new Color[10][10];// pentru a stii starea fiecarei celule din matrice
    boolean[][] hitCells = new boolean[10][10];
    public ClientBoardBattle(MainFrameBattle frame, Color[][] initialCellColors) {
        this.frame = frame;
        //preiau tabela colorata (cu navele plasate) din MainFrame->ClientBoard

        this.cellColorsShips = initialCellColors;
        init();
    }

    final void init() {

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
                int cellX = startX + j * cellSize;
                int cellY = startY + i * cellSize;

                // Desenează culoarea de fundal a celulei
                board.setColor(cellColorsShips[i][j]);
                board.fillRect(cellX, cellY, cellSize, cellSize);

                // Verifică dacă celula este marcată ca lovită în matricea hitCells și desenează X
                if (hitCells[i][j]) {
                    board.setColor(Color.RED); // Setează culoarea pentru X
                    board.setStroke(new BasicStroke(1)); // Setează grosimea liniei pentru X
                    // Desenează X
                    board.drawLine(cellX, cellY, cellX + cellSize, cellY + cellSize);
                    board.drawLine(cellX + cellSize, cellY, cellX, cellY + cellSize);
                }
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
    void colorPosition(String position) {
        // Decodificare
        char rowChar = position.charAt(0);
        int column = Integer.parseInt(position.substring(1)) - 1;
        int row = rowChar - 'A';

        // Verifică dacă poziția este validă
        if(row >= 0 && row < 10 && column >= 0 && column < 10) {

            hitCells[row][column] = true;
            repaint();
        }
    }


}