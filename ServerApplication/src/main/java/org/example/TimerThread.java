package org.example;

import duringMatch.timer.TimeUpdateListener;

public class TimerThread extends Thread {
    private int time;
    private boolean running;
    public boolean timeOver = false;
    private final int playerId;
    private final TimeUpdateListener listener;

    public TimerThread(int time, int id, TimeUpdateListener listener) {
        this.time = time;
        this.playerId = id;
        this.listener = listener;
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
                }
            }
            timeOver = true;
            System.out.println("Time over for player " + playerId);
        } catch (InterruptedException e) {
            System.out.println("Timer was interrupted for player " + playerId);
        }
    }

    private void printTime() {
        System.out.println("trimit TIMER");
        if (listener != null) {
            listener.onTimeUpdate(String.valueOf(time));
        }
    }

    public synchronized boolean isStart() {
        return running;
    }
}
