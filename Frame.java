package Wordle2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class Frame extends JFrame implements ActionListener, KeyListener {
    CustomButton[][] buttons = new CustomButton[6][5];
    JButton resetButton;
    JButton nextButton;
    JButton saveButton;
    JButton showScoresButton;
    JLabel label;
    JLabel score;
    JPanel buttonsPanel = new JPanel();
    JPanel lowerButtons = new JPanel();
    JPanel usedLettersPanel = new JPanel();
    JLabel[] letterLabels = new JLabel[26];
    Set<Character> usedLetters;
    boolean canContinue;
    boolean gameOver;
    boolean gameWon;
    int chance;
    int letter;
    int points;
    String word;

    public Frame(int points) {
        this.points = points;
        word = new Words().getWord();
        chance = 0;
        letter = 0;
        canContinue = true;
        gameWon = false;
        usedLetters = new HashSet<>();

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(700, 600);

        label = new JLabel("WORDLE", JLabel.CENTER);
        label.setFont(new Font("MV Boli", Font.BOLD, 20));
        label.setForeground(Color.YELLOW);
        label.setOpaque(true);
        label.setBackground(Color.BLACK);

        score = new JLabel("Score: " + points, JLabel.CENTER);
        score.setFont(new Font("Helvetica", Font.BOLD, 15));
        score.setOpaque(true);
        score.setBackground(Color.PINK);

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Helvetica", Font.BOLD, 20));
        resetButton.setForeground(Color.MAGENTA);
        resetButton.setBackground(Color.BLACK);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Helvetica", Font.BOLD, 20));
        nextButton.setForeground(Color.MAGENTA);
        nextButton.setBackground(Color.BLACK);
        nextButton.setFocusable(false);
        nextButton.addActionListener(this);
        nextButton.setEnabled(false);

        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Helvetica", Font.BOLD, 20));
        saveButton.setForeground(Color.MAGENTA);
        saveButton.setBackground(Color.BLACK);
        saveButton.setFocusable(false);
        saveButton.addActionListener(this);

        showScoresButton = new JButton("Show Scores");
        showScoresButton.setFont(new Font("Helvetica", Font.BOLD, 20));
        showScoresButton.setForeground(Color.MAGENTA);
        showScoresButton.setBackground(Color.BLACK);
        showScoresButton.setFocusable(false);
        showScoresButton.addActionListener(this);

        buttonsPanel.setLayout(new GridLayout(6, 5));
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                buttons[i][j] = new CustomButton(Color.WHITE);
                buttons[i][j].setEnabled(false);
                buttons[i][j].setFont(new Font("Helvetica", Font.BOLD, 20));
                buttonsPanel.add(buttons[i][j]);
            }
        }

        lowerButtons.setLayout(new GridLayout(1, 4));
        lowerButtons.add(resetButton);
        lowerButtons.add(nextButton);
        lowerButtons.add(saveButton);
        lowerButtons.add(showScoresButton);

        this.add(lowerButtons, BorderLayout.SOUTH);
        this.add(label, BorderLayout.NORTH);
        this.add(score, BorderLayout.EAST);
        this.add(buttonsPanel, BorderLayout.CENTER);

        usedLettersPanel.setLayout(new GridLayout(2, 13));
        for (int i = 0; i < 26; i++) {
            letterLabels[i] = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
            letterLabels[i].setFont(new Font("MV Boli", Font.BOLD, 20));
            letterLabels[i].setOpaque(true);
            letterLabels[i].setBackground(Color.LIGHT_GRAY);
            usedLettersPanel.add(letterLabels[i]);
        }
        this.add(usedLettersPanel, BorderLayout.WEST);

        this.addKeyListener(this);
        this.setFocusable(true);
        this.setTitle("Wordle");
        this.repaint();
        this.revalidate();
        this.setLocationRelativeTo(null);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            int code = e.getKeyCode();
            if (code >= 65 && code <= 90) { // Letters A-Z
                if (canContinue && letter < 5) {
                    buttons[chance][letter].setText(String.valueOf((char) code));
                    letter++;
                }
            } else if (code == 10) { // Enter key
                if (chance < 6 && letter == 5) {
                    String wordTyped = "";
                    for (int i = 0; i < 5; i++) {
                        wordTyped += buttons[chance][i].getText();
                    }
                    if (new Words().wordExists(wordTyped.toLowerCase())) {
                        label.setForeground(Color.ORANGE);
                        label.setText("WORDLE");
                        updateUsedLetters(wordTyped);
                        checkWord(wordTyped.toLowerCase());
                        checkWinner();
                        letter = 0;
                        chance++;
                        canContinue = true;
                    } else {
                        label.setForeground(Color.RED);
                        label.setText("Word Doesn't Exist!");
                    }
                } else if (letter < 5) {
                    label.setForeground(Color.RED);
                    label.setText("Less Letters!");
                }
            } else if (code == 8) { // Backspace
                if (letter > 0) {
                    letter--;
                    canContinue = true;
                    buttons[chance][letter].setText("");
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}


    private void updateUsedLetters(String wordTyped) {
        for (char c : wordTyped.toCharArray()) {
            usedLetters.add(c);
        }
        updateLetterColors();
    }

    private void updateLetterColors() {
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            if (usedLetters.contains(letter)) {
                letterLabels[i].setBackground(Color.GRAY);
            } else {
                letterLabels[i].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    public void checkWord(String toBeChecked) {
        boolean[] correctPositions = new boolean[5];
        boolean[] correctLetters = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (toBeChecked.charAt(i) == word.charAt(i)) {
                buttons[chance][i].setBackgroundColor(Color.GREEN);
                correctPositions[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (!correctPositions[i]) {
                for (int j = 0; j < 5; j++) {
                    if (!correctPositions[j] && !correctLetters[j] && toBeChecked.charAt(i) == word.charAt(j)) {
                        buttons[chance][i].setBackgroundColor(Color.YELLOW);
                        correctLetters[j] = true;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (buttons[chance][i].getBackgroundColor() != Color.GREEN && buttons[chance][i].getBackgroundColor() != Color.YELLOW) {
                buttons[chance][i].setBackgroundColor(Color.GRAY);
            }
        }

        for (int i = 0; i < 5; i++) {
            buttons[chance][i].revalidate();
            buttons[chance][i].repaint();
        }

        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    public void checkWinner() {
        if (chance == 5) {
            label.setBackground(Color.RED);
            label.setForeground(Color.BLACK);
            label.setText("Try next time! The word is: " + word);
            gameOver = true;
            nextButton.setEnabled(true); // Enable the "Next" button after the game is lost
        }

        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (buttons[chance][i].getBackgroundColor() == Color.GREEN) {
                count++;
            }
        }

        if (count == 5) {
            label.setBackground(Color.GREEN);
            label.setForeground(Color.BLACK);
            label.setText("Yay! YOU WON!");
            gameOver = true;
            gameWon = true; // Set gameWon to true when the player wins
            nextButton.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            this.dispose();
            new Frame(0);
        } else if (e.getSource() == nextButton) {
            int newPoints = gameWon ? points + 1 : points;
            this.dispose();
            new Frame(newPoints);
        } else if (e.getSource() == saveButton) {
            String playerName = JOptionPane.showInputDialog(this, "Enter your name:");
            saveHighScore(playerName, points);
        } else if (e.getSource() == showScoresButton) {  // Add this block
            showHighScores();
        }
    }


    private void saveHighScore(String playerName, int points) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscores.txt", true))) {
            writer.write(playerName + ": " + points);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

      private void showHighScores() {
        StringBuilder scores = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("highscores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTextArea textArea = new JTextArea(scores.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Frame(0));
    }
}
