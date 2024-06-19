package main.Interface.MainPanels;

import main.Interface.GameFrame;
import main.Interface.SubPanels.BonusTimersPanel;
import main.Interface.SubPanels.GamePanelMain;
import main.Interface.SubPanels.UiPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa odpowiadająca za połączenie kluczowych dla gry paneli w jeden
 */
public class GamePanel extends JPanel implements MainPanels_I {

    public GameFrame gameFrame;

    //panel z planszą
    public GamePanelMain gp;

    //panel z timerami bonusów
    public BonusTimersPanel btp;

    //panel z interfejsem
    public UiPanel uip;

    public Dimension GamePanelDimension;


    public GamePanel(GameFrame gameFrame, int height){

        this.gameFrame = gameFrame;
        gp = new GamePanelMain(gameFrame, height);
        btp = new BonusTimersPanel(gp);
        gp.setBTP(btp);
        uip = new UiPanel(gp);
        gp.setUIP(uip);
        setUpPanel();
        gp.startGame();
        gp.requestFocusInWindow();

    }

    /**
     * metoda ustawiająca rozmiar panelu oraz położenie wszystkich zawartych w nim komponentów
     */
    public void setUpPanel(){

        this.setLayout(new BorderLayout());

        int PanelHeight = gp.getHeight() + btp.getHeight();
        int PanelWidth = Math.max(btp.getWidth(), gp.getWidth() + uip.getWidth());

        GamePanelDimension = new Dimension(PanelWidth, PanelHeight);

        //Ustawienia panelu
        this.setPreferredSize(GamePanelDimension);
        this.setMinimumSize(GamePanelDimension);
        this.setSize(GamePanelDimension);

        //Dodanie komponentów do panelu
        this.add(gp, BorderLayout.CENTER);
        this.add(uip, BorderLayout.EAST);
        this.add(btp, BorderLayout.SOUTH);

    }

}
