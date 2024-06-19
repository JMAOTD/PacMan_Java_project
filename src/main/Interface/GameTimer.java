package main.Interface;

import main.Interface.SubPanels.UiPanel;

/**
 * Klasa odpowiedzialna za timer wyświetlany w czasie rozgrywki
 * Jest uruchamiany Thread który śpi jedną sekundę a następnie inkrementuje wartość secondsPassed
 * a następnie skonwertowana wartość jest przekazywana do UI
 */
public class GameTimer implements Runnable{

    Thread timerClock;
    public int secondsPassed;
    public UiPanel panel;
    GameFrame gameFrame;
    public boolean isStopped = false;
    int hours, minutes, seconds;
    String time;


    public GameTimer(UiPanel panel, GameFrame gameFrame){

        this.gameFrame = gameFrame;
        this.panel = panel;
        secondsPassed = 0;

    }

    public void startClock(){

        timerClock = new Thread(this);
        timerClock.start();

    }


    @Override
    public void run(){

        while(timerClock != null) {

            hours = secondsPassed / (3600);
            minutes = (secondsPassed - hours*(3600)) / 60;
            seconds = (secondsPassed - hours*3600 - minutes*60);

            time = (hours > 0 ? (hours > 9? hours : "0" + hours) : "00")
                    + ":"
                    + (minutes > 0? (minutes > 9? minutes : "0" + minutes) : "00")
                    + ":"
                    + (seconds > 0? (seconds > 9? seconds : "0" + seconds) : "00"
            );

            //wywołanie metody aktualizującej odpowiedni panel w UI
            panel.refreshInfo(2, time);

            try {

                for(int j = 0; j < 60; j++) {

                    gameFrame.checkPaused();
                    Thread.sleep(1000/60);

                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            secondsPassed++;

        }

    }

}
