package prepareShips;

import duringMatch.MainFrameFour;
import org.example.connection.HttpClient;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ControlPanelBottom extends JPanel {
    final MainFrameThree frame;
    JButton addShipBtn = new JButton("Add Ship");
    JButton readyForGameBtn = new JButton("Ready");
    SettingsPlaceShip settingsPlaceShip;
    ClientBoard clientBoard;
    ArrayList<Ship> shipsList = new ArrayList<>();// o lista cu cele 5 nave
    int currentShipIndex = 0;// numararea navelor, pentru a sti nava curenta
    public ControlPanelBottom(MainFrameThree frame, SettingsPlaceShip settingsPlaceShip, ClientBoard clientBoard) { // Modificați constructorul pentru a include ClientBoard
        this.frame = frame;
        this.settingsPlaceShip = settingsPlaceShip;
        this.clientBoard = clientBoard;
        shipsList.add(new Ship("Carrier",5,Color.BLUE));
        shipsList.add(new Ship("Battleship",4, Color.CYAN));
        shipsList.add(new Ship("Destroyer",3, Color.ORANGE));
        shipsList.add(new Ship("Submarine",3, Color.YELLOW));
        shipsList.add(new Ship("Patrol Boat",2, Color.PINK));

        init();
    }
    private void init() {

        //adaug butonul de adaugare nava
        add(addShipBtn);
        //buton dupa ce plaseaza ultima nava pt a incepe jocul
        add(readyForGameBtn);
        readyForGameBtn.setVisible(false);

        //style buton
        Font newFont = new Font("default", Font.BOLD, 20);
        addShipBtn.setFont(newFont);
        addShipBtn.setPreferredSize(new Dimension(150, 70));
        addShipBtn.setBackground(Color.darkGray);
        addShipBtn.setForeground(Color.WHITE);

        readyForGameBtn.setFont(newFont);
        readyForGameBtn.setPreferredSize(new Dimension(150, 70));
        readyForGameBtn.setBackground(Color.darkGray);
        readyForGameBtn.setForeground(Color.WHITE);

        //configurare listeners
        addShipBtn.addActionListener(this::listenerAddShipOnBoard);
        readyForGameBtn.addActionListener(this::listenerReadyForGame);

        //cream jocul odata cu crearea noii ferestre
        createGameInDb();
    }

    private void listenerAddShipOnBoard(ActionEvent e) {
        // valorile din spinner
        String fromRowLetter = (String) settingsPlaceShip.fromRow.getValue();
        int fromRow = fromRowLetter.charAt(0) - 'A'; // convertire de la litera la nr
        int fromCol = (Integer) settingsPlaceShip.fromColumn.getValue() - 1; // scad 1 pt a obtine indexul matricii 0-9

        String toRowLetter = (String) settingsPlaceShip.toRow.getValue();
        int toRow = toRowLetter.charAt(0) - 'A';
        int toCol = (Integer) settingsPlaceShip.toColumn.getValue() - 1;


        //Aici creez mesajul cu taote pozitiile navei pentru a fi trimis corect catre CLIENT->SERVER
        StringBuilder messageToClient = new StringBuilder();

        if (fromRow == toRow) { // Daca randurile sunt aceleasi (ex. A1 la A5)
            for (int i = fromCol; i <= toCol; i++) {
                messageToClient.append((char) (fromRow + 'A'));
                messageToClient.append(i + 1);
                messageToClient.append(" ");
            }
        } else if (fromCol == toCol) { // Daca coloanele sunt aceleasi (ex. A1 la D1)
            for (int i = fromRow; i <= toRow; i++) {
                messageToClient.append((char) (i + 'A'));
                messageToClient.append(fromCol + 1);
                messageToClient.append(" ");
            }
        }
        //trimit catre client mesajul ca sa ajunge dupa la server
        frame.client.setAnswer(messageToClient.toString().trim());

        // validare pozitie nava plasata
        if(validationPositionOfShip()) {
                // Obtine culoarea navei curente
                Color shipColor = shipsList.get(currentShipIndex).colorShip;

                // Seteaza culoarea celulelor pe baza culorii navei
                for (int i = fromRow; i <= toRow; i++) {
                    for (int j = fromCol; j <= toCol; j++) {
                        clientBoard.cellColorsShips[i][j] = shipColor;
                    }
                }

                // Trece la urmatoarea nava
                currentShipIndex++;
                if (currentShipIndex < shipsList.size()) {
                    Ship nextShip = shipsList.get(currentShipIndex);
                    // Actualizeaza numele si lungimea afisate cu setText
                    settingsPlaceShip.textNameShip.setText(nextShip.name);
                    settingsPlaceShip.textSizeShip.setText(Integer.toString(nextShip.size));
                }
            }
        else {
                System.out.println("Pozitie invalida ,am intrat pe validare fals");
            }

        //afisare mesaj SERVER
        displayMessageFromServer();

        //dupa ce plaseaza utlima nava va aparea butonul de ready
        if (currentShipIndex == 1) { // trebuie SCHIMBAT la 5 dupa pt joc
            addShipBtn.setVisible(false);//dispare btn add ship
            readyForGameBtn.setVisible(true);
        }

        // Repictare matrice client
        clientBoard.repaint();

    }
    private void displayMessageFromServer() {
        String serverMessage = getMessage();
        System.out.println("SERVER message in GUI:"+serverMessage);

        if (serverMessage != null) {
            // Actualizarea componentei Swing
            SwingUtilities.invokeLater(() -> {
                settingsPlaceShip.messageTextArea.setText(serverMessage);
                settingsPlaceShip.messageTextArea.setOpaque(true);
                settingsPlaceShip.messageTextArea.setBackground(new Color(0, 0, 0, 123));
                settingsPlaceShip.messageTextArea.repaint();
                settingsPlaceShip.revalidate();
                settingsPlaceShip.repaint();
            });
        }

    }
    private String getMessage() {
        Semaphore lock = frame.client.getMessageLock();
        synchronized (lock) {
            try {
                lock.acquire(); // Așteapta pana cand primesye notify() de la server
                return frame.client.getMessage();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //System.out.println("Thread intrerupt in validation from SettingsPlaceShip");
                return "Thread intrerupt in validation from SettingsPlaceShip";
            }
        }
    }
    private boolean validationPositionOfShip() {
        Semaphore lock = frame.client.getLock();
        synchronized (lock) {
            try {
                lock.acquire();// Așteapta pana cand primesye notify() de la server
                return frame.client.isPositionConfirmed();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread intrerupt in validation");
                return false;
            }
        }
    }
    private void listenerReadyForGame(ActionEvent e) {

        System.out.println("Am apasat butronul READY");
        String messageToClient = "READY";
        frame.client.setAnswer(messageToClient);


        displayMessageFromServer();

        waitToStart(); //asteptam sa primim de la server semnalul de start

        //aici sunt sigur ca s ambii playeri conectati
        //pot crea meciul in bd

        //cel vechi, am nevoie sa creez jocul inainte sa pun piesele pe tabla
        // createGameInDb();

        new MainFrameFour(frame.client, clientBoard.cellColorsShips).setVisible(true); //apare urmatoarea fereastra
        String serverMessage = getMessage();
        System.out.println("SERVER message in GUI:"+serverMessage);

        if (serverMessage != null) {
            // Actualizarea componentei Swing

            SwingUtilities.invokeLater(() -> {
                settingsPlaceShip.messageTextArea.setText(serverMessage);
                settingsPlaceShip.messageTextArea.setOpaque(true);
                settingsPlaceShip.messageTextArea.setBackground(new Color(0, 0, 0, 123));
                settingsPlaceShip.messageTextArea.repaint();
                settingsPlaceShip.revalidate();
                settingsPlaceShip.repaint();
            });

        }

        frame.setVisible(false);//inchide fereastra

    }
    private void waitToStart(){
        System.out.println("Am intrat in waitToStart()");
        Semaphore lock = frame.client.getGameCouldStartlock();
        synchronized (lock) {
            try {

                lock.acquire(); // Așteapta pana cand primesye notify() de la server
                System.out.println("Am primit semnalul de start");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread intrerupt in validation");
            }
        }
    }

    private synchronized void createGameInDb() {
        try {
            int playerId = frame.client.getPlayerIDFromDB();
            boolean createNewGame = checkIfNewGameNeeded();

            createOrUpdateGame(playerId, createNewGame);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to create or update game: " + ex.getMessage());
        }
    }

    private boolean checkIfNewGameNeeded() throws Exception {
        String checkGameUrl = "http://localhost:8080/api/games/checkGameExists";
        String existingGameResponse = HttpClient.sendGetRequest(checkGameUrl);
        return existingGameResponse.equals("false");
    }


    private void createOrUpdateGame(int playerId, boolean create) throws Exception {
        if (create) {
            // Creăm un nou joc
            String createGameUrl = "http://localhost:8080/api/games/create";

            String jsonInputString = "{\"player1Id\" : " + playerId + "}";


            String gameCreationResponse = HttpClient.sendPostRequest(createGameUrl, jsonInputString);
            System.out.println("Game creation response: " + gameCreationResponse);
        } else {
            // Actualizăm jocul existent cu player2Id
            String updateGameUrl = "http://localhost:8080/api/games/update/player2Id/" + playerId;


            String   jsonInputString = "{\"player2Id\" : " + playerId + "}";

            String gameUpdateResponse = HttpClient.sendPostRequest(updateGameUrl, jsonInputString);
            System.out.println("Update player2Id response: " + gameUpdateResponse);
        }
    }


    }
