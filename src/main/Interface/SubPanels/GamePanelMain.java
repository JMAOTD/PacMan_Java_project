package main.Interface.SubPanels;

import main.Interface.GameFrame;
import main.technical.ImageScaler;
import main.Interface.GameTimer;
import main.entity.PowerPalet;
import main.bonus.BonusManager;
import main.entity.*;
import main.inputs.KeyBinds;
import main.levels.Edible_Map;
import main.levels.Map;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Klasa która jest sercem całej gry, ponieważ odpowiada za powiązanie całej logiki gry w jedną całość
 * W tym panelu wyświetlana jest główna plansza z mapą, entity i wszystkimi elementami
 */
public class GamePanelMain extends JPanel implements Runnable {

    //podstawowy rozmiar chunka 16x16 pixeli
    public final int originalTileSize = 8;
    //ustawienie skalowania
    public int scale = 3;

    //skalowanie przeskalowanego chunka = 48x48 pixeli
    public final int tileSize = originalTileSize * scale;

    //ustawienie rozmiaru planszy w chunkach
    public int height;
    public int width;

    public Dimension dimension;

    //ustawienie rozmiaru planszy w pikselach
    public int screenWidth;
    public int screenHeight;

    //utworzenie wątku dla zarządzania czasem
    public Thread gameCore;

    //utworzenie instancji wszystkich objectów niezbędnych do prawidłowego
    //funkcjonowania logiki gry
    KeyBinds keyB = new KeyBinds(this);
    public GhostStateManager gsm;
    public PowerPalet pp;
    public Player player;
    public Blinky blinky;
    public Pinky pinky;
    public Inky inky;
    public Clyde clyde;
    public LinkedList<Ghost> ghosts;
    public GameFrame gameFrame;
    public GameTimer GT;
    public BonusTimersPanel BTP;
    public UiPanel UIP;
    public Map map;
    public Edible_Map eMap;
    public BonusManager bc;
    public ImageScaler IS;

    //Deklaracja zmiennej FPS - będzie wyznaczać ilośc klatek na sekundę
    final int FPS = 60;

    //czas pomiędzy klatkami dla ustawionegoo FPS w nanosekundach
    double drawInterwal = 1_000_000_000 / FPS;

    /**
     * zmienna currentlvl reprezentuje poziom na którym obecnie znajduje się gracz
     * po zjedzeniu wszystkich peletek gra jest resetowana i wartość zwiększa się o 1
     */
    public int currentlvl;


    //konstruktor
    public GamePanelMain(GameFrame gameFrame, int SETHeight){

        this.gameFrame = gameFrame;

        setSizes(SETHeight);
        dimension = new Dimension(screenWidth, screenHeight);
        this.setPreferredSize(dimension);
        setSize(dimension);

        this.addKeyListener(keyB);
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.setLayout(null);

        //inicjacja objektów
        IS = new ImageScaler();
        map = new Map(this);
        player = new Player(this, keyB);
        blinky = new Blinky(this);
        pinky = new Pinky(this);
        inky = new Inky(this);
        clyde = new Clyde(this);
        ghosts = new LinkedList<>();
        ghosts.add(blinky);
        ghosts.add(pinky);
        ghosts.add(inky);
        ghosts.add(clyde);

        //wyczyszczenie stanów ghostów
        for(int i = 0; i < ghosts.size(); i++) ghosts.get(i).clearStates();
        for(int i = 0; i < ghosts.size(); i++) ghosts.get(i).SetLabel();

        gsm = new GhostStateManager(this);
        pp = new PowerPalet(this);
        bc = new BonusManager(this);
        map.drawMapLabel();
        eMap = new Edible_Map(this);

        //ustawienie początkowej wartości numeru poziomu
        currentlvl = 1;

    }

    /**
     * ustawienie rozmiaru planszy na podstawie argumentu
     * owy rozmiar definuje mapę, ulokowanie peletek, początkowe pozycje entity
     * @param SETHeight
     */
    public void setSizes(int SETHeight){

        switch(SETHeight){
            case 12: height = 12; width = 16;
            break;
            case 15: height = 15; width = 20;
            break;
            case 18: height = 18; width = 24;
            break;
            case 21: height = 21; width = 28;
            break;
            case 31: height = 31; width = 28;
            break;
        }

        screenWidth = width * tileSize;
        screenHeight = height * tileSize;

    }

