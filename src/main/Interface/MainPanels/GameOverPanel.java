package main.Interface.MainPanels;

import main.Interface.GameFrame;
import main.technical.ImageScaler;
import main.high_scores.HighScore;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

/**
 * Ta klasa odpowiada za wyświetlenie gameOver screen, zawierającego 2 przyciski,
 * pole tekstowe do wprowadzenia imienia, 7 lejbeli pokazujących osiągnięty wynik i tło
 */
public class GameOverPanel extends JPanel implements MainPanels_I {

    public JLabel background;
    public GameFrame gameFrame;
    public JButton exit, submit;
    JTextArea nameField;
    public int score;
    public String mapType;

    public JLabel[] scoreDisplay;
    public Icon[] scoreIcons;
    public Icon empty;



    public GameOverPanel(GameFrame gameFrame){

        this.gameFrame = gameFrame;
        setUpPanel();

    }

    /**
     * metoda inicjalizująca ikony
     */
    public void setIcons(){

        ImageScaler is = new ImageScaler();
        Image tmp;

        scoreIcons = new Icon[10];

        //zainicjowanie icon reprezentujących cyfry
        for(int i = 0; i < scoreIcons.length; i++) {

            try {

                tmp = ImageIO.read(
                        Objects.requireNonNull(getClass().getResourceAsStream(
                                "/interface_elements/font_numerated/font_"+(i+12)+".png"))
                );
                tmp = is.imageScaler(tmp, 4);
                scoreIcons[i] = new ImageIcon(tmp);

            } catch (IOException e) {}

        }

        //zainicjowanie pustej icony
        try {

            tmp = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream(
                            "/interface_elements/font_numerated/font_78.png"))
            );
            tmp = is.imageScaler(tmp, 4);
            empty = new ImageIcon(tmp);

        } catch (IOException e) {}


    }

    /**
     * metoda tworząca i rozmieszczająca lejbele odpowiedzialne za
     */
    public void setScoreDisplayDisplay(){

        scoreDisplay = new JLabel[7];

        for(int i = 0; i < scoreDisplay.length; i++){

            scoreDisplay[i] = new JLabel();

            scoreDisplay[i].setBounds(
                    641 + (i*48)
                    ,290
                    ,48
                    ,64
            );

            scoreDisplay[i].setBackground(Color.BLACK);

            this.add(scoreDisplay[i]);

        }

    }

    /**
     * metoda wyświetlająca wynik gracza
     */
    public void DisplayScore(){

        int scoreTMP = score;
        int digit;

        for(int i = scoreDisplay.length - 1; i >= 0; i--){

            if(scoreTMP >= 0){

                digit = scoreTMP % 10;
                scoreDisplay[i].setIcon(scoreIcons[digit]);
                scoreTMP /= 10;

            }else scoreDisplay[i].setIcon(empty);

        }

    }

    /**
     * metoda dodająca nowy zapis do tablicy HighScores
     */
    public void saveTheHighScore(){

        String name = "; name: " + nameField.getText() + ";";

        gameFrame.highScoresPanel.scoresManager.addHighScore(new HighScore(" " + mapType, score, name));
        gameFrame.highScoresPanel.scoresManager.saveHighScores();

    }

    /**
     * metoda inicjalizująca wszystkie elementy zawarte w panelu jak i same parametry owego panelu
     */
    public void setUpPanel(){

        this.setLayout(null);

        // Ustawienia panelu
        this.setPreferredSize(new Dimension(1280, 720));
        this.setMinimumSize(new Dimension(1280, 720));
        this.setSize(new Dimension(1280, 720));

        setIcons();
        setScoreDisplayDisplay();

        nameField = new JTextArea();

        //maksymalna długośc imienia
        int maxLength = 10;

        //utworzenie własnego filtru żeby gracz mógł wprowadzić maksymalnie 10 znaków
        //w polu imienia
        DocumentFilter filter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) {
                    return;
                }
                if ((fb.getDocument().getLength() + string.length()) <= maxLength) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    return;
                }
                if ((fb.getDocument().getLength() - length + text.length()) <= maxLength) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        };

        //Dodanie DocumentFilter do JTextArea
        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(filter);

        nameField.setBounds(
                641
                ,370
                ,336
                ,64
        );

        nameField.setFont(new Font(Font.DIALOG, Font.BOLD, 50));
        nameField.setColumns(10);
        nameField.setRows(1);

        //Inicjalizacja komponentów
        exit = new JButton("Exit");
        exit.setBounds(24, 24, 198, 58);
        exit.setFocusable(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startMenu();
            }
        });

        submit = new JButton("Submit");
        submit.setBounds(510, 456, 260, 74);
        submit.setFocusable(false);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                saveTheHighScore();
                nameField.setText("");

                gameFrame.startMenu();

            }
        });

        background = new JLabel();
        background.setBounds(0, 0, 1280, 720);
        try {
            background.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/interface_elements/GamePanels/GameOverPanel.png")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Dodanie komponentów do panelu
        this.add(nameField);
        this.add(exit);
        this.add(submit);
        this.add(background);

        this.setFocusable(true);
        this.requestFocusInWindow();

    }

}