package startGame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SubmitMove extends JPanel {
    final MainFrameBattle frame;
    JButton submitMoveBtn = new JButton("Submit Move");

    public SubmitMove(MainFrameBattle frame) {
        this.frame = frame;
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

    }



}
