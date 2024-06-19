package main.Interface.MainPanels;

import main.Interface.GameFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * Klasa odpowiadająca za demonstrację startowego ekranu, z którego gracz może przejść do innych paneli
 */
public class StartPanel extends JPanel implements MainPanels_I {


    public GameFrame gameFrame;
    public JLabel background;
    public JButton exit, newGame, highScores;

    public StartPanel(GameFrame gameFrame){

        this.gameFrame = gameFrame;
        setUpPanel();

    }

    /**
     * metoda ustawiająca wszystkie parametry startPanel
     */
    public void setUpPanel(){

        this.setLayout(null);

        // Ustawienia panelu
        this.setPreferredSize(new Dimension(1280, 720));
        this.setMinimumSize(new Dimension(1280, 720));
        this.setSize(new Dimension(1280, 720));

        //inicjalizacja przyciska odpowiadającego za wyjście z gry
        exit = new JButton("Exit");
        exit.setBounds(24, 24, 198, 58);
        exit.setFocusable(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.exitTheGame();
            }
        });

        //inicjalizacja przyciska zabierającego użytkownika do panelu z wyborem planszy
        newGame = new JButton("New Game");
        newGame.setBounds(490, 360, 300, 64);
        newGame.setFocusable(false);
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.newGame();
            }
        });

        //inicjalizacja przyciska zabierającego użytkownika do panelu z HighScores
        highScores = new JButton("High Scores");
        highScores.setBounds(490, 440, 300, 64);
        highScores.setFocusable(false);
        highScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.highScores();
            }
        });

        //inicjalizacja lejbela będącego tłem
        background = new JLabel();
        background.setBounds(0, 0, 1280, 720);
        try {
            background.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/interface_elements/GamePanels/StartPanel.png")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Dodanie komponentów do panelu
        //w kolejności od przodu do tyłu
        this.add(exit);
        this.add(newGame);
        this.add(highScores);
        this.add(background);

        this.setFocusable(true);
        this.requestFocusInWindow();

    }

}