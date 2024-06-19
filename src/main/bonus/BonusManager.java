package main.bonus;

import main.bonus.BonusApplyers.*;
import main.bonus.BonusCreators.*;
import main.entity.Ghost;
import main.Interface.SubPanels.GamePanelMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Klasa odpowiadająca za tworzenie, i zarządzanie bonusami
 */
public class BonusManager extends JPanel implements Runnable{

    public HashMap<Integer, Image> BonusTiles;
    public Image B2xP, B2xS, BEL, BI, BFG, image, empty;
    public ImageIcon iconB2xP, iconB2xS, iconBEL, iconBI, iconBFG, iconEmpty;
    Random random;
    GamePanelMain gp;
    int yIndex,xIndex;
    Thread BonusCreator;
    Bonus_Imunity bi;
    Bonus_2x_Points b2xp;
    Bonus_2x_Speed b2xs;
    Bonus_Extra_Live bel;
    Bonus_Freeze_Ghosts bfg;
    public int[][] bonusMap;
    public JLabel[][] bonusLabels;
    public int addedBonuses;
    public JLabel label;
    public BonusImunityApplyer BIA;
    public Bonus2xPointsApplyer B2XPA;
    public Bonus2xSpeedApplyer B2XSA;
    public BonusFreezeGhostsApplyer BFGA;
    public BonusExtraLiveApplyer BELA;
    public LinkedList<BonusApplyer_I> bonuses;


    public BonusManager(GamePanelMain gp){

        this.gp = gp;
        random = new Random();
        bonusMap = new int[gp.height][gp.width];
        bonusLabels = new JLabel[gp.height][gp.width];
        BonusTiles = new HashMap<>();
        fillMatrix();
        getBonusImages();
        addedBonuses = 0;
        bonuses = new LinkedList<>();

        BIA = new BonusImunityApplyer(this.gp);
        B2XPA = new Bonus2xPointsApplyer(this.gp);
        B2XSA = new Bonus2xSpeedApplyer(this.gp);
        BFGA = new BonusFreezeGhostsApplyer(this.gp);
        BELA = new BonusExtraLiveApplyer(this.gp);

        bonuses.add(BIA);
        bonuses.add(B2XPA);
        bonuses.add(B2XSA);
        bonuses.add(BFGA);

    }

    /**
     * przypisanie obrazów png do zmiennych oraz zainicjowanie HasMap i Ikon reprezentujących owe bonusy
     */
    public void getBonusImages() {

        try {

            BI = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/BonusImunity.png")),1.5);
            BonusTiles.put(1,BI);
            iconBI = iconConverter(BI);

            B2xP = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/Bonus2xPoints.png")),1.5);
            BonusTiles.put(2,B2xP);
            iconB2xP = iconConverter(B2xP);

            B2xS = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/Bonus2xSpeed.png")),1.5);
            BonusTiles.put(3,B2xS);
            iconB2xS = iconConverter(B2xS);

            BEL = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/BonusExtraLive.png")),1.5);
            BonusTiles.put(4,BEL);
            iconBEL = iconConverter(BEL);

            BFG = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/BonusFreeze.png")),1.5);
            BonusTiles.put(5,BFG);
            iconBFG = iconConverter(BFG);

            empty = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/empty.png")),1.5);
            iconEmpty = iconConverter(empty);


        }catch(IOException e){}

    }

    /**
     * metoda wypełniająca całą matrycę zerowymi wartościami
     */
    private void fillMatrix(){
        for(int i = 0; i < bonusMap.length; i++){
            for(int j = 0; j < bonusMap[i].length; j++){
                bonusMap[i][j] = 0;
                bonusLabels[i][j] = setLabelData(iconEmpty, i, j);
                gp.add(bonusLabels[i][j]);
            }
        }
    }


    //metoda zmieniająca ikony wszystkich lejbeli na puste
    public void cleanMatrix(){

        for(int i = 0; i < bonusMap.length; i++){
            for(int j = 0; j < bonusMap[i].length; j++){
                bonusMap[i][j] = 0;
                bonusLabels[i][j].setIcon(iconEmpty);
            }
        }

        for(int i = 0; i < bonuses.size(); i++) bonuses.get(i).resetEffect();

    }

    /**
     * metoda konwertująca przekazany obraz na ikonę
     * @param image Image
     * @return ImageIcon
     */
    public ImageIcon iconConverter(Image image){
        return new ImageIcon(image);
    }

    public void startCycle(){

        BonusCreator = null;
        BonusCreator = new Thread(this);
        BonusCreator.start();

    }

