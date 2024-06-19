package main.Interface.MainPanels;

import main.Interface.GameFrame;
import main.high_scores.HighScore;
import main.high_scores.HighScoresManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * klasa odpowiadająca za prezentację graczowi wcześniej zapisanych wyników
 * wraz z imieniem (maks 10 symboli) samym wynikiem oraz rodzajem mapy (np 12x16)
 */
public class HighScoresPanel extends JPanel implements MainPanels_I {

    public JLabel background;
    public GameFrame gameFrame;
    public JButton exit;
    JScrollPane listOfScores;
    JList list;

    //list model do wyświetlania bardziej skomplikowanych struktur danych
    private DefaultListModel<HighScore> listModel;

    //utworzenie objektu zarządzającego HighScoresList
    public HighScoresManager scoresManager;


    //Konstruktor
    public HighScoresPanel(GameFrame gameFrame){

        this.gameFrame = gameFrame;
        setUpPanel();

    }

    /**
     * metoda odświeżenie listy
     */
    public void loadContent(){

        listModel.clear();

        for (HighScore highScore : scoresManager.getHighScores()) {
            listModel.addElement(highScore);
        }

        revalidate();
        repaint();

    }

    /**
     * metoda zainicjowania i ustawienia wszystkich komponentów
     */
    public void setUpPanel(){

        this.setLayout(null);

        //Ustawienia panelu
        this.setPreferredSize(new Dimension(1280, 720));
        this.setMinimumSize(new Dimension(1280, 720));
        this.setSize(new Dimension(1280, 720));

        //inicjalizacja komponentów
        exit = new JButton("Exit");
        exit.setBounds(24, 24, 198, 58);
        exit.setFocusable(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startMenu();
            }
        });

        background = new JLabel();
        background.setBounds(0, 0, 1280, 720);
        try {
            background.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/interface_elements/GamePanels/HighScoresPanel.png")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        scoresManager = new HighScoresManager();

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        Font font = new Font(Font.DIALOG, Font.BOLD, 25);
        list.setFont(font);
        listOfScores = new JScrollPane(list);
        listOfScores.setBounds(366, 218, 619, 473);

        // Dodanie komponentów do panelu
        this.add(listOfScores);
        this.add(exit);
        this.add(background);

        loadContent();

        this.setFocusable(true);
        this.requestFocusInWindow();

    }

}