package main.inputs;

import main.Interface.SubPanels.GamePanelMain;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Klasa odpowiadająca za bycie KeyListenerem
 */
public class KeyBinds implements KeyListener {

    //zmienna która będzie aktualizowana w razie naciśnięcia lub puszczenia
    //klawiszy na klawiaturze
    public boolean UpPressed, LeftPressed, DownPressed, RightPressed;

    GamePanelMain gp;

    public KeyBinds(GamePanelMain gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // jeżeli wciśnięty klawisz UP
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            UpPressed = true;
        }

        // jeżeli wciśnięty klawisz LEFT
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            LeftPressed = true;
        }

        // jeżeli wciśnięty klawisz DOWN
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            DownPressed = true;
        }

        // jeżeli wciśnięty klawisz LEFT
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            RightPressed = true;
        }

        // jeżeli wcisnąć Esc
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameFrame.exitToMenu();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        // jeżeli wciśnięty klawisz UP
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            UpPressed = false;
        }

        // jeżeli wciśnięty klawisz LEFT
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            LeftPressed = false;
        }

        // jeżeli wciśnięty klawisz DOWN
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            DownPressed = false;
        }

        // jeżeli wciśnięty klawisz LEFT
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            RightPressed = false;
        }

    }

}