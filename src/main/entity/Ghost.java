package main.entity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


/**
 * Klasa odpowiadająca za dyregowanie logiką ducha
 */
public class Ghost extends Entity {

    public Image
            blue1, blue2, blue3
            ,white1, white2, white3
            ,eyesTop, eyesLeft, eyesBot, eyesRight
            ,targetTile
            ,bonus200, bonus400, bonus600, bonus800;

    public boolean isEaten, isHunting, isFrighten, isScatter, isExitingFrighten, justGotEaten;
    public boolean speedBuffed = false;
    public boolean TMPisHunting, TMPisScatter;
    public int sleep;
    public int sleepFinal;
    public int xScatter, yScatter;
    public int xHome, yHome;
    public int targetY, targetX;
    public char ghostPattern;
    public JLabel label = new JLabel();

    /**
     * przypisanie wspólnych obrazów
     */
    public void setComunisticPictures() {

        try {

            white1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/White_1.png")), 2);
            white2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/White_2.png")), 2);
            white3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/White_3.png")), 2);
            blue1 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Blue_1.png")), 2);
            blue2 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Blue_2.png")), 2);
            blue3 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Blue_3.png")), 2);
            eyesTop = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Eyes_Top.png")), 2);
            eyesLeft = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Eyes_Left.png")), 2);
            eyesBot = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Eyes_Bot.png")), 2);
            eyesRight = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Eyes_Right.png")), 2);
            bonus200 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Bonus16x16_200.png")), 2);
            bonus400 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Bonus16x16_400.png")), 2);
            bonus600 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Bonus16x16_600.png")), 2);
            bonus800 = gp.IS.imageScaler(ImageIO.read(getClass().getResourceAsStream("/Entity/Ghost/Bonus16x16_800.png")), 2);

        } catch (IOException e) {}

    }

    /**
     * metoda przypisująca tymczasowe wartości do obecnych i decydujących o zachowaniu ducha
     */
    public void assignTMP(){

        isHunting = TMPisHunting;
        isScatter = TMPisScatter;

    }

    /**
     * metoda obracająca ducha o 180 stopni, pod warunkiem że ten nie znajduje się na skraju planszy
     */
    public void rotate180 (){

        boolean isOnTheEdge = HitBox.x < gp.tileSize
                || HitBox.x > (gp.screenWidth - gp.tileSize)
                || HitBox.y < gp.tileSize
                || HitBox.y > (gp.screenHeight - gp.tileSize);

        if(!isOnTheEdge) {

            switch (direction) {

                case 'u': direction = 'd'; break;
                case 'l': direction = 'r'; break;
                case 'd': direction = 'u'; break;
                case 'r': direction = 'l'; break;

            }
        }

    }

    /**
     * metoda resetująca stany ducha
     */
    public void clearStates(){

        isEaten = false;
        isHunting = false;
        isFrighten = false;
        isScatter = false;
        isExitingFrighten = false;

    }

    /**
     * jeżeli duch znajduje się równo na skrzyżowaniu to poszukiwany jest kierunek w którym
     * ma poruszać się duch
     */
    @Override
    public void IntersectionCheck() {

        int XCord = correctCordsX(HitBox.x / gp.tileSize);
        int YCord = correctCordsY(HitBox.y / gp.tileSize);

        hardCondition = HitBox.x % gp.tileSize == 0 && HitBox.y % gp.tileSize == 0;

        if(hardCondition && (isEaten || justGotEaten) && !speedBuffed) {
            speedBuffed = true;
            speedMultiplyer =2;
        }

        if (gp.map.ColisionTilesLayout[YCord][XCord] == 1 && hardCondition) {

            pathfindingAI();

        }

    }

