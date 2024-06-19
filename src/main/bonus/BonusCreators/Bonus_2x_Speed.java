package main.bonus.BonusCreators;

import main.Interface.SubPanels.GamePanelMain;

/**
 * klasa odpowiadająca za utworzenie bonusu podwójnej prędkości gracza,
 * na czas działąnia bonusu gracz porusza się z podwójną prędkością
 */
public class Bonus_2x_Speed extends BonusCreator implements Runnable{

    GamePanelMain gp;
    Thread Bonus2xSpeed;



    public Bonus_2x_Speed(GamePanelMain gp, int yIndex, int xIndex){

        this.gp = gp;
        timeAvailableToPick = 10;
        this.yIndex = yIndex;
        this.xIndex = xIndex;

    }

    public void CreateBonus(){

        Bonus2xSpeed = new Thread(this);
        Bonus2xSpeed.start();

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
        if(gp.bc.bonusMap[yIndex][xIndex] == 3) {
            gp.bc.bonusMap[yIndex][xIndex] = 0;

            gp.bc.clearLabel(yIndex, xIndex);

        }

    }

}