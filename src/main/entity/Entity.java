package main.entity;

import main.inputs.KeyBinds;
import main.Interface.SubPanels.GamePanelMain;

import java.awt.*;

/**
 * klasa odpowiada za opisanie logiki entity i w wielkiej mierze gracza
 */
public class Entity{

    //zmienne pokreślające pozycję entity
    public int x, y;

    //zmienna określająca prędkość
    public int speed;

    public int speedMultiplyer = 1;

    //zmienna char określająca kierunek w którym zwrócony jest entity
    public char direction;

    public int[] vector;

    //zmienne które będą wykorzystywane do wyświetlania sprajtów
    public Image u1, u2, u3, l1, l2, l3, d1, d2, d3, r1, r2, r3;
    public Image image, debugColisionBox;

    //zmienne potrzebne do zmiany spritów celem utworzenia animacji
    public int spriteCounter, FPSCounter;
    public boolean isMoving;
    public Rectangle HitBox;
    boolean isColiding;
    public int sighn = -1;
    boolean hardCondition;
    GamePanelMain gp;
    public KeyBinds keyB;


    /**
     * metoda sprawdzająca czy gracz może skrzęcić lewo/prawo
     */
    public void VerticalCheck () {


        if (keyB.UpPressed) {

            vector[0] = 0;
            vector[1] = -1;
            if (ColisionCheckU(1)) returnDirection(direction);
            else direction = 'u';

        } else if (keyB.DownPressed) {

            vector[0] = 0;
            vector[1] = 1;
            if (ColisionCheckD(1)) returnDirection(direction);
            else direction = 'd';

        }

    }

    /**
     * metoda sprawdzająca czy gracz chce zmienić kierunek na góra - dół
     */
    public void HorizontalCheck () {

        if (keyB.LeftPressed) {

            vector[0] = -1;
            vector[1] = 0;
            if (ColisionCheckL(1)) returnDirection(direction);
            else direction = 'l';

        } else if (keyB.RightPressed) {

            vector[0] = 1;
            vector[1] = 0;
            if (ColisionCheckR(1)) returnDirection(direction);
            else direction = 'r';

        }

    }

    /**
     * metoda weryfikująca możliwość zmiany na dowolny kierunek
     */
    public void IntersectionCheck (){

        VerticalCheck();
        HorizontalCheck();

    }

    /**
     * metoda zmieniająca położenie entity na podstawie warunków
     */
    public void move (){

        if(HitBox.x > gp.screenWidth) HitBox.x = -1*gp.tileSize + speed*speedMultiplyer;                 //right
        if(HitBox.x < -1*gp.tileSize) HitBox.x = gp.screenWidth + gp.tileSize - speed*speedMultiplyer;     //left
        if(HitBox.y > gp.screenHeight) HitBox.y = -1*gp.tileSize + speed*speedMultiplyer;                //down
        if(HitBox.y < -1*gp.tileSize) HitBox.y = gp.screenHeight + gp.tileSize - speed*speedMultiplyer;    //top


        if (!hardCondition || !ColisionCheckUniversal(direction, 0)) {

            switch(direction){
                case 'u' : HitBox.y -= speed*speedMultiplyer; break;
                case 'l' : HitBox.x -= speed*speedMultiplyer; break;
                case 'd' : HitBox.y += speed*speedMultiplyer; break;
                case 'r' : HitBox.x += speed*speedMultiplyer; break;
            }

        } else {

            switch(direction){
                case 'u' : HitBox.y += gp.screenHeight + gp.tileSize - speed*speedMultiplyer; break;
                case 'l' : HitBox.x += gp.screenWidth + gp.tileSize - speed*speedMultiplyer; break;
                case 'd' : HitBox.y -= gp.screenHeight + speed*speedMultiplyer; break;
                case 'r' : HitBox.x -= gp.screenWidth + speed*speedMultiplyer; break;
            }

        }

    }

    /**
     * metoda wyłowująca odpowiedną metodę zależne od kieruunku char
     * @param c kierunek (u,l,d,r) char
     * @param i jeżeli 0 to weryfikacja pod kątem teleportacji
     * @return boolean czy w danym kierunku jest możliwość poruszania się
     */
    public boolean ColisionCheckUniversal(char c, int i){
        switch(c){
            case 'u': return ColisionCheckU(i);
            case 'l': return ColisionCheckL(i);
            case 'd': return ColisionCheckD(i);
            case 'r': return ColisionCheckR(i);
            default: return false;
        }
    }

    /**
     * metoda wyłowująca odpowiedną metodę zależne od kieruunku char
     * @param c kierunek (u,l,d,r) char
     * @return boolean czy w danym kierunku jest możliwość poruszania się
     */
    public boolean ColisionCheckUniversal(char c){
        switch(c){
            case 'u': return ColisionCheckU(1);
            case 'l': return ColisionCheckL(1);
            case 'd': return ColisionCheckD(1);
            case 'r': return ColisionCheckR(1);
            default: return false;
        }
    }

    /**
     * metoda weryfikująca czy blok nad entity jest kolizyjny czy nie
     * @param i jeżeli 0 to weryfikacja pod kątem teleportacji
     * @return boolean czy blok nad entity jest kolizyjny czy nie
     */
    public boolean ColisionCheckU (int i){

        boolean returnStatement;

        int XCord = correctCordsX(HitBox.x / gp.tileSize + vector[0]);
        int YCord = correctCordsY((HitBox.y - speed*speedMultiplyer)/ gp.tileSize + vector[1] + 1);

        if(i == 0){
            YCord = correctCordsY((HitBox.y - speed*speedMultiplyer)/ gp.tileSize + vector[1] + 2);
            if(YCord == gp.height - 1) return false;
            returnStatement = gp.map.ColisionTilesLayout[YCord][XCord] == 4;
        }
        else returnStatement = gp.map.isSolidSet.get(gp.map.ColisionTilesLayout[YCord][XCord]);
        return  returnStatement;
    }

