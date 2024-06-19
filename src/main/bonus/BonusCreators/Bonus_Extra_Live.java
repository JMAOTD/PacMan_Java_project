package main.bonus.BonusCreators;

import main.Interface.SubPanels.GamePanelMain;

/**
 * klasa odpowiadająca za utworzenie dodatkowego życia dla gracza,
 * jeżeli gracz zje dany bonus to otrzyma dodatkowe życie, pod warunkiem że posiada mniej niż 3 życia
 */
public class Bonus_Extra_Live extends BonusCreator implements Runnable{


    GamePanelMain gp;

    Thread BonusExtraLive;



    public Bonus_Extra_Live(GamePanelMain gp, int yIndex, int xIndex){

        this.gp = gp;
        timeAvailableToPick = 5;
        this.yIndex = yIndex;
        this.xIndex = xIndex;

    }

    public void CreateBonus(){

        BonusExtraLive = new Thread(this);
        BonusExtraLive.start();

    }


    @Override
    public void run() {
        AvailableToPickTimer();
    }

    private void AvailableToPickTimer(){

        //włączenie odliczana 10 sekund na zjedzenie ulepszenia
        try {
            for(int j = 0; j < timeAvailableToPick*60; j++) {
                gp.gameFrame.checkInsidePaused();
                gp.gameFrame.checkPaused();
                Thread.sleep(1000/60);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // jeżeli po 10 sekundach gracz nie "zje" ulepszenia to dana komórka jest resetowana
        if(gp.bc.bonusMap[yIndex][xIndex] == 4) {
            gp.bc.bonusMap[yIndex][xIndex] = 0;

            gp.bc.clearLabel(yIndex, xIndex);

        }

    }

}