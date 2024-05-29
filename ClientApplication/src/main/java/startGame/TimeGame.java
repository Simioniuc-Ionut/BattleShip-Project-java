package startGame;

import javax.swing.*;
import java.awt.*;

public class TimeGame extends JPanel {

    JLabel timerLabel;
    MainFrameBattle frame;
    public TimeGame(MainFrameBattle frame) {
        this.frame = frame;
        init();
    }

    private void init() {

        add(Box.createRigidArea(new Dimension(0, 10)));
        timerLabel = new JLabel("00:00");//? o sa fie sincronizat cu serverul
        timerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        add(timerLabel, BorderLayout.CENTER);

    }
}
