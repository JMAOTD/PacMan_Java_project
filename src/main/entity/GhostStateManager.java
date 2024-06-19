package main.entity;

import main.Interface.SubPanels.GamePanelMain;

/**
 * klasa odpowiadająca za zarządzanie stanami duchów, jak i w orginalnym pacManie te stany zmieniają się pomiędzy
 * bezpośrednim polowaniem na gracza i "łażeniem" po mapie bez celu, a konkretnie rzecz biorąc kręcąc się
 * w kątach mapy
 */
public class GhostStateManager implements Runnable{

    GamePanelMain gp;

    Thread ghostManager;



    public GhostStateManager(GamePanelMain gp){

        this.gp = gp;

    }

    /**
     * jeżeli w momencie utworzenia i wystartowania wątka istnieje identyczny to poprzedni zostaje przerwany
     */
    public void startCycle(){

        if(ghostManager != null && ghostManager.isAlive()) {

            ghostManager.interrupt();
            try{
                ghostManager.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }

        ghostManager = new Thread(this);
        ghostManager.start();

    }


    @Override
    public void run() {

        for(int i = 0; i < 4; i++){

            try {
                if(!Thread.currentThread().isInterrupted()) {

                    for(int j = 0; j < gp.ghosts.size(); j++) {

                        //jeżeli duch znajduje się w specjalnym stanie to aktualizowana jest
                        //tymczasowa zmienna, która nie ma wpływu na zachowanie ducha
                        if(gp.ghosts.get(j).isFrighten || gp.ghosts.get(j).isExitingFrighten
                                ||gp.ghosts.get(j).isEaten || gp.ghosts.get(j).justGotEaten) {
                            gp.ghosts.get(j).TMPisScatter = true;
                        }else{
                            gp.ghosts.get(j).isScatter = true;
                            gp.ghosts.get(j).TMPisScatter = true;

                            gp.UIP.refreshInfo(4, "Scatter");
                        }

                    }

                }
            }catch (RuntimeException e){}


            //odliczanie czasu do zmiany stanu
            try {
                if(!Thread.currentThread().isInterrupted()) {
                    try {

                        for(int j = 0; j < 7*60; j++) {
                            gp.gameFrame.checkInsidePaused();
                            gp.gameFrame.checkPaused();
                            Thread.sleep(1000/60);
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }catch (RuntimeException e){}


            //jeżeli wątek nie został przerwany to zmienia się stan duchów
            //oraz obracają się o 180 stopni
            try {
                if(!Thread.currentThread().isInterrupted()) {

                    for(int j = 0; j < gp.ghosts.size(); j++) {
                        gp.ghosts.get(j).isScatter = false;
                        gp.ghosts.get(j).TMPisScatter = false;

                        if(!gp.ghosts.get(j).isFrighten && !gp.ghosts.get(j).isExitingFrighten
                                && !gp.ghosts.get(j).isEaten && !gp.ghosts.get(j).justGotEaten) {

                            gp.ghosts.get(j).rotate180();

                        }
                    }

                }
            }catch (RuntimeException e){}

            //jeżeli mija już ostatni cykl to ustawia się stan polowania i potem nie jest już zmieniamy
            if(i == 3){

                try {
                    if(!Thread.currentThread().isInterrupted()) {

                        for(int j = 0; j < gp.ghosts.size(); j++) {

                            //jeżeli duch znajduje się w specjalnym stanie to aktualizowana jest
                            //tymczasowa zmienna, która nie ma wpływu na zachowanie ducha
                            if(gp.ghosts.get(j).isFrighten || gp.ghosts.get(j).isExitingFrighten
                                    ||gp.ghosts.get(j).isEaten || gp.ghosts.get(j).justGotEaten){

                                gp.ghosts.get(j).TMPisHunting = true;

                            }else{
                                gp.ghosts.get(j).isHunting = true;
                                gp.ghosts.get(j).TMPisHunting = true;
                                gp.UIP.refreshInfo(4, "Chase");
                            }

                        }

                    }
                }catch (RuntimeException e){}


            }else {

                try {
                    if(!Thread.currentThread().isInterrupted()) {

                        for(int j = 0; j < gp.ghosts.size(); j++) {

                            if(gp.ghosts.get(j).isFrighten || gp.ghosts.get(j).isExitingFrighten
                                    ||gp.ghosts.get(j).isEaten || gp.ghosts.get(j).justGotEaten){

                                gp.ghosts.get(j).TMPisHunting = true;

                            }else{

                                gp.ghosts.get(j).isHunting = true;
                                gp.ghosts.get(j).TMPisHunting = true;

                                gp.UIP.refreshInfo(4, "Chase");

                            }

                        }

                    }
                }catch (RuntimeException e){}

                //odliczenie czasu
                try {
                    if(!Thread.currentThread().isInterrupted()) {
                        try {

                            for(int j = 0; j < 20*60; j++) {
                                gp.gameFrame.checkInsidePaused();
                                gp.gameFrame.checkPaused();
                                Thread.sleep(1000/60);
                            }

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }catch (RuntimeException e){}


                try {
                    if(!Thread.currentThread().isInterrupted()) {

                        for(int j = 0; j < gp.ghosts.size(); j++) {
                            gp.ghosts.get(j).isHunting = false;
                            gp.ghosts.get(j).TMPisHunting = false;

                            if(!gp.ghosts.get(j).isFrighten && !gp.ghosts.get(j).isExitingFrighten
                                    && !gp.ghosts.get(j).isEaten && !gp.ghosts.get(j).justGotEaten) {

                                gp.ghosts.get(j).rotate180();

                            }
                        }

                    }
                }catch (RuntimeException e){}

            }

        }

    }

}
