package main.bonus.BonusApplyers;

import main.Interface.SubPanels.GamePanelMain;

/**
 * Klasa odpowiadająca za aplikowanie efekty dla gracza w wypadku zjedzenia bonusu
 * efekt - jeżeli gracz posiada mniej niż 3 życia to otrzymuje jedno ekstra
 */
public class BonusExtraLiveApplyer extends BonusAplyer{



    public BonusExtraLiveApplyer(GamePanelMain gp){

        this.gp = gp;

    }

    public void ApplyEffect(){

        GotEatenByPlayer();

    }

    private synchronized void GotEatenByPlayer(){

        gp.bc.bonusMap[yIndex][xIndex] = 0;
        gp.bc.clearLabel(yIndex, xIndex);
        //jeżeli player nie posiada maksymalnej ilości żyć to otrzymuje jedno ekstra
        if(gp.player.lives < 3) {
            gp.player.lives++;
            gp.UIP.refreshLivesCounter();
        }

    }


}
