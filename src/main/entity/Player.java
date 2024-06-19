package main.entity;

import main.high_scores.Score;
import main.inputs.KeyBinds;
import main.Interface.SubPanels.GamePanelMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Klasa odpowiadająca za logikę oraz graficzną prezentację Entity gracz
 */
public class Player extends Entity implements I_Entity{


    // utowrzenie zmiennej wyznaczającej znak, potrzebnej do algorytmu zmiany numeru sprajta
    private int sighn;
    public Image u1i, u2i, u3i, l1i, l2i, l3i, d1i, d2i, d3i, r1i, r2i, r3i;
    public int scoreMultipliyer;
    public Score score;
    //zmienne odpowiadające za ilośc żyć gracza, oraz za ilość punktów
    //przyznawanych za zjedzenie kolejnych duszków kiedy są wystraszone
    public int lives, bonusCounter;
    //stany gracza, potrzebne do bonusów
    public boolean isImunityActive, is2xPointsActive, is2xSpeedActive, isGhostFreezeActive;
    public JLabel label;


    //konstruktor
    public Player(GamePanelMain gp, KeyBinds keyB){

        score = new Score(0);
        this.gp = gp;
        this.keyB = keyB;

        this.getEntityImage();
        label = new JLabel();

        //stworzenie hitBoxa gracza
        HitBox = new Rectangle(x, y, gp.tileSize, gp.tileSize);

        this.setDefault();

        //ustawienie domku duchów dla gracza jako solidny blok
        gp.map.isSolidSet.replace(3, true);

        lives = 3;
        SetLabel();

    }

    /**
     * ustawia dane playera na domyślne
     */
    public void setDefault(){

        switch(gp.height){

            case 12:
                x = (int) (7.5 * gp.tileSize);
                y = 8 * gp.tileSize;
                break;
            case 15:
                x = (int) (9.5 * gp.tileSize);
                y = 12 * gp.tileSize;
                break;
            case 18:
                x = (int) (11.5 * gp.tileSize);
                y = 16 * gp.tileSize;
                break;
            case 21:
                x = (int) (13.5 * gp.tileSize);
                y = 16 * gp.tileSize;
                break;
            case 31:
                x = (int) (13.5 * gp.tileSize);
                y = 23 * gp.tileSize;
                break;

        }

        speed = 3;

        sighn = -1;

        direction = ' ';
        vector = new int []{1, 0};

        FPSCounter = 0;
        spriteCounter = 1;
        hardCondition = false;

        this.image = r1;

        //domyślne ustawienie na false
        isMoving = false;
        isColiding = false;

        bonusCounter = 0;

        scoreMultipliyer = 1;

        HitBox.x = x;
        HitBox.y = y;

        clearEffects();

    }

    /**
     * metoda zerująca wszystkie efekty gracza
     */
    public void clearEffects(){

        isGhostFreezeActive = false;

        is2xPointsActive = false;

        scoreMultipliyer = 1;
        is2xSpeedActive = false;

        speedMultiplyer = 1;
        isImunityActive = false;

    }

    /**
     * pobranie i przypisanie obrazów
     */
    public void getEntityImage(){

        try {

            u1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/u1.png")), 2);
            u2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/u2.png")), 2);
            u3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/u3.png")), 2);

            l1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/l1.png")), 2);
            l2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/l2.png")), 2);
            l3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/l3.png")), 2);

            d1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/d1.png")), 2);
            d2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/d2.png")), 2);
            d3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/d3.png")), 2);

            r1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/r1.png")), 2);
            r2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/r2.png")), 2);
            r3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/r3.png")), 2);

            u1i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/u1i.png")), 2);
            u2i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/u2i.png")), 2);
            u3i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/u3i.png")), 2);

            l1i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/l1i.png")), 2);
            l2i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/l2i.png")), 2);
            l3i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/l3i.png")), 2);

            d1i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/d1i.png")), 2);
            d2i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/d2i.png")), 2);
            d3i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/d3i.png")), 2);

            r1i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/r1i.png")), 2);
            r2i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/r2i.png")), 2);
            r3i = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Player/r3i.png")), 2);


        }catch(IOException e){}

    }

    /**
     * metoda odświażająca pozycję entity gracz
     */
    public void update(){



        //weryfikacja czy gracz znajduje się w tym samym tile co bonus
        getBonus(hardCondition);

        //zmienna weryfikuje czy entity jest wycentroweana na siatce
        hardCondition = HitBox.x % gp.tileSize == 0 && HitBox.y % gp.tileSize == 0;


        //jeżeli entity jest wyrównana względem X to może porószać się tylko góra - dół
        if(HitBox.x % gp.tileSize == 0 && vector[0] == 0)
            VerticalCheck();

        //a jeżeli względem Y to tylko lewo i prawo
        else if (HitBox.y % gp.tileSize == 0 && vector[1] == 0)
            HorizontalCheck();

        //jeżeli entity jest wyrónana względem (X i Y) to może zmienić kierunek w dowolną stronę
        if (hardCondition) IntersectionCheck();

        //weryfikacja możliwości poruszania się w obecnym kierunku
        isMoving = !ColisionCheckUniversal(direction);

        //weryfikacja czy gracz znajduje się w tym samy tile co peletka
        earnPoints(hardCondition);

        //jeżeli wartość isMoving == true to wyłowujemy metodę zmieniającą położenie
        if(isMoving) move();


    }

