package main.high_scores;

import java.io.*;
import java.util.ArrayList;

/**
 * klasa odpowiadająca za przechowywanie i zarządzanie HighScores
 */
public class HighScoresManager {

    public ArrayList<HighScore> highScores;

    public HighScoresManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    /**
     * metoda dodania nowego objektu HighScore do arrayListy
     * @param highScore objekt HighScore
     */
    public void addHighScore(HighScore highScore) {
        highScores.add(highScore);
        sortHighScores();
    }

    /**
     * sortowanie malejące
     */
    private void sortHighScores() {
        highScores.sort((o1, o2) -> {
            return Integer.compare(o2.score, o1.score);
        });
    }

    /**
     * metoda zapisująca obecny stan arrayLista przy pomocy serializacji do pliku
     */
    public void saveHighScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/HighScores.txt"))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * metoda inicjalizująca ArrayList z HighScores danymi przechowanymi w pliku
     */
    public void loadHighScores() {

        File file = new File("resources/HighScores.txt");
        //jeżeli plik jest pusty to ArrayList nie jest inicjalizowany danymi z tej listy
        if (file.length() == 0) {
            return;
        }

        //pobranie objekty zawierającego ArrayList i zainicjalizowanie tymi danymi
        //arrayList w obecnym objekcie
        //automatyczne posortowanie
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            highScores = (ArrayList<HighScore>) ois.readObject();
            sortHighScores();
        }
        catch (EOFException e) {}
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * metoda zwracająca ArrayList z HighScores
     * @return ArrayLis
     */
    public ArrayList<HighScore> getHighScores() {
        return highScores;
    }

}
