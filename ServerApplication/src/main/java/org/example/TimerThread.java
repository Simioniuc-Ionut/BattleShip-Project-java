package org.example;

public class TimerThread extends Thread {
    private int time;
    private boolean start;
    public boolean timeOver = false;
    private final int playerId;

    public TimerThread(int time, int id) {
        this.time = time;
        this.playerId = id;
    }

    public void startTimer() {

        start = true;
        System.out.println("L am facut true " + start);
    }

    public void pauseTimer() {
        start = false;
    }

    @Override
    public void run() {
        try {
            System.out.println("Sunt in run + timp " + time);
            while (time > 0) {
                //System.out.println(start);
                Thread.sleep(1000);
                if (start) {
                    //System.out.println("ceva inainte ");
                    //Thread.sleep(1000);
                    //System.out.println("ceva dupa ");
                    time--;
                    // System.out.println(time);
                    printTime();
                }
            }
            timeOver = true;
        } catch (InterruptedException e) {
            System.out.println("timer was interrupted" + playerId);
            //e.printStackTrace();
        }
    }

    private void printTime() {
        //System.out.println("Player : " + playerId + "Time remaining: "+time+" seconds");
    }

    public boolean isStart() {
        return start;
    }
}
