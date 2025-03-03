package Wordle2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Words {
    private static final String WORDS_FILE = "words.txt";
    private String[] toGetWord;

    public Words() {
        toGetWord = loadWordList();
    }

    private String[] loadWordList() {
        try {
            File file = new File(WORDS_FILE);
            BufferedReader br = new BufferedReader(new FileReader(file));
            return br.lines().toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public String getWord() {
        Random rand = new Random();
        return toGetWord[rand.nextInt(toGetWord.length)];
    }

    public boolean wordExists(String word) {
        for (String w : toGetWord) {
            if (w.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}
