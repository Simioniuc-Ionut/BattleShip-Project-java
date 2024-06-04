package duringMatch;

import finishMatch.MainFrameFive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SubmitMove extends JPanel {
    final MainFrameFour frame;
    OpponentBoard opponentBoard;
    ClientBoardBattle clientBoardBattle;
    SettingsBattle settingsBattle;
    JButton submitMoveBtn = new JButton("Submit Move");
    private boolean gameOverDisplayed = false; // Flag pt actiunea de a deschide gameover

    public SubmitMove(MainFrameFour frame, OpponentBoard opponentBoard, ClientBoardBattle clientBoardBattle, SettingsBattle settingsBattle) {
        this.frame = frame;
        this.opponentBoard = opponentBoard;
        this.clientBoardBattle = clientBoardBattle;
        this.settingsBattle = settingsBattle;

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

        submitMoveBtn.addActionListener(this::listenerAddSubmitMove);

        // Thread care asculta mesajele : server->client->interfata
        threadMessageFromClient();


    }

    public void threadMessageFromClient() {
        new Thread(() -> {
            while (true) {
                String message = frame.client.getMessage();
                if (message != null) {
                    final String msg = message;
                    SwingUtilities.invokeLater(() -> {
                        handleServerMessageForPaintClientBoard(msg);
                        handleServerMessageForPaintHit(msg);
                        verifyGameOver(msg);
                        //afisare mesaje in JPanel
                        settingsBattle.messageTextAreaBattle.setText(msg);
                        settingsBattle.messageTextAreaBattle.repaint();
                        settingsBattle.revalidate();
                        settingsBattle.repaint();
                    });
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void verifyGameOver(String msg) {
        if (msg.contains("Game over.") && !gameOverDisplayed) {

            gameOverDisplayed = true; // setez falgul pt a se deschide doar cate o fereastra

            SwingUtilities.invokeLater(() -> {
                new MainFrameFive(frame.client, msg).setVisible(true);
                frame.dispose(); // inchidere ferestre client
            });
        }
    }

    public void listenerAddSubmitMove(ActionEvent e) {
        //trimit pozitia catre client
        sendPositionToClient();

        //colorez celulele selectate temporar
        colorCellSelected();

    }


    private void colorCellSelected() {
        if (opponentBoard.lastRowClicked != null && opponentBoard.lastColClicked != null) {
            opponentBoard.permanentCells.add(opponentBoard.lastRowClicked + "," + opponentBoard.lastColClicked);
            opponentBoard.cellColorsShips[opponentBoard.lastRowClicked][opponentBoard.lastColClicked] = Color.GRAY;
            opponentBoard.lastRowClicked = null;
            opponentBoard.lastColClicked = null;
            opponentBoard.repaint();
        }
    }

    private void handleServerMessageForPaintClientBoard(String message) {
        // Exemplu de mesaj: "Server response: Opponent moved: E4. Your turn."
        if (message.contains("Opponent moved:")) {
            // Extragerea pozitiei
            String position = message.substring(message.indexOf("Opponent moved:") + 15, message.indexOf(". Your turn")).trim();
            // Apelarea metodei de colorare
            SwingUtilities.invokeLater(() -> {
                clientBoardBattle.colorPosition(position);
            });
        }

    }

    private void handleServerMessageForPaintHit(String message) {
        //Server response: You hit at position: A1. Waiting for opponent's move
        if (message.contains("You hit at position:")) {

            String position = message.substring(message.indexOf("You hit at position:") + 20, message.indexOf(". Waiting for opponent's move")).trim();
            SwingUtilities.invokeLater(() -> {
                opponentBoard.colorPositionHit(position, Color.BLACK, true);
            });
        } else if (message.contains("NOT_YOUR_TURN: ")) {
            // Extragerea pozitiei
            String position = message.substring(message.indexOf("NOT_YOUR_TURN: ") + 15).trim();
            // Apelarea metodei de colorare
            SwingUtilities.invokeLater(() -> {
                opponentBoard.colorPositionHit(position, Color.BLACK, false);
            });
        }
    }

    private void sendPositionToClient() {
        // Obtine valorile din click (tinta) si trimit raspunsul cu pozitia la client->server
        if (opponentBoard.rowClick != null && opponentBoard.colClick != null) {
            // Convertire
            String submitRowLetter = String.valueOf((char) ('A' + opponentBoard.rowClick));
            String submitColNumber = String.valueOf(opponentBoard.colClick + 1);

            StringBuilder messageToClient = new StringBuilder();
            messageToClient.append(submitRowLetter);
            messageToClient.append(submitColNumber);

            //trimit catre client mesajul ca sa ajunge dupa la server
            frame.client.setAnswer(messageToClient.toString().trim());

            System.out.println("SUBMITED -> " + messageToClient);
        }
    }

}