    /**
     * metoda przypisująca istniejący BonusTimersPanel do obecnego GamePanela
     * @param BTP BonusTimersPanel
     */
    public void setBTP(BonusTimersPanel BTP){

        this.BTP = BTP;

    }

    /**
     * metoda przypisująca istniejący UiPanel do obecnego GamePanela
     * @param UIP UiPanel
     */
    public void setUIP(UiPanel UIP){

        this.UIP = UIP;
        GT = new GameTimer(UIP, this.gameFrame);
        UIP.refreshInfo(3, Integer.toString(currentlvl));

    }

    /**
     * metoda uruchamiająca wątek gameCore oraz inne kluczowe wątki
     */
    public void startGame(){

        gameCore = new Thread(this);
        gameCore.start();
        gsm.startCycle();
        bc.startCycle();
        GT.startClock();

    }

    /**
     * metoda przywracająca stan początkowy po zjedzeniu wszystkich peletek
     */
    public void nextLvl(){

        currentlvl++;
        UIP.refreshInfo(3, Integer.toString(currentlvl));
        resetEntities();
        eMap.setEdiblesTiles();
        eMap.initializeFoodMatrix();
        revalidate();
        repaint();

    }

    /**
     * metoda resetująca pozycje entity w razie utracenia przez gracza życia
     */
    public void resetEntities() {

        bc.cleanMatrix();

        blinky.setDefault(true);
        pinky.setDefault(true);
        clyde.setDefault(true);
        inky.setDefault(true);

        for (Ghost ghost : ghosts) ghost.speedMultiplyer = 1;

        player.setDefault();
        gsm.startCycle();
        UIP.refreshLivesCounter();

        revalidate();
        repaint();


    }

    @Override
    public void run() {

        //pętla oparta na istnieniu wątka
        while (gameCore != null) {

            if (player.lives < 1) {

                gameFrame.pauseGame();
                gameFrame.gameOver();

            }

            //weryfikacja czy nadal wątek ma klucz
            try {
                gameFrame.checkPaused();
                gameFrame.checkInsidePaused();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //wyliczenie czasu początku następnego cyklu
            double nextDrawTime = System.nanoTime() + drawInterwal;


            update();
            drawLabels();


            //jeżeli jeden z duchów zostaje zjedzony to gra zamraża się i wyświetlana jest
            //ilość punktów zdobytych za zjedzenie tego ducha
            //gra musi być zapauzowana żeby sprite bonusu pozostał wystarczająco długo widoczny
            for (int i = 0; i < ghosts.size(); i++) {

                if (ghosts.get(i).justGotEaten) {

                    gameFrame.insidePauseGame();

                    for (int j = 0; j < 60; j++) {

                        try {

                            gameFrame.checkPaused();
                            Thread.sleep(1000 / 60);

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    gameFrame.insideResumeGame();

                    ghosts.get(i).justGotEaten = false;

                }
            }


            try {

                //obliczenie pozostałego czasu
                double remainingTime = nextDrawTime - System.nanoTime();

                //konwertacja pozostałego czasu na milisekundy - dzielimy przez 1_000_000
                remainingTime /= 1_000_000;

                if(remainingTime < 0) remainingTime = 0;

                Thread.sleep((long)remainingTime);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }

    }

    /**
     * metoda która aktualizuje dane entity
     */
    public void update() {

        for (int i = 0; i < ghosts.size(); i++) {
            if (player != null) {
                ghosts.get(i).update();
            }
        }
        if (player != null) {
            player.update();
        }

    }

    /**
     * metoda wywołująca metody aktualizące położenie lejbeli gracza i duchów
     */
    public void drawLabels(){

        for(int i = 0; i < ghosts.size(); i++) {
            if (player != null) {
                ghosts.get(i).drawLabel();
            }
        }
        if (player != null) {
            player.drawLabel();
        }

    }

}
