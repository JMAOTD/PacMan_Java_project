package main.entity;

import main.Interface.SubPanels.GamePanelMain;

/**
 * Klasa odpowiadająca za wprowadzenie ghostów w stan niebieskich, losowo wybierających kierunki
 * i wystraszonych duchów
 */
public class PowerPalet implements Runnable{

    GamePanelMain gp;
    Thread BlueMadness;



    public PowerPalet(GamePanelMain gp){

        this.gp = gp;

    }

    /**
     * metoda tworząca nowy wątek, a jeżeli w momencie wywołania metody wątek istniał to
     * zostaje on przerwany ponieważ stary wątek nie powinien kasować efetku zanim nowy wątek
     * jeszcze odlicza czas do zakończenia efektu
     */
    public void startCycle(){

        if(BlueMadness != null && BlueMadness.isAlive()) {

            BlueMadness.interrupt();
            try{
                BlueMadness.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }

        BlueMadness = new Thread(this);
        BlueMadness.start();

    }

    @Override
    public void run() {

        //odbywa się iteracja przez wszystkich duchów
        //i jeżeli znajdują się w jednym z dwóch dozwolonych stanów
        //to ich stan zmienia się na niebiesko wystraszonych
        for (int i = 0; i < gp.ghosts.size(); i++) {

            if(!gp.ghosts.get(i).isEaten && !gp.ghosts.get(i).justGotEaten) {

                gp.ghosts.get(i).rotate180();
                gp.ghosts.get(i).isFrighten = true;
                gp.ghosts.get(i).isScatter = false;
                gp.ghosts.get(i).isHunting = false;

            }

        }

        //w UI pojawia sie informacja że duchy są wystraszone
        gp.UIP.refreshInfo(4, "Frighten");

        try {
            if(!Thread.currentThread().isInterrupted()) {
                try {

                    //odliczenie 9 sekund
                    for(int j = 0; j < 9*60; j++) {
                        gp.gameFrame.checkInsidePaused();
                        gp.gameFrame.checkPaused();
                        Thread.sleep(1000/60);
                    }


                    for (int i = 0; i < gp.ghosts.size(); i++) {

                        //jeżęli duch nadal jest w stanie Frighten
                        //to jego stan zmienia sie na exitingFrighten
                        //wtedy jego sprajt zaczyna migać i gracz wie
                        //że duchy powrócą do normalnych stanów
                        if(gp.ghosts.get(i).isFrighten) {

                            gp.ghosts.get(i).isFrighten = false;
                            gp.ghosts.get(i).isExitingFrighten = true;

                        }

                    }

                    //w UI pojawia sie informacja że duchy są wystraszone
                    gp.UIP.refreshInfo(4, "Returning");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }catch (RuntimeException e){}



        try {
            if(!Thread.currentThread().isInterrupted()) {
                try {

                    //odliczanie trzech sekund
                    for(int j = 0; j < 3*60; j++) {
                        gp.gameFrame.checkInsidePaused();
                        gp.gameFrame.checkPaused();
                        Thread.sleep(1000/60);
                    }

                    for(int i = 0; i < gp.ghosts.size(); i++) {

                        //jeżeli duch jest w stanie ExitingFrighten
                        //to obraca się o 180 stopni i jest przypisywany mu stan
                        //który obecnie powinnien mieć zgodnie z GhostStateManager
                        if(gp.ghosts.get(i).isExitingFrighten) {

                            gp.ghosts.get(i).isExitingFrighten = false;
                            gp.ghosts.get(i).assignTMP();
                            gp.ghosts.get(i).rotate180();

                            //aktualizacja danych w UI
                            if(gp.ghosts.get(i).TMPisHunting){
                                gp.UIP.refreshInfo(4, "Chase");
                            } else {
                                gp.UIP.refreshInfo(4, "Scutter");
                            }

                        }

                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }catch (RuntimeException e){}

    }

}
