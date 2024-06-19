package main.Interface;

import main.technical.ImageScaler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Klasa odpowiedzialna za pobranie z folderu obrazy reprezentujące pojedyncze symbole i przypisanie ich do HashMapy z
 * kluczem będącym tym samym symbolem
 */
public class FontIcons {

    public HashMap<Character, ImageIcon> iconMap;
    ImageScaler IS;
    String allFontSymbols;
    ImageIcon empty;

    public FontIcons(){
        IS = new ImageScaler();

        //wszystkie symbole które znajdują się w narysowanej przeze mnie ćcionce
        allFontSymbols = "!\"#$%&'(),./0123456789:;?@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";

        empty = getImageIcon(78);
        iconMap = new HashMap<>();
        initHashMap();

    }

    /**
     * metoda pobierająca wszystkie wcześniej przygotowane obrazy
     * @param index wskazuje na numer pliku do pobrania
     * @return
     */
    public ImageIcon getImageIcon(int index) {

        Image tmpImage = null;

        try {

            tmpImage = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream(
                            "/Interface_elements/font_numerated/font_" + index + ".png"))
            );

        } catch (IOException e) {}

        return new ImageIcon(tmpImage);

    }

    /**
     * metoda przypisująca w hashMapie symbol do jego graficznej reprezentacji
     */
    public void initHashMap(){

        for(int i = 0; i < 79; i++){

            iconMap.put(allFontSymbols.charAt(i), getImageIcon(i));

        }

    }

}
