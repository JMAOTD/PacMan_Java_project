package main;

import main.Interface.GameFrame;

import javax.swing.*;

public class PacMan2D {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new GameFrame());

    }

}