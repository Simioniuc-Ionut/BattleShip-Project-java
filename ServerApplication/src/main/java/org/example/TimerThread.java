package org.example;

//public class TimerThread extends Thread {
//    private int time;
//    private boolean running;
//    public boolean timeOver = false;
//    private final int playerId;
//
//    public TimerThread(int time, int id) {
//        this.time = time;
//        this.playerId = id;
//
//    }
//
//    public synchronized void startTimer() {
//        running = true;
//        System.out.println("Timer started for player " + playerId);
//    }
//
//    public synchronized void pauseTimer() {
//        running = false;
//        System.out.println("Timer paused for player " + playerId);
//    }
//
//    @Override
//    public void run() {
//        try {
//           // System.out.println("Timer thread running for player " + playerId + " with time " + time);
//            while (time > 0) {
//                Thread.sleep(1000);
//                if (running) {
//                    time--;
//                    System.out.println("Time remaining for player " + playerId + ": " + time);
//                    printTime();
//                    //System.out.println("after print");
//                }
//            }
//            timeOver = true;
//            System.out.println("Time over for player " + playerId);
//        } catch (InterruptedException e) {
//            System.out.println("Timer was interrupted for player " + playerId);
//        }
//    }
//
//    private void printTime() {
//        System.out.println("trimit TIMER" );
//      //(String.valueOf(time));
//
//    }
//
//
//
//    public synchronized boolean isStart() {
//        return running;
//    }
//}


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TimerThread extends Thread {
    private int minutes;
    private int seconds;

    private boolean running;
    private final int playerId;
    private PrintWriter out;

    private GameServer gameServer;
    private ClientThread player;
    public TimerThread(int minutes, int seconds, int id,  PrintWriter out,GameServer gameServer,ClientThread player) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.playerId = id;
        this.out = out;
        this.player=player;
        this.gameServer=gameServer;

    }

    public synchronized void startTimer() {
        running = true;
        System.out.println("Timer started for player " + playerId);
    }

    public synchronized void pauseTimer() {
        running = false;
        System.out.println("Timer paused for player " + playerId);
    }

    @Override
    public void run() {
        try {
            while (minutes > 0 || seconds > 0) {
                Thread.sleep(1000);
                if (running) {

                    System.out.println("Time remaining for player " + playerId + ": " + getTimeRemaining());
                    printTime(); // Trimite timpul rămas către client
                    decrementTime();
                }
            }
            running=false;
            System.out.println("Time over for player " + playerId);
            out.println("TIME_OVER");

            //Notificam serverul ca jocul a luat sfarsit
            gameServer.makeGameOver(player.getOpponent());

        } catch (InterruptedException e) {
            System.out.println("Timer was interrupted for player " + playerId);
        }
    }
    private void decrementTime() {
        if (seconds > 0) {
            seconds--;
        } else {
            if (minutes > 0) {
                minutes--;
                seconds = 59;
            }
        }
    }
    public String getTimeRemaining() {
        return String.format("%02d:%02d", minutes, seconds);
    }
    private void printTime() {
        out.println(getTimeRemaining());
        // Trimite timpul rămas către clientul grafic
    }
    public synchronized boolean isStart() {
        return running;
    }
}
