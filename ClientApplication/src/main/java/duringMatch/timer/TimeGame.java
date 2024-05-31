package duringMatch.timer;

import duringMatch.MainFrameFour;

import javax.swing.*;
import java.awt.*;

public class TimeGame extends JPanel implements TimeUpdateListener {

    JLabel timerLabel;
    MainFrameFour frame;
    public TimeGame(MainFrameFour frame) {
        this.frame = frame;
        init();
    }

    private void init() {

        add(Box.createRigidArea(new Dimension(0, 10)));
        timerLabel = new JLabel("00:00");//? o sa fie sincronizat cu serverul
        timerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        add(timerLabel, BorderLayout.CENTER);

    }

    @Override
    public void onTimeUpdate(String time) {
        System.out.println("timer incep");
        SwingUtilities.invokeLater(() -> timerLabel.setText(time));
    }

}