    /**
     * metoda weryfikująca czy blok po lewej od entity jest kolizyjny czy nie
     * @param i jeżeli 0 to weryfikacja pod kątem teleportacji
     * @return boolean czy blok po lewej od entity jest kolizyjny czy nie
     */
    public boolean ColisionCheckL (int i){

        boolean returnStatement;

        int XCord = correctCordsX ((HitBox.x - speed*speedMultiplyer) / gp.tileSize + vector[0] + 1);
        int YCord = correctCordsY(HitBox.y / gp.tileSize + vector[1]);

        if(i == 0){
            XCord = correctCordsX ((HitBox.x - speed*speedMultiplyer) / gp.tileSize + vector[0] + 2);
            if(XCord == gp.width - 1) return false;
            returnStatement = gp.map.ColisionTilesLayout[YCord][XCord] == 4;
        }
        else returnStatement = gp.map.isSolidSet.get(gp.map.ColisionTilesLayout[YCord][XCord]);
        return  returnStatement;
    }

    /**
     * metoda weryfikująca czy blok pod entity jest kolizyjny czy nie
     * @param i jeżeli 0 to weryfikacja pod kątem teleportacji
     * @return boolean czy blok pod entity jest kolizyjny czy nie
     */
    public boolean ColisionCheckD (int i){

        boolean returnStatement;

        int XCord = correctCordsX(HitBox.x / gp.tileSize + vector[0]);
        int YCord = correctCordsY(HitBox.y / gp.tileSize + vector[1]);

        if(i == 0){
            if(HitBox.y == 0) return false;

            try{
                returnStatement = gp.map.ColisionTilesLayout[YCord - 1][XCord] == 4;
            }catch (ArrayIndexOutOfBoundsException e) {
                XCord = correctCordsX(XCord);
                YCord = correctCordsY(YCord - 1);
                returnStatement = gp.map.ColisionTilesLayout[YCord][XCord] == 4;
            }

        }
        else returnStatement = gp.map.isSolidSet.get(gp.map.ColisionTilesLayout[YCord][XCord]);
        return  returnStatement;
    }

    /**
     * metoda weryfikująca czy blok po prawej od entity jest kolizyjny czy nie
     * @param i jeżeli 0 to weryfikacja pod kątem teleportacji
     * @return boolean czy blok po prawej od entity jest kolizyjny czy nie
     */
    public boolean ColisionCheckR (int i){

        boolean returnStatement;

        int XCord = correctCordsX(HitBox.x/ gp.tileSize + vector[0]);
        int YCord = correctCordsY(HitBox.y / gp.tileSize + vector[1]);

        if(i == 0){
            if(HitBox.x == 0) return false;

            try{
                returnStatement = gp.map.ColisionTilesLayout[YCord][XCord - 1] == 4;
            }catch (ArrayIndexOutOfBoundsException e) {
                XCord = correctCordsX(XCord - 1);
                YCord = correctCordsY(YCord);
                returnStatement = gp.map.ColisionTilesLayout[YCord][XCord] == 4;
            }

        }
        else returnStatement = gp.map.isSolidSet.get(gp.map.ColisionTilesLayout[YCord][XCord]);
        return  returnStatement;
    }

    /**
     * metoda poprawiająca kooordynaty X żeby uniknąć wyjścia poza zasięg arraya
     * @param x wartość
     * @return zawsze zwracana wartośc będzie w granicach indeksów arraya
     */
    public int correctCordsX(int x){

        int returnValue = x;

        if(x < 0) returnValue = 0;
        if(x > gp.width - 1) returnValue = gp.width -1;

        return returnValue;
    }

    /**
     * metoda poprawiająca kooordynaty Y żeby uniknąć wyjścia poza zasięg arraya
     * @param y wartość
     * @return zawsze zwracana wartośc będzie w granicach indeksów arraya
     */
    public int correctCordsY(int y){

        int returnValue = y;

        if(y < 0) returnValue = 0;
        if(y > gp.height - 1) returnValue = gp.height -1;

        return returnValue;
    }

    /**
     * przywraca kierunek entity jeżeli zmiana miałaby powodować kolizję
     * @param c kierunek (u,l,d,r) char
     */
    public void returnDirection(char c){

        switch (c) {

            case 'u': vector[0] =  0; vector[1] = -1; break;
            case 'l': vector[0] = -1; vector[1] =  0; break;
            case 'd': vector[0] =  0; vector[1] =  1; break;
            case 'r': vector[0] =  1; vector[1] =  0; break;

        }

    }

    /**
     * metoda zwracająca wartość zmiennej odpowiadającej za to czy dany blok o podanych
     * współrzędnych jest solidny czy nie
     * @param y współrzędna Y podzielona przez rozmiar tajla
     * @param x współrzędna X podzielona przez rozmiar tajla
     * @return boolean czy blok jest solidny czy nie
     */
    public boolean isSolid (int y, int x){

        int YCord = correctCordsY(y);
        int XCord = correctCordsX(x);

        return gp.map.isSolidSet.get(gp.map.ColisionTilesLayout[YCord][XCord]);

    }

}