    /**
     * metoda wybierająca taki kierunek na skrzyżowaniu który najszybciej przyprowadzi do celu
     */
    public void pathfindingAI(){

        findTargetTile();

        if(!isFrighten && !isExitingFrighten) {
            char directionTemp = ' ';

            double dist1 = 999;
            double dist2 = 999;
            double dist3 = 999;

            int currentY = HitBox.y / gp.tileSize;
            int currentX = HitBox.x / gp.tileSize;


            switch (direction) {

                case 'u': {

                    if (!isSolid(currentY, currentX - 1)) {
                        dist1 = distanceToTarget(currentY, currentX - 1, targetY, targetX);
                    }
                    if (!isSolid(currentY, currentX + 1)) {
                        dist2 = distanceToTarget(currentY, currentX + 1, targetY, targetX);
                    }
                    if (!isSolid(currentY - 1, currentX)) {
                        dist3 = distanceToTarget(currentY - 1, currentX, targetY, targetX);
                    }

                    if (dist1 <= dist2 && dist1 <= dist3) directionTemp = 'l';
                    else if (dist2 <= dist1 && dist2 <= dist3) directionTemp = 'r';
                    else if (dist3 <= dist1 && dist3 <= dist2) directionTemp = 'u';

                }
                break;


                case 'l': {

                    if (!isSolid(currentY, currentX - 1)) {
                        dist1 = distanceToTarget(currentY, currentX - 1, targetY, targetX);
                    }
                    if (!isSolid(currentY - 1, currentX)) {
                        dist2 = distanceToTarget(currentY - 1, currentX, targetY, targetX);
                    }
                    if (!isSolid(currentY + 1, currentX)) {
                        dist3 = distanceToTarget(currentY + 1, currentX, targetY, targetX);
                    }

                    if (dist1 <= dist2 && dist1 <= dist3) directionTemp = 'l';
                    else if (dist2 <= dist1 && dist2 <= dist3) directionTemp = 'u';
                    else if (dist3 <= dist1 && dist3 <= dist2) directionTemp = 'd';

                }
                break;


                case 'd': {

                    if (!isSolid(currentY, currentX - 1)) {
                        dist1 = distanceToTarget(currentY, currentX - 1, targetY, targetX);
                    }
                    if (!isSolid(currentY, currentX + 1)) {
                        dist2 = distanceToTarget(currentY, currentX + 1, targetY, targetX);
                    }
                    if (!isSolid(currentY + 1, currentX)) {
                        dist3 = distanceToTarget(currentY + 1, currentX, targetY, targetX);
                    }

                    if (dist1 <= dist2 && dist1 <= dist3) directionTemp = 'l';
                    else if (dist2 <= dist1 && dist2 <= dist3) directionTemp = 'r';
                    else if (dist3 <= dist1 && dist3 <= dist2) directionTemp = 'd';

                }
                break;


                case 'r': {

                    if (!isSolid(currentY, currentX + 1)) {
                        dist1 = distanceToTarget(currentY, currentX + 1, targetY, targetX);
                    }
                    if (!isSolid(currentY - 1, currentX)) {
                        dist2 = distanceToTarget(currentY - 1, currentX, targetY, targetX);
                    }
                    if (!isSolid(currentY + 1, currentX)) {
                        dist3 = distanceToTarget(currentY + 1, currentX, targetY, targetX);
                    }

                    if (dist1 <= dist2 && dist1 <= dist3) directionTemp = 'r';
                    else if (dist2 <= dist1 && dist2 <= dist3) directionTemp = 'u';
                    else if (dist3 <= dist1 && dist3 <= dist2) directionTemp = 'd';

                }
                break;

            }

            direction = directionTemp;

        }
    }

    /**
     * metoda zarządzająca algorytmami poruszania się duchów w zależności od obecnego stanu ducha
     */
    public void findTargetTile(){

        if(isHunting && !isFrighten && !isExitingFrighten && !isScatter && !isEaten && !justGotEaten) {
            calculateHuntTarget();
        }

        if((isEaten || justGotEaten) && !isHunting && !isFrighten && !isExitingFrighten && !isScatter){
            targetX = xHome/gp.tileSize;
            targetY = yHome/gp.tileSize;
        }

        if((isFrighten || isExitingFrighten) && !isScatter && !isEaten && !isHunting && !justGotEaten) {
            randomDirection();
        }

        if(isScatter && !isEaten && !isHunting && !isFrighten && !isExitingFrighten && !justGotEaten) {
            targetX = xScatter;
            targetY = yScatter;
        }

    }

