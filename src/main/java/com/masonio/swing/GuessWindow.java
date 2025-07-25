package com.masonio.swing;

import com.masonio.Main;
import com.masonio.guess.Guesser;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GuessWindow extends JFrame {
    private JLabel currentGuessLabel;
    private List<JButton> guessButtons;
    private Guesser guesser;

    public GuessWindow() {
        this.setSize(500, 500);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Wonder Wordle");
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(false);
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icon.png")));
            this.setIconImage(icon.getImage());
        } catch (NullPointerException e) {
            System.out.println("Could not load the window icon");
        }

        guessButtons = new ArrayList<>();
        initComponents();
        guesser = new Guesser();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);

        currentGuessLabel = new JLabel();
        currentGuessLabel.setFont(currentGuessLabel.getFont().deriveFont(100f));
        currentGuessLabel.setText(Main.FIRST_GUESS.toUpperCase());
        this.add(currentGuessLabel, gbc);

        JPanel guessButtonPanel = new JPanel(new GridLayout(1, 6, 20, 0));
        for (int i = 0; i < 5; i++) {
            JButton button = getColorButton();
            guessButtons.add(button);
            guessButtonPanel.add(button);
        }
        gbc.gridy = 1;
        this.add(guessButtonPanel, gbc);

        JButton submitButton = createSubmitButton();
        gbc.gridy = 2;
        this.add(submitButton, gbc);
    }

    private static JButton getColorButton() {
        JButton button = new JButton();
        button.setBackground(ButtonColors.GRAY.color);
        button.setPreferredSize(new Dimension(50, 50));

        button.addActionListener(l -> {
            if (button.getBackground().equals(ButtonColors.GRAY.color)) {
                button.setBackground(ButtonColors.GREEN.color);
            } else if (button.getBackground().equals(ButtonColors.GREEN.color)) {
                button.setBackground(ButtonColors.YELLOW.color);
            } else if (button.getBackground().equals(ButtonColors.YELLOW.color)) {
                button.setBackground(ButtonColors.GRAY.color);
            } else {
                button.setBackground(ButtonColors.GRAY.color);
            }
        });

        return button;
    }

    private JButton createSubmitButton() {
        JButton submitButton = new JButton("Submit Results");
        submitButton.addActionListener(_ -> {
            Map<Integer, Character> greenMap = new HashMap<>();
            ArrayList<Character> yellowList = new ArrayList<>();
            int correct = 0;

            String word = currentGuessLabel.getText().toLowerCase();
            for (int i = 0; i < guessButtons.size(); i++) {
                if (guessButtons.get(i).getBackground().equals(ButtonColors.GREEN.color)) {
                    greenMap.put(i, word.charAt(i));
                    correct++;
                } else if (guessButtons.get(i).getBackground().equals(ButtonColors.YELLOW.color)) {
                    yellowList.add(word.charAt(i));
                    guesser.addExclusion(i, word.charAt(i));
                } else if (guessButtons.get(i).getBackground().equals(ButtonColors.GRAY.color)) {
                    guesser.addExclusion(word.charAt(i));
                }
            }

            if (correct == guessButtons.size()) {
                currentGuessLabel.setForeground(Color.ORANGE);
                submitButton.setEnabled(false);
            } else {
                guess(guesser.makeGuess(greenMap, yellowList.toArray(new Character[0])));
            }
        });

        return submitButton;
    }

    enum ButtonColors {
        GRAY(new Color(58, 58, 60)),
        GREEN(new Color(83, 141, 78)),
        YELLOW(new Color(181, 159, 59));

        private final Color color;

        ButtonColors(Color color) {
            this.color = color;
        }
    }

    private void guess(String guess) {
        currentGuessLabel.setText(guess);
        for (JButton button : guessButtons) {
            if (button.getBackground().equals(ButtonColors.YELLOW.color)) {
                button.setBackground(ButtonColors.GRAY.color);
            }
        }
    }
}
