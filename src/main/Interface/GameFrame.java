package main.Interface;

import main.Interface.MainPanels.*;
import main.entity.Ghost;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;


/**
 * Klasa odpowiadająca za organizację oraz zarządzanie wszystkimi panelami niezbędnymi dla gry
 */
public class GameFrame extends JFrame {

    /**
     * Lista do której dodawane są wszystkie panele
     */
    public LinkedList<MainPanels_I> panels;

    /**
     * panel wyjścia z trwającej gry
     */
    public ExitConfirmationPanel exitConfirmationPanel;

    /**
     * panel na zakończenie gry
     */
    public GameOverPanel gameOverPanel;

    /**
     * panel wyboru planszy i rozpoczęcia gry
     */
    public NewGamePanel newGamePanel;

    /**
     * tytułowy panel gry
     */
    public StartPanel startPanel;

    /**
     * panel gry z planszą i UI
     */
    public GamePanel gamePanel;

    /**
     * panel z HighScores
     */
    public HighScoresPanel highScoresPanel;

    //2 zamki niezbędne do odpowiednich zapauzowań gry
    private final Object pauseLock = new Object();
    private final Object insidePauseLock = new Object();

    //2 zmienne boolean również do pauzowania gry
    private boolean isPaused = false;
    private boolean isPausedInside = false;


