package main.levels;

import main.Interface.SubPanels.GamePanelMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;



/**
 * Klasa odpowiedzialna za mapę, wizualną która widnieje jako tło oraz mapę kolizji,
 * która jest wczytywana z pliku txt
 */
public class Map extends JPanel implements MapLayout {

    //mapa przechowująca wartości boolean odpowiednie do cyfrowej mapy
    public HashMap <Integer, Boolean> isSolidSet;

    public Image map;

    public int[][] ColisionTilesLayout;

    public int MapWidth, MapHeight;

    public GamePanelMain gp;

    public JLabel label;

    /**
     * konstruktor dla GamePanel
     * @param gp GamePanelMain
     */
    public Map(GamePanelMain gp){

        this.gp = gp;
        MapHeight = this.gp.height;
        MapWidth = this.gp.width;
        isSolidSet = new HashMap<>();
        ColisionTilesLayout = new int[MapHeight][MapWidth];
        getMapImages();
        label = new JLabel();

    }


    /**
     * przypisanie obrazów png do zmiennych oraz zainicjowanie HasMap i dwówymiarowego masywu colisionTilesLayout
     */
    @Override
    public void getMapImages() {

        InputStream is = null;
        BufferedReader br = null;

        try {

            //w zależności od rozmiaru wybranej planszy
            //wczytywane są odpowiednie pliki
            switch(MapHeight){
                case 12:
                    map = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Map/MapVisual/map12x16.png")),1.5);
                    is = getClass().getResourceAsStream("/Map/MapColision/Map12x16Colision.txt");
                    break;
                case 15:
                    map = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Map/MapVisual/map15x20.png")),1.5);
                    is = getClass().getResourceAsStream("/Map/MapColision/Map15x20Colision.txt");
                    break;
                case 18:
                    map = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Map/MapVisual/map18x24.png")),1.5);
                    is = getClass().getResourceAsStream("/Map/MapColision/Map18x24Colision.txt");
                    break;
                case 21:
                    map = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Map/MapVisual/map21x28.png")),1.5);
                    is = getClass().getResourceAsStream("/Map/MapColision/Map21x28Colision.txt");
                    break;
                case 31:
                    map = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Map/MapVisual/map31x28Original.png")),1.5);
                    is = getClass().getResourceAsStream("/Map/MapColision/Map31x28OriginalColision.txt");
                    break;
            }


            //dodanie wartości tłumaczących cyfrową mapę
            isSolidSet.put(0, true);
            isSolidSet.put(1, false);
            isSolidSet.put(2, false);
            isSolidSet.put(3, false);
            isSolidSet.put(4, false);


            br = new BufferedReader(new InputStreamReader(is));


        }catch(IOException e){}


        //odczytanie z plików wartości kolizyjności poszczególnych tajlów
        try{

            int x = 0, y = 0;
            String line;

            while(x < MapHeight && y < MapWidth){

                line = br.readLine();
                String[] lineValues = line.split(" ");

                for(int i = 0; i < MapWidth; i++){

                    int number = Integer.parseInt(lineValues[i]);
                    ColisionTilesLayout[x][i] =  number;

                }

                x++;
            }

        }catch(Exception e){
            System.out.println("Wykryto zmianę w plikach źródłowych gry /!\\ ALARM");
        }


    }

    /**
     * metoda dodająca jako tło obraz mapy
     */
    public void drawMapLabel() {

        label.setIcon(new ImageIcon(map));
        label.setLocation(0, 0);
        label.setSize(gp.screenWidth, gp.screenHeight);
        gp.add(label);

    }

}
