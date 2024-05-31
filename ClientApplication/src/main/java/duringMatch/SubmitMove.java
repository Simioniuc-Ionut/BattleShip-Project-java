package duringMatch;

import finishMatch.MainFrameFinish;
import org.example.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Semaphore;

public class SubmitMove extends JPanel {
    final MainFrameBattle frame;
    OpponentBoard opponentBoard;
    ClientBoardBattle clientBoardBattle;
    SettingsBattle settingsBattle;
    JButton submitMoveBtn = new JButton("Submit Move");
    private boolean gameOverDisplayed = false; // Flag to track if the game over window has been displayed

    public SubmitMove(MainFrameBattle frame,OpponentBoard opponentBoard, ClientBoardBattle clientBoardBattle,SettingsBattle settingsBattle) {
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

        //configurare listeners
        submitMoveBtn.addActionListener(this::listenerAddSubmitMove);

        //SINCRONIZARE cu mesajele primite de la SERVER
        // În clasa SubmitMove, adăugați un nou callback pentru gestionarea mesajelor de la server


        // Modificați thread-ul care ascultă mesajele de la server pentru a apela acest callback
        threadMessageFromClient();



    }
    public void threadMessageFromClient(){
        new Thread(() -> {
            while (true) {
                String message = frame.client.getMessage();
                if (message != null) {
                    final String msg = message;
                    SwingUtilities.invokeLater(() -> {
                        handleServerMessageForPaintClientBoard(msg);
                        handleServerMessageForPaintHit(msg);
                        verifyGameOver(msg);
                        settingsBattle.messageTextAreaBattle.setText(msg);
                        settingsBattle.messageTextAreaBattle.repaint();
                        settingsBattle.revalidate();
                        settingsBattle.repaint();


                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void verifyGameOver(String msg){

        if (msg.contains("Game over.") && !gameOverDisplayed) {

            gameOverDisplayed = true; // Set the flag to true to prevent multiple windows
            SwingUtilities.invokeLater(() -> {
                new MainFrameFinish(frame.client).setVisible(true); // Open Game Over window
                frame.dispose(); // Close the game window
            });
        }
    }
        public void listenerAddSubmitMove(ActionEvent e){
            //trimit pozitia catre client
            sendPositionToClient();

//            if ((validateYourTurnToMove())) {
//                System.out.println(validateYourTurnToMove()+" ROSU");
            colorCellSelected();
//            } else {
//                System.out.println(validateYourTurnToMove()+" NEGRU");
//                colorCellBlack();
//            }

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

//        private void colorCellBlack() {
//            if (opponentBoard.lastRowClicked != null && opponentBoard.lastColClicked != null) {
//                opponentBoard.cellColorsShips[opponentBoard.lastRowClicked][opponentBoard.lastColClicked] = Color.black;
//                opponentBoard.permanentCells.remove(opponentBoard.lastRowClicked + "," + opponentBoard.lastColClicked);
//                opponentBoard.lastRowClicked = null;
//                opponentBoard.lastColClicked = null;
//                opponentBoard.repaint();
//            }
//        }

    private  boolean validateYourTurnToMove(){
        System.out.println("Aici validez TURN");
        Semaphore lock = frame.client.getMoveTurnLock();
        synchronized (lock) {
            try {
                //Asteapta:
                lock.acquire(); // Așteaptă până când primește notify() de la server
                return frame.client.isYourTurnToMakeAMove();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread intrerupt in validation");
                return false;
            }
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

            String position = message.substring(message.indexOf("You hit at position:") + 20,  message.indexOf(". Waiting for opponent's move")).trim();
            SwingUtilities.invokeLater(() -> {
                opponentBoard.colorPositionHit(position,Color.BLACK,true);
            });
        }
       else if (message.contains("NOT_YOUR_TURN: ")) {
           // Extragerea pozitiei
           String position = message.substring(message.indexOf("NOT_YOUR_TURN: ") + 15).trim();
           // Apelarea metodei de colorare
           SwingUtilities.invokeLater(() -> {
               opponentBoard.colorPositionHit(position,Color.BLACK,false);
           });
       }
    }


    private String getMessage() {
        Semaphore lock = frame.client.getMessageLock();
        synchronized (lock) {
            try {
                lock.acquire(); // Așteaptă până când primește notify() de la server
                return frame.client.getMessage();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //System.out.println("Thread intrerupt in validation from SettingsPlaceShip");
                return "Thread intrerupt in validation from SettingsPlaceShip";
            }
        }
    }
//    private  boolean validateYourTurnToMove(){
//        System.out.println("Aici validez TINTA");
//        Semaphore lock = frame.client.getMoveTurnLock();
//        synchronized (lock) {
//            try {
//                //Asteapta:
//                lock.acquire(); // Așteaptă până când primește notify() de la server
//                return frame.client.isYourTurnToMakeAMove();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                System.out.println("Thread intrerupt in validation");
//                return false;
//            }
//        }
//
//    }
    private void sendPositionToClient(){
        // Obtine valorile din click (tinta)
        if (opponentBoard.rowClick != null && opponentBoard.colClick != null) {
            // Convertire
            String submitRowLetter = String.valueOf((char) ('A' + opponentBoard.rowClick));
            String submitColNumber = String.valueOf(opponentBoard.colClick + 1);

            StringBuilder messageToClient = new StringBuilder();
            messageToClient.append(submitRowLetter);
            messageToClient.append(submitColNumber);
            //trimit catre client mesajul ca sa ajunge dupa la server
            frame.client.setAnswer(messageToClient.toString().trim());

            System.out.println("SUBMITED -> "+messageToClient);
        }
    }
    //fac sa primesc mesaje de la server privind starea

}