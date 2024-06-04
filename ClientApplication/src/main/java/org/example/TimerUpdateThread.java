package org.example;

import duringMatch.MainFrameFour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TimerUpdateThread extends Thread {
    private final Socket timeSocket;
    private final MainFrameFour mainFrame;
    private BufferedReader in;

    public TimerUpdateThread(Socket timeSocket, MainFrameFour mainFrame) {
        this.timeSocket = timeSocket;
        this.mainFrame = mainFrame;

        try {
            this.in = new BufferedReader(new InputStreamReader(timeSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                if (message.equals("TIME_OVER")) {
                    // Timer is over, handle accordingly
                    mainFrame.getTimeGamePanel().updateTime("00:00");
                   // System.out.println("sunt in time over in timerUpdatThread");
                    //oprim jocul
                    break;
                } else {
                    //int timeRemaining = Integer.parseInt(message);
                    System.out.println("timer is " + message);
                    mainFrame.getTimeGamePanel().updateTime(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (timeSocket != null) {
                    timeSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