    /**
     * metoda decydująca gdzie skręci duch w zależności od jego typu
     */
    public void calculateHuntTarget(){

        switch(ghostPattern){

            //Blinky - zawsze dąży do pozycji gracza
            case 'b':

                targetY = gp.player.HitBox.y / gp.tileSize;
                targetX = gp.player.HitBox.x / gp.tileSize;
                break;

            //Pinky celuje 4 tily przed graczem
            case 'p':

                switch(gp.player.direction){

                    case 'u':
                        targetY = gp.player.HitBox.y / gp.tileSize - 4;
                        targetX = gp.player.HitBox.x / gp.tileSize - 4;
                        break;

                    case 'l':
                        targetY = gp.player.HitBox.y / gp.tileSize;
                        targetX = gp.player.HitBox.x / gp.tileSize - 4;
                        break;

                    case 'd':
                        targetY = gp.player.HitBox.y / gp.tileSize + 4;
                        targetX = gp.player.HitBox.x / gp.tileSize;
                        break;

                    case 'r':
                        targetY = gp.player.HitBox.y / gp.tileSize;
                        targetX = gp.player.HitBox.x / gp.tileSize + 4;
                        break;

                    default:
                        targetY = gp.player.HitBox.y / gp.tileSize;
                        targetX = gp.player.HitBox.x / gp.tileSize;
                        break;
                }
                break;


            //Inky - tutaj skomplikowane, ponieważ cel zależy od pozycji gracza oraz Blinky
            //pomaga oflankować
            case 'i':

                switch(gp.player.direction){

                    case 'u':
                        targetY = gp.player.HitBox.y / gp.tileSize - 2;
                        targetX = gp.player.HitBox.x / gp.tileSize - 2;
                        break;

                    case 'l':
                        targetY = gp.player.HitBox.y / gp.tileSize;
                        targetX = gp.player.HitBox.x / gp.tileSize - 2;
                        break;

                    case 'd':
                        targetY = gp.player.HitBox.y / gp.tileSize + 2;
                        targetX = gp.player.HitBox.x / gp.tileSize;
                        break;

                    case 'r':
                        targetY = gp.player.HitBox.y / gp.tileSize;
                        targetX = gp.player.HitBox.x / gp.tileSize + 2;
                        break;

                    default:
                        targetY = gp.player.HitBox.y / gp.tileSize;
                        targetX = gp.player.HitBox.x / gp.tileSize;
                        break;
                }

                targetY -= (gp.blinky.HitBox.y / gp.tileSize) - targetY;
                targetX -= (gp.blinky.HitBox.x / gp.tileSize) - targetX;

                break;


            //Clyde celuje w gracza dopóki nie znajdzie się w promieniu ośmiu bloków od gracza
            //wtedy jego celem staje się pozycja w lewym dolnym rogu mapy
            case 'c':

                if(distanceToTarget(HitBox.y, HitBox.x, gp.player.HitBox.y, gp.player.HitBox.x)/ gp.tileSize <= 8){

                    targetY = gp.height - 1;
                    targetX = 0;

                }else {

                    targetY = gp.player.HitBox.y / gp.tileSize;
                    targetX = gp.player.HitBox.x / gp.tileSize;

                }
                break;

            default:
                targetY = gp.player.HitBox.y / gp.tileSize;
                targetX = gp.player.HitBox.x / gp.tileSize;
        }

    }

