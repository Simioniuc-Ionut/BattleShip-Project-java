package startGame;

import prepareShips.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OpponentBoard extends JPanel {
    private final MainFrameBattle frame;
    int cellSize = 40; // dimensiune fiecare celula
    int startX = 40; // pozitie start pe axa x a ferestrei
    int startY = 110; // pozitie start pe axa y a ferestrei
    Color[][] cellColorsShips = new Color[10][10];// pentru a stii starea fiecarei celule din matrice
    Integer rowClick;
    Integer colClick;
    Integer lastRowClicked = null;
    Integer lastColClicked = null;

    public OpponentBoard(MainFrameBattle frame) {
        this.frame = frame;
        // Inițializați toate celulele cu culoarea albă
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cellColorsShips[i][j] = Color.black;
            }
        }
        init();
    }

    final void init() {
        //mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (lastRowClicked != null && lastColClicked != null) {
                    // Resetare culoarea ultimei celule selectate
                    cellColorsShips[lastRowClicked][lastColClicked] = Color.black;
                }

                // Actualizare noua celula selectata
                rowClick = (e.getY() - startY) / cellSize;
                colClick = (e.getX() - startX) / cellSize;
                cellColorsShips[rowClick][colClick] = Color.red;

                // Referinta noii celule selectate
                lastRowClicked = rowClick;
                lastColClicked = colClick;

                repaint();
            }
        });

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

        // Deseneaza celulele cu negru
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board.setColor(Color.black);
                board.fillRect(startX + j * cellSize, startY + i * cellSize, cellSize, cellSize);
            }
        }

        // Deseneaza X roșu peste celulele selectate
//        board.setColor(Color.red); // Setează culoarea la roșu pentru X
//        board.setStroke(new BasicStroke(2)); // Setează grosimea liniei pentru X
//        if (lastRowClicked != null && lastColClicked != null) {
//            int x1 = startX + lastColClicked * cellSize;
//            int y1 = startY + lastRowClicked * cellSize;
//            int x2 = x1 + cellSize;
//            int y2 = y1 + cellSize;
//            // Desenați prima linie a X-ului
//            board.drawLine(x1, y1, x2, y2);
//            // Desenați a doua linie a X-ului
//            board.drawLine(x1, y2, x2, y1);
//        }

        // Deseneaza grila
        board.setColor(Color.white); // Seteaza culoarea grilei
        for (int i = 0; i <= 10; i++) {
            // Linii orizontale
            board.drawLine(startX, startY + i * cellSize, startX + 10 * cellSize, startY + i * cellSize);
            // Linii verticale
            board.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + 10 * cellSize);
        }
    }


}