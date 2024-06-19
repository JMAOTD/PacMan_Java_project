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
 * klasa odpowiadająca za wyświetlenie graczowi ekranu pauzy, na którym gracz widzi napis
 * że gra jest zapauzowana, oraz może wybrać czy chce wrócic do głównego menu czy kontynuować grę
 */
public class ExitConfirmationPanel extends JPanel implements MainPanels_I {

    public JLabel pausedSighn;
    public GameFrame gameFrame;
    public JButton exit, resume;


    public ExitConfirmationPanel(GameFrame gameFrame){

        this.gameFrame = gameFrame;
        setUpPanel();

    }
    /**
     * ustawienie rozmiaru panela na podstawie już dodanego GamePanel
     * ponieważ confirmationPanel ma być nad panelem gry i ma być dopasowany
     * @param gp GamePanel
     */
    public void setSize(GamePanel gp){

        Dimension preferredSize = gp.getPreferredSize();

        this.setPreferredSize(preferredSize);
        this.setMinimumSize(preferredSize);
        this.setSize(preferredSize);

    }

    /**
     * metoda inicjalizująca wszystkie elementy zawarte w panelu jak i same parametry owego panelu
     */
    public void setUpPanel(){

        Dimension preferredSize = gameFrame.gamePanel.getPreferredSize();

        this.setPreferredSize(preferredSize);
        this.setMinimumSize(preferredSize);
        this.setSize(preferredSize);

        this.setLayout(null);

        this.setOpaque(false);

        pausedSighn = new JLabel();

        //wycentrowanie lejbela względem okna
        pausedSighn.setBounds(
                (this.getWidth() - 336)/2
                , (this.getHeight() - 330)/2
                ,336
                , 150
        );

        try {
            pausedSighn.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/interface_elements/GamePanels/PausedLabel.png")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Tworzenie komponentów
        exit = new JButton("Exit");

        //wycentrowanie przyciska względem okna
        exit.setBounds(
                (this.getWidth() - 260)/2
                , pausedSighn.getY() + pausedSighn.getHeight() + 16
                ,260
                ,74
        );

        exit.setFocusable(false);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.startMenu();
                gameFrame.deleteGamePanel();
                gameFrame.resumeGame();

            }
        });

        resume = new JButton("Resume");

        //wycentrowanie przyciska względem okna
        resume.setBounds(
                (this.getWidth() - 260)/2
                , exit.getY() + exit.getHeight() + 16
                ,260
                ,74
        );

        resume.setFocusable(false);

        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.resume();
            }
        });

        this.add(pausedSighn);
        this.add(exit);
        this.add(resume);


        this.setFocusable(true);
        this.requestFocusInWindow();

    }

}