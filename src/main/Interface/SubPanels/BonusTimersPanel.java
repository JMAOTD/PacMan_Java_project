package main.Interface.SubPanels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Klasa odpowiada za pokazanie graczowi przez jak długi czas będą zastosowane dla jego
 * postaci odpowiednie efekty
 */
public class BonusTimersPanel extends JPanel {

    GamePanelMain gp;

    public int ScreenWidth, ScreenHeight;
    public Dimension dimension;
    public ImageIcon bonus1, bonus2, bonus3, bonus4, emptyCell, fullCell;
    public JLabel[][] bonusBars;
    public JLabel[] bonusLabels;
    public int xStart, yStart, tileSize;



    public BonusTimersPanel(GamePanelMain gp){

        this.gp = gp;

        tileSize = 24;

        ScreenWidth = tileSize * (8*4 + 1) ;
        ScreenHeight = tileSize * 3;


        xStart = tileSize;
        yStart = tileSize;

        dimension = new Dimension(ScreenWidth, ScreenHeight);

        this.setPreferredSize(dimension);
        setSize(dimension);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(false);
        this.setLayout(null);

        setIcons();

        bonusBars = new JLabel[4][10];
        bonusLabels = new JLabel[4];
        initializePanel();
        initializeBars();

    }

    /**
     * metoda przypisująca do zmiennych ImageIcon odpowiednie obrazy
     */
    public void setIcons(){

        bonus1 = gp.bc.iconBI;
        bonus2 = gp.bc.iconB2xS;
        bonus3 = gp.bc.iconB2xP;
        bonus4 = gp.bc.iconBFG;

        //przypisanie obrazów png do zmiennych oraz zainicjowanie HasMap
        try {

            Image tmp1, tmp2;

            tmp1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/interface_elements/empty.png")),1.5);
            emptyCell = gp.bc.iconConverter(tmp1);

            tmp2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/interface_elements/full.png")),1.5);
            fullCell = gp.bc.iconConverter(tmp2);

        }catch(IOException e){}

    }

    /**
     * metoda tworząca i dodająca do panelu znaczki bonusów
     */
    public void initializePanel(){

        bonusLabels[0] = setLabelData(bonus1, yStart, xStart);
        bonusLabels[1] = setLabelData(bonus2, yStart ,xStart + 8 * tileSize);
        bonusLabels[2] = setLabelData(bonus3, yStart, xStart + 16 * tileSize);
        bonusLabels[3] = setLabelData(bonus4, yStart, xStart + 24 * tileSize);

        for(int i = 0; i < bonusLabels.length; i++) this.add(bonusLabels[i]);

    }

    /**
     * metoda ustawiające domyślnie pustu progresBar dla wszystkich bonusów
     */
    public void initializeBars(){

        for(int i = 0; i < bonusBars.length; i++){

            for(int j = 0; j < bonusBars[i].length; j++){

                bonusBars[i][j] = setLabelData(emptyCell
                        ,bonusLabels[i].getY()
                        ,bonusLabels[i].getX() + tileSize*2 + (tileSize/2) * j
                );

                this.add(bonusBars[i][j]);

            }

        }

    }

    /**
     * metoda zwracająca lejbel z charakterystykami ustawionymi zgodni z przekazanymi parametrami
     * @param icon ImageIcon
     * @param y pozycja Y
     * @param x pozycja X
     * @return Zwraca sparametryzowany JLabel
     */
    public JLabel setLabelData(ImageIcon icon, int y, int x){

        JLabel label = new JLabel();
        label.setIcon(icon);
        label.setBounds(
                x
                ,y
                ,tileSize
                ,tileSize
        );

        return label;
    }

    /**
     * metoda która pokazuje odpowiednią do przekazanych parametrów ilość progresBara
     * @param bonusNum numer bonusu
     * @param percent procent załadowania
     */
    public synchronized void loadBar(int bonusNum, int percent){

        for(int i = 0; i < bonusBars[bonusNum].length; i++){

            if(i < percent - 1){

                bonusBars[bonusNum][i].setIcon(fullCell);

            }else{

                bonusBars[bonusNum][i].setIcon(emptyCell);

            }

        }

    }

}
