package com.masonio;

import com.masonio.io.WordReader;
import com.masonio.swing.GuessWindow;

import javax.swing.*;

public class Main {
    private static final String VERSION = "1.0.0";
    public static final String FIRST_GUESS = "OCEAN";

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((_, throwable) -> {
            JOptionPane.showMessageDialog(
                    null,
                    "Uncaught error: " + throwable.toString(),
                    "Fatal Error",
                    JOptionPane.ERROR_MESSAGE
            );
        });


        System.out.println("Starting Version " + VERSION + "...");

        WordReader.getInstance().read(Main.class.getClassLoader().getResourceAsStream("words.txt"));
        SwingUtilities.invokeLater(() -> {
            GuessWindow window = new GuessWindow();
            window.setVisible(true);
        });

    }
}