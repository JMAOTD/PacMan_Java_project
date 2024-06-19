package main.bonus.BonusApplyers;

import main.Interface.SubPanels.GamePanelMain;

/**
 * Klasa odpowiadająca za aplikowanie efekty dla gracza w wypadku zjedzenia bonusu
 * efekt - gracz zyskuje podwójną ilość punktów naliczanych mu w trakcie trwania efektu
 */
public class Bonus2xPointsApplyer extends BonusAplyer implements Runnable, BonusApplyer_I{


    public Bonus2xPointsApplyer(GamePanelMain gp){

        this.gp = gp;
        timeSpan = 5;

    }


    public void ApplyEffect(){

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
        gp.BTP.loadBar(2, 1);

    }

    @Override
    public void run() {

        try {
            if(!Thread.currentThread().isInterrupted()) {

                //jeżeli obecny wątek nie był przerwany to pasek łądowania efektu rozpoczyna
                //proces odliczania
                gp.BTP.loadBar(2, 11);
                gp.player.is2xPointsActive = true;
                gp.player.scoreMultipliyer = 2;

            }
        }catch (RuntimeException e){}


        //iteracyjnie pasek ładowania efekty zmniejsza się jak i odliczanie do końca efektu dobiega końca
        for(int i = 0; i < 10; i ++){

            try {
                if(!Thread.currentThread().isInterrupted()) {
                    try {

                        for(int j = 0; j < timeSpan/10*60; j++) {
                            gp.gameFrame.checkPaused();
                            gp.gameFrame.checkInsidePaused();
                            Thread.sleep(1000/60);
                        }
                        gp.BTP.loadBar(2, 10 - i);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }else break;

            }catch (RuntimeException e){}

        }

        //jeżeli gracz ponownie otrzymał bonus to wcześniejszy wątek nie wyłącza działania bonusu
        try {
            if(!Thread.currentThread().isInterrupted()) {

                gp.player.is2xPointsActive = false;
                gp.player.scoreMultipliyer = 1;

            }
        }catch (RuntimeException e){}

    }

}