package main.high_scores;

import java.io.Serializable;

/**
 * klasa będąca szablonem HighScore
 */
public class HighScore implements Serializable {

    public String mapType;
    public int score;
    public String name;

    public HighScore(String mapType, int score, String name){

        this.mapType = mapType;
        this.score = score;
        this.name = name;

    }

    @Override
    public String toString() {
        return mapType + " " + score + " " + name;
    }
}