    public GameFrame(){


        //inicjalizacja Paneli i listy
        setPanelsList();


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        this.getContentPane().setBackground(Color.BLACK);
        this.setLayout(null);
        this.setTitle("PacMan 2D");
        this.setResizable(true);

        startMenu();

        resize();

        this.setVisible(true);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                allighnComponents();
            }
        });

    }

    /**
     * wyrównanie dodanych do Frejma komponentów żeby znajdowały się po środku
     */
    public void allighnComponents(){

        Insets insets = this.getInsets();

        for(Component c : this.getContentPane().getComponents()){

            if(c.isVisible()){

                c.setLocation(
                        (this.getWidth() - c.getWidth() - (insets.left + insets.right))/2
                        ,(this.getHeight() - c.getHeight() - (insets.top + insets.bottom))/2
                );

            }

        }

    }

    /**
     * metoda dopasowująca rozmiar frejma do zawartości
     */
    public void resize(){

        //pobranie wartości dodanych odstępów
        //żeby panele nie były obcięte ponieważ używany jest null Layout
        Insets insets = this.getInsets();
        int frameWidth = 0;
        int frameHeight = 0;

        //dopasowanie rozmiaru Frejma pod zawartość obecnie dodaną
        for(Component c : this.getContentPane().getComponents()) {

            Dimension preferredSize = c.getPreferredSize();
            frameWidth = preferredSize.width + insets.left + insets.right;
            frameHeight = preferredSize.height + insets.top + insets.bottom;

        }

        Dimension dimension = new Dimension(frameWidth, frameHeight);

        this.setSize(dimension);
        this.setMinimumSize(dimension);

        this.pack();

        revalidate();
        repaint();

    }

    /**
     * ustawienie listy paneli
     */
    public void setPanelsList(){

        gameOverPanel = new GameOverPanel(this);
        gameOverPanel.setBounds(
                0
                ,0
                ,gameOverPanel.getWidth()
                ,gameOverPanel.getHeight()
        );

        newGamePanel = new NewGamePanel(this);
        newGamePanel.setBounds(
                0
                ,0
                ,newGamePanel.getWidth()
                , newGamePanel.getHeight()
        );


        startPanel = new StartPanel(this);
        startPanel.setBounds(
                0
                ,0
                ,startPanel.getWidth()
                ,startPanel.getHeight()
        );

        highScoresPanel = new HighScoresPanel(this);
        highScoresPanel.setBounds(
                0
                ,0
                ,highScoresPanel.getWidth()
                ,highScoresPanel.getHeight()
        );

        panels = new LinkedList<>();

        panels.add(gameOverPanel);
        panels.add(newGamePanel);
        panels.add(startPanel);
        panels.add(highScoresPanel);

    }

    /**
     * metoda wywołująca menu
     */
    public void startMenu(){

        for(int i = 0; i < panels.size(); i++) this.remove((Component) panels.get(i));
        this.add(startPanel);
        resize();

    }

    /**
     * metoda wywołująca ekran nowej gry i wyboru rozmiaru planszy
     */
    public void newGame(){

        resumeGame();
        for(int i = 0; i < panels.size(); i++) this.remove((Component) panels.get(i));
        this.add(newGamePanel);
        resize();

    }

    /**
     * metoda wywołująca okno z HighScores
     */
    public void highScores(){

        for(int i = 0; i < panels.size(); i++) this.remove((Component) panels.get(i));
        highScoresPanel.loadContent();
        this.add(highScoresPanel);
        resize();

    }

    /**
     * Metoda wywołująca okno GameOver
     */
    public void gameOver(){

        for(int i = 0; i < panels.size(); i++) this.remove((Component) panels.get(i));
        panels.remove(gamePanel);

        gameOverPanel.score = gamePanel.gp.player.score.score;
        gameOverPanel.mapType = "map: " + gamePanel.gp.height + "x" + gamePanel.gp.width + "; score: ";
        gameOverPanel.DisplayScore();

        deleteGamePanel();

        this.add(gameOverPanel);
        resize();

    }

    /**
     * Metoda rozpoczynająca grę
     * @param height parametr odpowiadającaa za rozmiar planszy gry
     */
    public void startGame(int height){

        for(int i = 0; i < panels.size(); i++) this.remove((Component) panels.get(i));

        gamePanel = new GamePanel(this, height);
        panels.add(gamePanel);

        this.add(gamePanel);
        resize();
        gamePanel.gp.requestFocusInWindow();

    }

    /**
     * metoda wywołująca okno pauzy
     */
    public void exitToMenu(){

        pauseGame();
        for(int i = 0; i < panels.size(); i++) this.remove((Component) panels.get(i));

        if(exitConfirmationPanel != null) exitConfirmationPanel = null;

        exitConfirmationPanel = new ExitConfirmationPanel(this);
        exitConfirmationPanel.setSize(gamePanel);
        exitConfirmationPanel.setBounds(
                0
                ,0
                ,exitConfirmationPanel.getWidth()
                ,exitConfirmationPanel.getHeight()
        );

        panels.add(exitConfirmationPanel);

        this.add(exitConfirmationPanel);
        this.add(gamePanel);

        resize();

    }

    /**
     * metoda powrotu do trwającej gry z okna pauzy
     */
    public void resume(){

        for(int i = 0; i < panels.size(); i++) this.remove((Component) panels.get(i));
        this.add(gamePanel);
        resize();
        gamePanel.gp.requestFocusInWindow();
        gamePanel.gp.GT.isStopped = false;
        resumeGame();

    }


    /**
     * Metoda zamykająca JFrejm i cały program
     */
    public void exitTheGame(){
        //zamknięcie Frejma
        this.dispose();
        //całkowite zamknięcie aplikacji
        System.exit(0);
    }

    /**
     * metoda usuwająca objekty z game panel w momencie usunięcia panela
     * żeby po zkończeniu gry czy wyjściu z gry ona nadal nie trwała
     */
    public void deleteGamePanel(){

        gamePanel.gp.gameCore = null;

        gamePanel.gp.player = null;

        for(Ghost g : gamePanel.gp.ghosts) g = null;

        gamePanel.gp.GT = null;
        gamePanel.gp = null;

        gamePanel = null;

    }

    /**
     * Synchronizacja wątków celem zapauzowania gry
     * wyjście do ekranu pauzy, albo moment zjedzenia duszka (jak w oryginalnym pac-manie)
     */
    public void pauseGame() {

        synchronized (pauseLock) {
            isPaused = true;
        }

    }

    /**
     * wznowienie gry
     */
    public void resumeGame() {

        synchronized (pauseLock) {
            isPaused = false;
            pauseLock.notifyAll();
        }

    }

    /**
     * metoda weryfikująca czy klucz został przekazany
     * @throws InterruptedException
     */
    public void checkPaused() throws InterruptedException {

        synchronized (pauseLock) {
            while (isPaused) {
                pauseLock.wait();
            }
        }

    }

    /**
     * zapauzowanie gry przy użyciu drugiego klucza
     */
    public void insidePauseGame() {

        synchronized (insidePauseLock) {
            isPausedInside = true;
        }

    }

    /**
     * wznowienie gry
     */
    public void insideResumeGame() {

        synchronized (insidePauseLock) {
            isPausedInside = false;
            insidePauseLock.notifyAll();
        }

    }

    /**
     * metoda weryfikująca czy klucz został przekazany
     * @throws InterruptedException
     */
    public void checkInsidePaused() throws InterruptedException {

        synchronized (insidePauseLock) {
            while (isPausedInside) {
                insidePauseLock.wait();
            }
        }

    }

}
