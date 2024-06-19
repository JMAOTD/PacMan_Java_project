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
 * klasa odpowiadająca za prezentowanie użytkownikowi ekranu
 * wyboru planszy
 */
public class NewGamePanel extends JPanel implements MainPanels_I {


    public JLabel background;
    public GameFrame gameFrame;
    public JButton exit, size1, size2, size3, size4, size5;


    //konstruktor
    public NewGamePanel(GameFrame gameFrame){

        this.gameFrame = gameFrame;
        setUpPanel();

    }

    /**
     * metoda inicjalizująca wszystkie objekty i ustawiająca parametry
     * panelu oraz zawartych w nim komponentów
     */
    public void setUpPanel(){

        this.setLayout(null);

        //Ustawienia panela
        this.setPreferredSize(new Dimension(1280, 720));
        this.setMinimumSize(new Dimension(1280, 720));
        this.setSize(new Dimension(1280, 720));

        //Inicjalizacja i ustawienie wszystkich przycisków
        exit = new JButton("Exit");
        exit.setBounds(24, 24, 198, 58);
        exit.setFocusable(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startMenu();
            }
        });

        size1 = new JButton("12x16");
        size1.setBounds(64, 138, 276, 76);
        size1.setFocusable(false);
        size1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startGame(12);
            }
        });

        size2 = new JButton("15x20");
        size2.setBounds(64, 230, 276, 76);
        size2.setFocusable(false);
        size2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startGame(15);
            }
        });

        size3 = new JButton("18x24");
        size3.setBounds(64, 322, 276, 76);
        size3.setFocusable(false);
        size3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startGame(18);
            }
        });

        size4 = new JButton("21x28");
        size4.setBounds(64, 414, 276, 76);
        size4.setFocusable(false);
        size4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startGame(21);
            }
        });

        size5 = new JButton("31x28");
        size5.setBounds(64, 506, 276, 76);
        size5.setFocusable(false);
        size5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startGame(31);
            }
        });

        //ustawienie tła
        background = new JLabel();
        background.setBounds(0, 0, 1280, 720);
        try {
            background.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/interface_elements/GamePanels/NewGamePanel.png")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Dodanie komponentów do panelu
        this.add(exit);
        this.add(size1);
        this.add(size2);
        this.add(size3);
        this.add(size4);
        this.add(size5);
        this.add(background);

        this.setFocusable(true);
        this.requestFocusInWindow();

    }

}
