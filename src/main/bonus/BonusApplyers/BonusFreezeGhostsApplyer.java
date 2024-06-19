package main.bonus.BonusApplyers;

import main.Interface.SubPanels.GamePanelMain;

/**
 * Klasa odpowiadająca za aplikowanie efekty dla gracza w wypadku zjedzenia bonusu
 * efekt - duchy przestają ruszać się
 */
public class BonusFreezeGhostsApplyer extends BonusAplyer implements Runnable, BonusApplyer_I{


    public BonusFreezeGhostsApplyer(GamePanelMain gp){

        this.gp = gp;
        timeSpan = 5;

    }


    public void ApplyEffect(){

        System.out.println();
        resetEffect();

        effectApplyer = new Thread(this);
        effectApplyer.start();

    }

    public void resetEffect(){

        //jeżeli gracz ponownie zjadł bonus to poprzedni wątek zostaje przerwany żeby czas zaczął liczyć się od nowa
        if(effectApplyer != null && effectApplyer.isAlive()) {

            effectApplyer.interrupt();
            try{
                effectApplyer.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }

        //pasek ładowania jest zerowany
        gp.BTP.loadBar(3, 1);

    }


    @Override
    public void run() {

        try {
            if(!Thread.currentThread().isInterrupted()) {

                //jeżeli obecny wątek nie był przerwany to pasek łądowania efektu rozpoczyna
                //proces odliczania
                gp.BTP.loadBar(3, 11);
                gp.player.isGhostFreezeActive = true;
                for(int i = 0; i < gp.ghosts.size(); i++){
                    gp.ghosts.get(i).isMoving = false;
                }

            }
        }catch (RuntimeException e){}


        //iteracyjnie pasek ładowania efekty zmniejsza się jak i odliczanie do końca efektu dobiega końca
        for(int i = 0; i < 10; i ++){

            try {
                if(!Thread.currentThread().isInterrupted()) {
                    try {

                        for(int j = 0; j < timeSpan/10*60; j++) {
                            gp.gameFrame.checkInsidePaused();
                            gp.gameFrame.checkPaused();
                            Thread.sleep(1000/60);
                        }

                        gp.BTP.loadBar(3, 10 - i);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }else break;

            }catch (RuntimeException e){}

        }

        //jeżeli gracz ponownie otrzymał bonus to wcześniejszy wątek nie wyłącza działania bonusu
        try {
            if(!Thread.currentThread().isInterrupted()) {

                gp.player.isGhostFreezeActive = false;

                for(int i = 0; i < gp.ghosts.size(); i++){
                    gp.ghosts.get(i).isMoving = true;
                }

            }
        }catch (RuntimeException e){}

    }

}