    /**
     * metoda zwracająca losowy kierunek (poza zawróceniem)
     * i oczywiście nie może to być kierunek w stronę ściany
     */
    public void randomDirection(){

        Random random = new Random();

        LinkedList<Character> posibleDirections = new LinkedList<>();

        int currentY = HitBox.y / gp.tileSize;
        int currentX = HitBox.x / gp.tileSize;


        switch(direction) {

            case 'u': {

                if (!isSolid(currentY, currentX - 1)) {
                    posibleDirections.add('l');
                }
                if (!isSolid(currentY, currentX + 1)) {
                    posibleDirections.add('r');
                }
                if (!isSolid(currentY - 1, currentX)) {
                    posibleDirections.add('u');
                }


            } break;


            case 'l': {

                if (!isSolid(currentY, currentX - 1)) {
                    posibleDirections.add('l');
                }
                if (!isSolid(currentY - 1, currentX)) {
                    posibleDirections.add('u');
                }
                if (!isSolid(currentY + 1, currentX)) {
                    posibleDirections.add('d');
                }


            } break;


            case 'd': {

                if (!isSolid(currentY, currentX - 1)) {
                    posibleDirections.add('l');
                }
                if (!isSolid(currentY, currentX + 1)) {
                    posibleDirections.add('r');
                }
                if (!isSolid(currentY + 1, currentX)) {
                    posibleDirections.add('d');
                }


            }break;


            case 'r': {

                if (!isSolid(currentY, currentX + 1)) {
                    posibleDirections.add('r');
                }
                if (!isSolid(currentY - 1, currentX)) {
                    posibleDirections.add('u');
                }
                if (!isSolid(currentY + 1, currentX)) {
                    posibleDirections.add('d');
                }


            }break;

        }

        int num = random.nextInt(posibleDirections.size());

        direction = posibleDirections.get(num);

    }

    /**
     * metoda sprawdzająca czy duch koliduje z graczem, i też w jakim stanie obecnie jest duch i gracz
     */
    public void isColidingWithPlayer(){

        if(gp.player != null) {

            boolean isColiding = distanceToTarget(
                    HitBox.y
                    , HitBox.x
                    , gp.player.HitBox.y
                    , gp.player.HitBox.x) < gp.tileSize;

            if (isColiding && (isFrighten || isExitingFrighten)) {

                clearStates();
                isEaten = true;
                justGotEaten = true;
                gp.player.bonusCounter++;

                targetX = xHome / gp.tileSize;
                targetY = yHome / gp.tileSize;


                switch (gp.player.bonusCounter) {

                    case 1:
                        gp.player.score.score += 200 * gp.player.scoreMultipliyer;
                        break;

                    case 2:
                        gp.player.score.score += 400 * gp.player.scoreMultipliyer;
                        break;

                    case 3:
                        gp.player.score.score += 600 * gp.player.scoreMultipliyer;
                        break;

                    case 4:
                        gp.player.score.score += 800 * gp.player.scoreMultipliyer;
                        break;

                }

                gp.UIP.refreshInfo(0, Integer.toString(gp.player.score.score));


            }

            if (isColiding && (isScatter || isHunting) && !gp.player.isImunityActive) {

                gp.player.lives--;
                gp.resetEntities();

            }
        }


    }

    /**
     * metoda zwracająca wartośc boolean w zale3żności od tedo w jakim punkcie ekranu/mapy znajduje
     * się duch
     * @return wartość boolean czy duch nie jest na granicy planszy
     */
    public boolean isInsideOfMap(){

        return HitBox.x >= 0
                && HitBox.x <= gp.screenWidth
                && HitBox.y >= 0
                && HitBox.y <= gp.screenHeight;

    }

    /**
     * metoda obliczająca z teoremy Pitagorasa odległośc pomiędzy dwoma punktami
     * @param cY obecna współrzędna Y
     * @param cX obecna współrzędna X
     * @param tY docelowa współrzędna Y
     * @param tX docelowa współrzędna X
     * @return odległośc (double)
     */
    public double distanceToTarget(int cY, int cX, int tY, int tX){

        int yLength = Math.abs(cY - tY);
        int xLength = Math.abs(cX - tX);

        return Math.sqrt(yLength * yLength + xLength * xLength);

    }

    /**
     * metoda aktualizująca położenie ducha
     */
    public void update() {


        if((isEaten || justGotEaten) && HitBox.x == xHome && HitBox.y == yHome ){

            speedMultiplyer = 1;
            speedBuffed = false;

            clearStates();
            assignTMP();

        }

        if(sleep < 0) {

            int XCord = correctCordsX(HitBox.x / gp.tileSize);
            int YCord = correctCordsY((HitBox.y) / gp.tileSize);

            if (gp.map.ColisionTilesLayout[YCord][XCord] == 1)
                IntersectionCheck();

            //jeżeli wartość isMoving == true to wyłowujemy metodę zmieniającą położenie
            if (isMoving) move();

        }else sleep--;

        //weryfikacja czy duch styka się z graczem
        isColidingWithPlayer();

    }