    @Override
    public void run(){

        while(BonusCreator != null) {

            //5 sekundowy odstęp pomiędzy tworzeniem bonusów dla gracza
            try {
                for(int j = 0; j < 60*5; j++) {
                    gp.gameFrame.checkInsidePaused();
                    gp.gameFrame.checkPaused();
                    Thread.sleep(1000/60);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

//            //25% szansa na stworzenie ulepszenia przez jednego z duszków
//            if (random.nextInt(4) == 0) {
//
//                int index = random.nextInt(gp.ghosts.size());
//
//                //sprawdzenie czy ghost nie jest poza granicami mapy, żeby uniknąć
//                // out of array bounds errora
//
//                if(gp.ghosts.get(index).isInsideOfMap()) {
//                    BonusCreator(gp.ghosts.get(index));
//                }
//
//            }


            /*
            Ponieważ nie było jasnego doprecyzowania czy jeden z duszków co 5 sakund ma tworzyć ulepszenie
            czy wszystkie na raz mogą utworzyć ulepszenie to napisałem 2 bloki kodu
             */


            //25% szansa na stworzenie ulepszenia przez każdego z duszków raz na 5 sekund
            for(int i = 0; i < gp.ghosts.size(); i++){

                if (random.nextInt(4) == 0) {

                    if(gp.ghosts.get(i).isInsideOfMap() && gp.ghosts.get(i).sleep < 0) {

                        BonusCreator(gp.ghosts.get(i));

                    }

                }

            }

        }

    }


    /**
     * metoda zmieniająca ikonę na przekazaną jako argument
     * @param icon ImageIcon
     * @param y współrzędna Y
     * @param x współrzędna X
     */
    public void setLabelImage(ImageIcon icon, int y, int x){
        bonusLabels[y][x].setIcon(icon);
    }

    /**
     * metoda tworząca lejbel i ustawia przekazany obraz jako ikonę
     * @param icon ImageIcon
     * @param y współrzędna Y
     * @param x współrzędna X
     * @return JLabel z powyższymi parametrami
     */
    public JLabel setLabelData(ImageIcon icon, int y, int x){
        JLabel label = new JLabel();
        label.setIcon(icon);
        label.setBounds(x * gp.tileSize - gp.tileSize/4
                ,y * gp.tileSize - gp.tileSize/4
                ,(int)(gp.tileSize * 1.5)
                ,(int)(gp.tileSize * 1.5));

        return label;
    }

    /**
     * metoda ustawiająca pusty obraz do lejbela
     * @param y współrzędna Y
     * @param x współrzędna X
     */
    public void clearLabel(int y, int x){
        bonusLabels[y][x].setIcon(iconEmpty);
    }

    /**
     * metoda losująca jedno z 5 ulepszeń i wprowadzająca na mapie to ulepszenie
     * @param ghost Ghost
     */
    public void BonusCreator(Ghost ghost){

        //pobranie współrzędnych ducha
        //na tych współrzędnych pojawi się bonus
        yIndex = ghost.HitBox.y / gp.tileSize;
        xIndex = ghost.HitBox.x / gp.tileSize;

        switch (random.nextInt(5) + 1){

            /*

            Struktura jest bardzo prosta

            1.W matrycy na odpowiednich współrzędnych zmienia się indeks
            2.Tworzona jest nowa instancja bonusu
            3.Uruchamiany jest wątek odliczjący X sekund zanim zniknie ten bonus
            4.Na odpowiednich współrzędnych jest ustawiana Icona reprezentująca bonus

             */


            case 1:
                bonusMap[yIndex][xIndex] = 1;
                bi = new Bonus_Imunity(gp, yIndex, xIndex);
                bi.CreateBonus();
                setLabelImage(iconBI, yIndex, xIndex);
                break;

            case 2:
                bonusMap[yIndex][xIndex] = 2;
                b2xp = new Bonus_2x_Points(gp, yIndex, xIndex);
                b2xp.CreateBonus();
                setLabelImage(iconB2xP, yIndex, xIndex);
                break;

            case 3:
                bonusMap[yIndex][xIndex] = 3;
                b2xs = new Bonus_2x_Speed(gp, yIndex, xIndex);
                b2xs.CreateBonus();
                setLabelImage(iconB2xS, yIndex, xIndex);
                break;

            case 4:
                bonusMap[yIndex][xIndex] = 4;
                bel = new Bonus_Extra_Live(gp, yIndex, xIndex);
                bel.CreateBonus();
                setLabelImage(iconBEL, yIndex, xIndex);
                break;

            case 5:
                bonusMap[yIndex][xIndex] = 5;
                bfg = new Bonus_Freeze_Ghosts(gp, yIndex, xIndex);
                bfg.CreateBonus();
                setLabelImage(iconBFG, yIndex, xIndex);
                break;

        }

    }

    /**
     * metoda aplikująca odpowiedni bonus w zależności od indeksu znajdującego się w tablicy
     * @param YCord współrzędna Y
     * @param XCord współrzędna X
     */
    public void BonusApplyer(int YCord, int XCord){

        clearLabel(YCord, XCord);

        switch (bonusMap[YCord][XCord]){

            case 1:
                BIA.ApplyEffect();
                break;
            case 2:
                B2XPA.ApplyEffect();
                break;
            case 3:
                B2XSA.ApplyEffect();
                break;
            case 4:
                BELA.ApplyEffect();
                break;
            case 5:
                BFGA.ApplyEffect();
                break;

        }

        //wyzerowanie indeksu po aplikacji bonusu
        bonusMap[YCord][XCord] = 0;

    }

}