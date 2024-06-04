package duringMatch;

import duringMatch.timer.TimeGame;
import org.example.GameClient;
import org.example.TimerUpdateThread;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.Socket;

public class MainFrameFour extends JFrame {
    ClientBoardBattle clientBoardBattle;
    OpponentBoard opponentBoard;
    SettingsBattle settingsBattle;
    SubmitMove submitMove;
    TimeGame timeGame;

    public GameClient client;
    private final Socket socketTimer;

    public MainFrameFour(GameClient client, Color[][] initialCellColors) {
        super("startGameBattleShip");
        this.client = client;
        this.timeGame = new TimeGame();
        this.socketTimer=client.getSocketTimer();

        //aici creez tredul de timer
        if(!client.isTimerThreadRunning()) {
            new TimerUpdateThread(socketTimer, this).start();
            client.setTimerThreadRunning(true); //deja a fost creat un thread set timer
        }
        initStartGameBattleShip(initialCellColors);
    }
    public TimeGame getTimeGamePanel(){
        return timeGame;
    }

    private void initStartGameBattleShip(Color[][] initialCellColors) {
        setDefaultCloseOperation(EXIT_ON_CLOSE); // inchide fereastra

        // componentele din fereatra
        clientBoardBattle = new ClientBoardBattle(this, initialCellColors);
        opponentBoard = new OpponentBoard(this);
        settingsBattle = new SettingsBattle(this);
        timeGame = new TimeGame();
        submitMove = new SubmitMove(this,opponentBoard,clientBoardBattle,settingsBattle);


        // grupare oe axa x
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // spatiere
        clientBoardBattle.setBorder(new EmptyBorder(0, 10, 0, 5));
        opponentBoard.setBorder(new EmptyBorder(0, 5, 0, 5));
        settingsBattle.setBorder(new EmptyBorder(0, 5, 0, 5));


        mainPanel.add(clientBoardBattle);
        mainPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        mainPanel.add(settingsBattle);
        mainPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        mainPanel.add(opponentBoard);

        // Add components
        add(timeGame, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(submitMove, BorderLayout.SOUTH);


        // Setarea teamId și username
        clientBoardBattle.updatePlayerInfoLabel();

        pack();
        setSize(new Dimension(1400, 700));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
