package main.levels;

import main.Interface.SubPanels.GamePanelMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 * Klasa odpowiedzialna za graficzną prezentację wszystkich paletek oraz
 * obliczenia związane z tym elementem gry
 */
public class Edible_Map extends JPanel implements MapLayout{

    public HashMap<Integer, Image> EdiblesTiles;
    public HashMap <Integer, Boolean> isEdibleSet;
    public Image smallDot, bigDot, empty, tile;
    public int[][] EdiblesTilesLayout;
    public int leftPallets;
    public int MapWidth, MapHeight;
    GamePanelMain gp;
    public JLabel[][] labels;
    ImageIcon imageicon;
    Image tempImage;



    public Edible_Map(GamePanelMain gp){

        this.gp = gp;
        MapHeight = gp.height;
        MapWidth = gp.width;
        EdiblesTiles = new HashMap<>();
        isEdibleSet = new HashMap<>();
        EdiblesTilesLayout = new int[MapHeight][MapWidth];
        tile = null;
        getMapImages();
        imageicon = null;

        labels = new JLabel[MapHeight][MapWidth];
        leftPallets = 0;
        initializeFoodMatrix();

    }

    /**
     * metoda odpowiedzialna za pobranie z pliku z zasobami obrazów
     */
    @Override
    public void getMapImages() {


        //przypisanie obrazów png do zmiennych oraz zainicjowanie HasMap i dwówymiarowego masywu colisionTilesLayout
        try {

            //Przypisanie  ltilów edibles
            empty = ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/empty.png"));
            EdiblesTiles.put(0,empty);
            isEdibleSet.put(0, false);

            smallDot = ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/Small_Dot.png"));
            EdiblesTiles.put(1,smallDot);
            isEdibleSet.put(1, true);

            bigDot = ImageIO.read(getClass().getResourceAsStream("/Entity/Edible/Big_Dot.png"));
            EdiblesTiles.put(2, bigDot);
            isEdibleSet.put(2, true);


        }catch(IOException e){}

        setEdiblesTiles();


    }

    /**
     * metoda odpowiedzialna za zainicjowanie matrycy danymi z pliku .txt
     */
    public void setEdiblesTiles(){

        InputStream is = null;
        BufferedReader br = null;

        try{

            switch(MapHeight){
                case 12:
                    is = getClass().getResourceAsStream("/Map/Edibles_Map/Map12x16Edible.txt");
                    break;
                case 15:
                    is = getClass().getResourceAsStream("/Map/Edibles_Map/Map15x20Edible.txt");
                    break;
                case 18:
                    is = getClass().getResourceAsStream("/Map/Edibles_Map/Map18x24Edible.txt");
                    break;
                case 21:
                    is = getClass().getResourceAsStream("/Map/Edibles_Map/Map21x28Edible.txt");
                    break;
                case 31:
                    is = getClass().getResourceAsStream("/Map/Edibles_Map/Map31x28OriginalEdible.txt");
                    break;
            }

            br = new BufferedReader(new InputStreamReader(is));

            String line;

            //zczytanie z pliku linijka po linijce danych, następnie rozdzielenie ich regexem spacji
            //i dodanie do Matrycy
            for(int i = 0; i < MapHeight; i++) {

                line = br.readLine();
                String[] lineValues = line.split(" ");

                for (int j = 0; j < MapWidth; j++) {

                    int number = Integer.parseInt(lineValues[j]);
                    EdiblesTilesLayout[i][j] = number;

                }

            }

        }catch(Exception e){
            System.out.println("Wykryto zmianę w plikach źródłowych gry /!\\ ALARM");
        }



    }

    /**
     * utworzenie i dodanie do panelu Lejbeli jedynie jeżeli wartość w Matryce jest niezerowa
     */
    public void initializeFoodMatrix(){

        for(int i = 0; i < labels.length; i++){

            for(int j = 0; j < labels[i].length; j++) {

                if(EdiblesTilesLayout[i][j]!= 0) {

                    tempImage = EdiblesTiles.get(EdiblesTilesLayout[i][j]);
                    imageicon = new ImageIcon(tempImage);

                    labels[i][j] = new JLabel();
                    labels[i][j] = setLabelData(imageicon, i, j);

                    gp.add(labels[i][j]);

                    //po każdym dodaniu paletki następuje inkrementacja zmiennej
                    //danna zmienna jest bardzo przydatna żeby wyznaczyć
                    //kiedy gracz przechodzi na następny poziom
                    leftPallets++;

                }

            }

        }

    }

    /**
     * metoda zmieniająca obraz w JLejbelu na pusty po zjedzeniu paletki przez gracza
     * @param y
     * @param x
     */
    public void clearLabel(int y, int x){
        labels[y][x].setIcon(new ImageIcon(empty));
        leftPallets--;
        //jeżeli wszystkie paletki zjedzone to wywoływana jest metoda przenosząca gracza na następny poziom
        if(leftPallets == 0) gp.nextLvl();
    }




    /**
     * metoda zwracająca lejbel zainicjowany przekazanymi jako argument danymi
     * @param icon ImageIcon
     * @param y yPosition
     * @param x xPosition
     * @return
     */
    public JLabel setLabelData(ImageIcon icon, int y, int x){

        JLabel label = new JLabel();
        label.setIcon(icon);
        label.setBounds(x * gp.tileSize, y * gp.tileSize, gp.tileSize, gp.tileSize);

        return label;
    }


}
