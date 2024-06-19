package main.Interface.SubPanels;

import main.Interface.FontIcons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * klasa odpowiedzialna za reprezentowanie Graficznego interfejsu użytkownika
 * skonstruowana jest z 6 linijek lejbeli, gdzie początek każdej linijki jest statyczny
 */
public class UiPanel extends JPanel {

    GamePanelMain gp;

    public int ScreenWidth, ScreenHeight;
    public Dimension dimension;

    public JLabel[] Score, HighScore, Time, Current_lvl, Ghost_Phase, Lives;

    public ImageIcon live;

    //inicjalizacja tablicy która będzie tabloidem
    public JLabel[][] panel = {

            Score = new JLabel[15]
            ,HighScore = new JLabel[20]
            ,Time = new JLabel[15]
            ,Current_lvl = new JLabel[17]
            ,Ghost_Phase = new JLabel[22]
            ,Lives = new JLabel[11]

    };

    public String[] titles;

    public int xStart, yStart, tileWidth, tileHeight, labelWidth, labelHeight;
    public FontIcons FI;
    public int highScore;



    public UiPanel(GamePanelMain gp){

        this.gp = gp;
        FI = new FontIcons();

        //ustawienia punktów startowych oraz rozmiarów lejbeli
        tileWidth = 14;
        tileHeight = 18;

        labelWidth = tileWidth - 2;
        labelHeight = tileHeight - 2;

        ScreenWidth = tileWidth * 25;
        ScreenHeight = tileHeight * 13;

        xStart = tileWidth + 1;
        yStart = tileHeight + 1;

        dimension = new Dimension(ScreenWidth, ScreenHeight);

        this.setPreferredSize(dimension);
        setSize(dimension);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setLayout(null);

        //inicjalizacja stałych elementów tabloidów
        titles = new String[]{
                 "Score "
                ,"Hight Score "
                ,"Time "
                ,"Current lvl "
                ,"Ghost phase "
                ,"Lives "
        };

        setLabels();
        setHighScore();

    }

    /**
     * rozmieszczenie wszystkich lejbeli w sposób w który będą tworzyć tabloid
     */
    public void setLabels(){

        for(int i = 0; i < panel.length; i++){

            for(int j = 0; j < panel[i].length; j++){

                if(j < titles[i].length()) {

                    panel[i][j] = setLabelData(
                            FI.iconMap.get(titles[i].charAt(j))
                            ,yStart + i * labelHeight
                            , xStart + j * labelHeight
                    );

                } else {

                    panel[i][j] = setLabelData(
                            FI.iconMap.get(' ')
                            ,yStart + i*labelHeight
                            , xStart + j*labelHeight
                    );

                }

                this.add(panel[i][j]);


            }

            yStart += tileHeight;

        }

        Image tmpImage = null;

        try {

            tmpImage = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream(
                            "/Interface_elements/live.png"))
            );

        } catch (IOException e) {}

        live = new ImageIcon(tmpImage);
        refreshLivesCounter();

    }

    /**
     * metoda pobierająca największy HighScore jeżeli tablica z mini nie jest pusta, a następnie wyświetla
     * w UI
     */
    public void setHighScore(){

        //jeżeli plik z HighScores jest pusty to highScores = 0;
        if(!gp.gameFrame.highScoresPanel.scoresManager.highScores.isEmpty()) {

            highScore = gp.gameFrame.highScoresPanel.scoresManager.highScores.get(0).score;

        }else highScore = 0;

        refreshInfo(1, Integer.toString(highScore));

    }

    /**
     * metoda updejtująca tekst/liczby we wskazanym jako parametr panelu
     * panel to kilka jlejbeli wyrównanych względem siebie horyzontalnie
     * @param rowNum numer linijki w "tabloidzie"
     * @param newValue nowa wartość
     */
    public void refreshInfo(int rowNum, String newValue){

        for(int i = titles[rowNum].length(); i < panel[rowNum].length; i++){

            if(i - titles[rowNum].length() + 1 > newValue.length()) {

                panel[rowNum][i].setIcon(FI.iconMap.get(' '));

            }else {

                panel[rowNum][i].setIcon(FI.iconMap.get(newValue.charAt(i - titles[rowNum].length())));

            }
        }

    }

    /**
     * metoda zwracająca lejbel z odpowiednio ustawionymi parametrami zgodnymi z parametrami przekazanymi
     * @param icon ImageIcon
     * @param y pozycja y
     * @param x pozycja x
     * @return zwraca JLejbel
     */
    public JLabel setLabelData(ImageIcon icon, int y, int x){

        JLabel label = new JLabel();
        label.setIcon(icon);
        label.setBounds(
                x
                ,y
                ,tileWidth
                ,tileHeight
        );

        return label;
    }

    /**
     * metoda odświeżająca ilość serc wyświetlanych w UI
     */
    public void refreshLivesCounter(){

        for(int i = titles[5].length(); i < panel[5].length; i++){

            if(i - titles[5].length() + 1 > gp.player.lives) {

                panel[5][i].setIcon(FI.iconMap.get(' '));

            }else {

                panel[5][i].setIcon(live);

            }

        }

    }

}
