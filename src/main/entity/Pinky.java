package main.entity;

import main.Interface.SubPanels.GamePanelMain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Klasa odpowiada za nadanie osobowości duchowi Pinky
 */
public class Pinky extends Ghost implements I_Entity{

    //konstruktor
    public Pinky(GamePanelMain gp){

        this.gp = gp;

        this.getEntityImage();
        this.setDefault(false);

        //stworzenie hitBoxa ducha
        HitBox = new Rectangle(x, y, gp.tileSize, gp.tileSize);

        //pattern ducha, potrzebny do algorytmu poszukiwania ścieżki do gracza
        ghostPattern = 'p';

    }

    /**
     * metoda ustawia dane Pinky na domyślne
     * @param isReset boolean czy jest resetowany
     */
    public void setDefault(boolean isReset){

        switch(gp.height){

            case 12:
                x = (int) (7.5 * gp.tileSize);
                y = 3 * gp.tileSize;
                break;
            case 15:
                x = (int) (9.5 * gp.tileSize);
                y = 5 * gp.tileSize;
                break;
            case 18:
                x = (int) (11.5 * gp.tileSize);
                y = 7 * gp.tileSize;
                break;
            case 21:
                x = (int) (13.5 * gp.tileSize);
                y = 6 * gp.tileSize;
                break;
            case 31:
                x = (int) (13.5 * gp.tileSize);
                y = 11 * gp.tileSize;
                break;

        }

        xHome = x;
        yHome = y;

        if(isReset) {
            HitBox.x = x;
            HitBox.y = y;
        }

        xScatter = 2;
        yScatter = 0;

        speed = 2;
        sighn = -1;

        direction = 'r';
        vector = new int []{-1, 0};

        FPSCounter = 0;
        spriteCounter = 1;
        hardCondition = false;

        this.image = u1;

        //domyślne ustawienie na false
        isMoving = true;
        isColiding = false;

        sleep = (gp.tileSize / speed) * 15 + 9;

        clearStates();

    }

    /**
     * metoda pobiera i przypisuje obrazy
     */
    @Override
    public void getEntityImage() {

        try {

            u1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Top_1.png")),2);
            u2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Top_2.png")),2);
            u3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Top_3.png")),2);
            l1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Left_1.png")),2);
            l2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Left_2.png")),2);
            l3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Left_3.png")),2);
            d1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Bot_1.png")),2);
            d2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Bot_2.png")),2);
            d3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Bot_3.png")),2);
            r1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Right_1.png")),2);
            r2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Right_2.png")),2);
            r3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Pinky_Right_3.png")),2);

        }catch(IOException e){}

        //przypisanie wspólnych dla wszystkich duchów obrazów
        setComunisticPictures();

    }

}