    /**
     * metoda zmieniająca sprajty w zależności od stanu, spriteCounter
     */
    public void drawLabel() {

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

        //w zależności od stanu ducha ustawiany jest odpowiedni obraz
        if((isHunting || isScatter) && !isFrighten && !isEaten && !isExitingFrighten ){
            //pętla switch zmieniająca sprite w zależności od kierunku entity i numeru sprita
            switch(direction){

                case 'u':
                    switch(spriteCounter){
                        case 1: image = u1; break;
                        case 2: image = u2; break;
                        case 3: image = u3; break;
                    } break;
                case 'l':
                    switch(spriteCounter){
                        case 1: image = l1; break;
                        case 2: image = l2; break;
                        case 3: image = l3; break;
                    } break;
                case 'd':
                    switch(spriteCounter){
                        case 1: image = d1; break;
                        case 2: image = d2; break;
                        case 3: image = d3; break;
                    } break;
                case 'r':
                    switch(spriteCounter){
                        case 1: image = r1; break;
                        case 2: image = r2; break;
                        case 3: image = r3; break;
                    } break;

            }
        }else if(isEaten){

            switch(direction){

                case 'u': image = eyesTop; break;
                case 'l': image = eyesLeft; break;
                case 'd': image = eyesBot; break;
                case 'r': image = eyesRight; break;

            }

        }else if(isFrighten){

            switch(spriteCounter) {
                case 1: image = blue1; break;
                case 2: image = blue2; break;
                case 3: image = blue3; break;
            }

        }else if(isExitingFrighten){

            if(FPSCounter % 10 == 0) {
                switch (spriteCounter % 2) {
                    case 0:
                        switch (spriteCounter) {
                            case 1: image = white1;break;
                            case 2: image = white2;break;
                            case 3: image = white3;break;
                        }break;
                    case 1:
                        switch (spriteCounter) {
                            case 1: image = blue1;break;
                            case 2: image = blue2;break;
                            case 3: image = blue3;break;
                        }break;
                }
            }

        }

        //w zależności od ilości zjedzonych przez gracza duchów w jednym cyklu BlueMadnes
        //przypisywany na jedną klatkę do ducha jest sprajt z odpowiedną liczbą
        if(justGotEaten){
            switch(gp.player.bonusCounter){

                case 1: image = bonus200;break;

                case 2: image = bonus400;break;

                case 3: image = bonus600;break;

                case 4: image = bonus800;break;

            }
        }

        label.setIcon(new ImageIcon(image));
        label.setLocation(HitBox.x - gp.tileSize/4, HitBox.y - gp.tileSize/4);

    }

    /**
     * metoda tworząca lejbel ducha i go dodaje
     */
    public void SetLabel(){
        label.setLocation(HitBox.x - gp.tileSize/4, HitBox.y - gp.tileSize/4);
        label.setSize((int)(gp.tileSize * 1.5), (int)(gp.tileSize * 1.5));

        gp.add(label);
    }

    /**
     * metoda odpowiadająca za aktualizację pozycji ducha
     */
    @Override
    public void move (){

        if(HitBox.x > gp.screenWidth + gp.tileSize - speed*speedMultiplyer) HitBox.x = -1*gp.tileSize + speed*speedMultiplyer;
        if(HitBox.x < -1*gp.tileSize) HitBox.x = gp.screenWidth + gp.tileSize - speed*speedMultiplyer;
        if(HitBox.y > gp.screenHeight + gp.tileSize - speed*speedMultiplyer) HitBox.y = -1*gp.tileSize + speed*speedMultiplyer;
        if(HitBox.y < -1*gp.tileSize) HitBox.y = gp.screenHeight + gp.tileSize - speed*speedMultiplyer;

        switch(direction){
            case 'u' : HitBox.y -= speed*speedMultiplyer; break;
            case 'l' : HitBox.x -= speed*speedMultiplyer; break;
            case 'd' : HitBox.y += speed*speedMultiplyer; break;
            case 'r' : HitBox.x += speed*speedMultiplyer; break;
        }

    }

}