    /**
     * metoda naliczania punktów za zjedzenie peletek
     * @param allighned boolean czy gracz jest wyrównany na siatce 24x24
     */
    public void earnPoints(boolean allighned){

        if(allighned){

            int XCord = correctCordsX(HitBox.x / gp.tileSize);
            int YCord = correctCordsY(HitBox.y / gp.tileSize);

            if(gp.eMap.EdiblesTilesLayout[YCord][XCord] == 1){
                score.score += 10 * scoreMultipliyer;
                gp.eMap.EdiblesTilesLayout[YCord][XCord] = 0;

                gp.eMap.clearLabel(YCord, XCord);

                gp.UIP.refreshInfo(0, Integer.toString(score.score));
            }

            if(gp.eMap.EdiblesTilesLayout[YCord][XCord] == 2){
                score.score += 50 * scoreMultipliyer;
                gp.eMap.EdiblesTilesLayout[YCord][XCord] = 0;

                gp.eMap.clearLabel(YCord, XCord);

                gp.pp.startCycle();
                bonusCounter = 0;

                gp.UIP.refreshInfo(0, Integer.toString(score.score));
            }

        }

    }

    /**
     * metoda aplikowania bonusów
     * @param allighned boolean czy gracz jest wyrównany na siatce 24x24
     */
    public void getBonus(boolean allighned){

        if(allighned){

            int XCord = correctCordsX(HitBox.x / gp.tileSize);
            int YCord = correctCordsY(HitBox.y / gp.tileSize);

            if(gp.bc.bonusMap[YCord][XCord] != 0){

                gp.bc.BonusApplyer(YCord, XCord);

            }

        }

    }

    /**
     * metoda aktualizująca lejbel gracza
     */
    public void drawLabel(){

        //pętla iterująca co 10 klatek numer sprita w pętli 1 -> 2 -> 3 -> 2 -> 1 -> 2...
        if(isMoving) {
            FPSCounter++;
            if (FPSCounter % 10 == 0 && FPSCounter != 0) {
                if (FPSCounter == 10 || FPSCounter == 30) {
                    sighn *= -1;
                }
                if (FPSCounter == 40) {
                    FPSCounter = 0;
                }
                spriteCounter += 1 * sighn;
            }
        }


        //pętla switch zmieniająca sprite w zależności od kierunku entity i numeru sprita
        //jeżeli bonus imunitetu jest aktywny to będą to zółte sprajty w kształcie duszka
        if(!isImunityActive) {

            switch (direction) {

                case 'u':
                    switch (spriteCounter) {
                        case 1: image = u1;break;
                        case 2: image = u2;break;
                        case 3: image = u3;break;
                    }
                    break;
                case 'l':
                    switch (spriteCounter) {
                        case 1: image = l1;break;
                        case 2: image = l2;break;
                        case 3: image = l3;break;
                    }
                    break;
                case 'd':
                    switch (spriteCounter) {
                        case 1: image = d1;break;
                        case 2: image = d2;break;
                        case 3: image = d3;break;
                    }
                    break;
                case 'r':
                    switch (spriteCounter) {
                        case 1: image = r1;break;
                        case 2: image = r2;break;
                        case 3: image = r3; break;
                    }
                    break;

            }

        }else{

            switch (direction) {

                case 'u':
                    switch (spriteCounter) {
                        case 1: image = u1i;break;
                        case 2: image = u2i;break;
                        case 3: image = u3i;break;
                    }
                    break;
                case 'l':
                    switch (spriteCounter) {
                        case 1: image = l1i;break;
                        case 2: image = l2i;break;
                        case 3: image = l3i;break;
                    }
                    break;
                case 'd':
                    switch (spriteCounter) {
                        case 1: image = d1i;break;
                        case 2: image = d2i;break;
                        case 3: image = d3i;break;
                    }
                    break;
                case 'r':
                    switch (spriteCounter) {
                        case 1: image = r1i;break;
                        case 2: image = r2i;break;
                        case 3: image = r3i; break;
                    }
                    break;

            }

        }

        label.setIcon(new ImageIcon(image));
        label.setLocation(HitBox.x - gp.tileSize/4, HitBox.y - gp.tileSize/4);

    }

    /**
     * metoda stworzenia i dodania lejbela
     */
    public void SetLabel(){

        label.setIcon(new ImageIcon(image));
        label.setLocation(HitBox.x - gp.tileSize/4, HitBox.y - gp.tileSize/4);
        label.setSize((int)(gp.tileSize * 1.5), (int)(gp.tileSize * 1.5));

        gp.add(label);
    }

}