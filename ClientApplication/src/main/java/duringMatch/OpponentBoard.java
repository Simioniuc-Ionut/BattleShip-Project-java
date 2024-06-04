package duringMatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class OpponentBoard extends JPanel {
    private final MainFrameFour frame;
    int cellSize = 40; // dimensiune fiecare celula
    int startX = 40; // pozitie start pe axa x a ferestrei
    int startY = 110; // pozitie start pe axa y a ferestrei
    Color[][] cellColorsShips = new Color[10][10];// pentru a stii starea fiecarei celule din matrice
    Integer rowClick;
    Integer colClick;
    Integer lastRowClicked = null;
    Integer lastColClicked = null;
    //adaug haset ca sa pastrez celulele tintite sa fie colorate permanent
    Set<String> permanentCells = new HashSet<>();
    boolean[][] hitCells = new boolean[10][10];


    public OpponentBoard(MainFrameFour frame) {
        this.frame = frame;
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

                int row = (e.getY() - startY) / cellSize;
                int col = (e.getX() - startX) / cellSize;

                // Verific daca celula este deja permanenta
                if (permanentCells.contains(row + "," + col)) {
                    return;
                }

                if (lastRowClicked != null && lastColClicked != null) {
                    // Resetare culoarea ultimei celule selectate
                    cellColorsShips[lastRowClicked][lastColClicked] = Color.black;
                }

                // Actualizare noua celula selectata
                rowClick = row;
                colClick = col;
                cellColorsShips[rowClick][colClick] = Color.GRAY;

                // Referinta noii celule selectate
                lastRowClicked = rowClick;
                lastColClicked = colClick;

                repaint();
            }

//                sendMessageToServer();
//            }
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

        // colorez fiecare celula in functie de selectarea pozitiei navei
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int cellX = startX + j * cellSize;
                int cellY = startY + i * cellSize;

                //culoarea de fundal a celulei
                board.setColor(cellColorsShips[i][j]);
                board.fillRect(cellX, cellY, cellSize, cellSize);

                if (hitCells[i][j]) {
                    board.setColor(Color.RED);
                    board.setStroke(new BasicStroke(1));
                    // Deseneaza X
                    board.drawLine(cellX, cellY, cellX + cellSize, cellY + cellSize);
                    board.drawLine(cellX + cellSize, cellY, cellX, cellY + cellSize);
                }
            }
        }


        // Deseneaza grila
        board.setColor(Color.white);
        for (int i = 0; i <= 10; i++) {
            // Linii orizontale
            board.drawLine(startX, startY + i * cellSize, startX + 10 * cellSize, startY + i * cellSize);
            // Linii verticale
            board.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + 10 * cellSize);
        }
    }

    void colorPositionHit(String position, Color x, boolean hitOrNotYourTurn) {
        // Decodificare
        char rowChar = position.charAt(0);
        int column = Integer.parseInt(position.substring(1)) - 1;
        int row = rowChar - 'A';

        if (row >= 0 && row < 10 && column >= 0 && column < 10) {
            // Daca este un hit, coloram si adaug in permanentCells
            hitCells[row][column] = hitOrNotYourTurn;
            String cellKey = row + "," + column;

            if (hitOrNotYourTurn) {
                permanentCells.add(cellKey);
            } else {
                // Daca hitOrNotYourTurn este false, stergem pozitia din permanentCells
                permanentCells.remove(cellKey);
            }

            cellColorsShips[row][column] = x;
            repaint();
        }
    }


}