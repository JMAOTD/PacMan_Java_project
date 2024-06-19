package main.entity;

import main.Interface.SubPanels.GamePanelMain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Klasa odpowiada za nadanie osobowości duchowi Clyde
 */
public class Clyde extends Ghost implements I_Entity{

    //konstruktor
    public Clyde(GamePanelMain gp){

        this.gp = gp;

        this.getEntityImage();
        this.setDefault(false);

        //utworzenie HitBoxa ducha
        HitBox = new Rectangle(x, y, gp.tileSize, gp.tileSize);

        //pattern ducha, potrzebny do algorytmu poszukiwania ścieżki do gracza
        ghostPattern = 'c';

    }

    /**
     * ustawia dane Clyde na domyślne
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

        xScatter = 0;
        yScatter = gp.height - 1;

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

        sleep = (gp.tileSize / speed) * 25 + 5;

        clearStates();

    }

    /**
     * metoda pobiera i przypisuje obrazy
     */
    @Override
    public void getEntityImage() {

        try {

            u1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Top_1.png")),2);
            u2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Top_2.png")),2);
            u3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Top_3.png")),2);
            l1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Left_1.png")),2);
            l2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Left_2.png")),2);
            l3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Left_3.png")),2);
            d1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Bot_1.png")),2);
            d2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Bot_2.png")),2);
            d3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Bot_3.png")),2);
            r1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Right_1.png")),2);
            r2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Right_2.png")),2);
            r3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Clyde_Right_3.png")),2);

        }catch(IOException e){}

        //przypisanie wspólnych dla wszystkich duchów obrazów
        setComunisticPictures();

    }

}