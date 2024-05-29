package startGame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SubmitMove extends JPanel {
    final MainFrameBattle frame;
    OpponentBoard opponentBoard;
    ClientBoardBattle clientBoardBattle;
    JButton submitMoveBtn = new JButton("Submit Move");

    public SubmitMove(MainFrameBattle frame, OpponentBoard opponentBoard, ClientBoardBattle clientBoardBattle) {
        this.frame = frame;
        this.opponentBoard = opponentBoard;
        this.clientBoardBattle = clientBoardBattle;
        init();
    }

    private void init() {

        add(submitMoveBtn);


        //style buton
        Font newFont = new Font("default", Font.BOLD, 20);
        submitMoveBtn.setFont(newFont);
        submitMoveBtn.setPreferredSize(new Dimension(180, 60));
        submitMoveBtn.setBackground(Color.darkGray);
        submitMoveBtn.setForeground(Color.WHITE);//culoare text

        //configurare listeners
        submitMoveBtn.addActionListener(this::listenerAddSubmitMove);

    }
    public void listenerAddSubmitMove(ActionEvent e){
        // Obtine valorile din click (tinta)
        if (opponentBoard.rowClick != null && opponentBoard.colClick != null) {
            // Convertire
            String submitRowLetter = String.valueOf((char) ('A' + opponentBoard.rowClick));
            String submitColNumber = String.valueOf(opponentBoard.colClick + 1);

            StringBuilder messageToClient = new StringBuilder();
            messageToClient.append(submitRowLetter);
            messageToClient.append(submitColNumber);
            //trimit catre client mesajul ca sa ajunge dupa la server
//            frame.client.setAnswer(messageToClient.toString().trim());

            System.out.println("SUBMITED -> "+messageToClient);
        }

        // celula selectata dupa submit va ramane rosie
        if (opponentBoard.lastRowClicked != null && opponentBoard.lastColClicked != null) {
            opponentBoard.cellColorsShips[opponentBoard.lastRowClicked][opponentBoard.lastColClicked] = Color.red;
            opponentBoard.lastRowClicked = null;
            opponentBoard.lastColClicked = null;
            opponentBoard.repaint();
        }




    }



}