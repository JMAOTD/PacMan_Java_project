package main.technical;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa odpowiadająca za skalowanie obrazu, jej celem jest skrócenie kodu i zwiększenie czytelności
 */
public class ImageScaler {

    /**
     * metoda zwracająca przeskalowany obraz przekazany jako ardument
     * @param image Image
     * @param scale skala double
     * @return przeskalowany Image
     */
    public Image imageScaler(Image image, double scale){

        return image.getScaledInstance((int)(image.getWidth(null) * scale)
                , (int)(image.getHeight(null)*scale)
                , Image.SCALE_SMOOTH);

    }

    /**
     * metoda zwracająca ImageIcon stworzony z obrazu przekazanego
     * jako argument oraz powiększonego o skalę podaną również jako argument
     * @param image Image
     * @param scale skala double
     * @return przeskalowany ImageIcon
     */
    public ImageIcon Scaler(Image image, double scale){

        ImageIcon tmpIcon;
        Image tmpImage = image.getScaledInstance((int)(image.getWidth(null) * scale)
                , (int)(image.getHeight(null)*scale)
                , Image.SCALE_SMOOTH);

        tmpIcon = new ImageIcon(tmpImage);

        return tmpIcon;

    }


}
