package prepareShips;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ControlPanelBottom extends JPanel {
    final MainFrame frame;
    JButton addShipBtn = new JButton("Add Ship");
    SettingsPlaceShip settingsPlaceShip; // Adăugați acest câmp
    ClientBoard clientBoard; // Adăugați acest câmp
    ArrayList<Ship> shipsList = new ArrayList<>();// o lista cu cele 5 nave
    int currentShipIndex = 0;// numararea navelor, pentru a sti nava curenta


    public ControlPanelBottom(MainFrame frame, SettingsPlaceShip settingsPlaceShip, ClientBoard clientBoard) { // Modificați constructorul pentru a include ClientBoard
        this.frame = frame;
        this.settingsPlaceShip = settingsPlaceShip; // Setează SettingsPlaceShip
        this.clientBoard = clientBoard; // Setează ClientBoard
        shipsList.add(new Ship("Carrier",5,Color.BLUE));
        shipsList.add(new Ship("Battleship",4, Color.CYAN));
        shipsList.add(new Ship("Destroyer",3, Color.ORANGE));
        shipsList.add(new Ship("Submarine",3, Color.YELLOW));
        shipsList.add(new Ship("Patrol Boat",2, Color.PINK));



        init();
    }
    private void init() {
        //afisare mesaj de la server

        //adaug butonul de adaugare nava
        add(addShipBtn);
        // Creare un nou font
        Font newFont = new Font("default", Font.BOLD, 20);
        addShipBtn.setFont(newFont);
        addShipBtn.setPreferredSize(new Dimension(150, 70));
        addShipBtn.setBackground(Color.darkGray);
        addShipBtn.setForeground(Color.WHITE);//culoare text

        //configure listeners for all buttons
        addShipBtn.addActionListener(this::addShipOnBoard);


    }

    private void addShipOnBoard(ActionEvent e) {
        // Obțineți valorile din spinner
        String fromRowLetter = (String) settingsPlaceShip.fromRow.getValue();
        int fromRow = fromRowLetter.charAt(0) - 'A'; // convertire de la litera la nr
        int fromCol = (Integer) settingsPlaceShip.fromColumn.getValue() - 1; // scad 1 pt a obtine indexul matricii 0-9

        String toRowLetter = (String) settingsPlaceShip.toRow.getValue();
        int toRow = toRowLetter.charAt(0) - 'A';
        int toCol = (Integer) settingsPlaceShip.toColumn.getValue() - 1;

// if(validare) else afisez in JPanel
        // Obtine culoarea navei curente
        Color shipColor = shipsList.get(currentShipIndex).colorShip;

        // Seteaza culoarea celulelor pe baza culorii navei
        for (int i = fromRow; i <= toRow; i++) {
            for (int j = fromCol; j <= toCol; j++) {
                clientBoard.cellColorsShips[i][j] = shipColor;
            }
        }

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

        // Repictați panoul
        clientBoard.repaint();

        // Trece la urmatoarea navă
        currentShipIndex++;
        if (currentShipIndex < shipsList.size()) {
            Ship nextShip = shipsList.get(currentShipIndex);
            // Actualizeaza numele si lungimea afișate cu setText
            settingsPlaceShip.textNameShip.setText(nextShip.name);
            settingsPlaceShip.textSizeShip.setText(Integer.toString(nextShip.size));
        }
    }



}
