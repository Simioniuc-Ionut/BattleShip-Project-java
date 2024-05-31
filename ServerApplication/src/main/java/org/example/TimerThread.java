package org.example;

public class TimerThread extends Thread {
    private int time;
    private boolean running;
    public boolean timeOver = false;
    private final int playerId;

    public TimerThread(int time, int id) {
        this.time = time;
        this.playerId = id;

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
            System.out.println("Timer thread running for player " + playerId + " with time " + time);
            while (time > 0) {
                Thread.sleep(1000);
                if (running) {
                    time--;
                    System.out.println("Time remaining for player " + playerId + ": " + time);
                    printTime();
                    System.out.println("after print");
                }
            }
            timeOver = true;
            System.out.println("Time over for player " + playerId);
        } catch (InterruptedException e) {
            System.out.println("Timer was interrupted for player " + playerId);
        }
    }

    private void printTime() {
        System.out.println("trimit TIMER" );
      //(String.valueOf(time));

    }



    public synchronized boolean isStart() {
        return running;
    }
}
