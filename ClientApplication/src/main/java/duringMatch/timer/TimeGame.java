//package duringMatch.timer;
//
//import duringMatch.MainFrameFour;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class TimeGame extends JPanel {
//
//    JLabel timerLabel;
//    MainFrameFour frame;
//    public TimeGame(MainFrameFour frame) {
//        this.frame = frame;
//        init();
//    }
//
//    private void init() {
//
//        add(Box.createRigidArea(new Dimension(0, 10)));
//        timerLabel = new JLabel("00:00");//? o sa fie sincronizat cu serverul
//        timerLabel.setFont(new Font("Arial", Font.BOLD, 40));
//        add(timerLabel, BorderLayout.CENTER);
//

//    }
//
//
//    public void onTimeUpdate(String time) {
//        SwingUtilities.invokeLater(() -> {
//            timerLabel.setText(time); // Asigurați-vă că acest cod funcționează corect
//            System.out.println("Timer updated to: " + time); // Adăugați acest mesaj pentru depanare
//        });
//    }
//
//
//}

package duringMatch.timer;

import javax.swing.*;
import java.awt.*;

public class TimeGame extends JPanel {
    private final JLabel timerLabel;

    public TimeGame() {
        timerLabel = new JLabel("Time remaining: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        add(timerLabel, BorderLayout.CENTER);
    }

    public void updateTime(String time) {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText("Time remaining: " + time);
        });
    }
}
