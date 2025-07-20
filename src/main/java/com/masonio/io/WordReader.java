package com.masonio.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordReader {
    private List<String> words;
    private static final WordReader instance = new WordReader();

    private WordReader() {
        words = new ArrayList<>();
    }

    public void read(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                words.add(nextLine);
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("Encountered a fatal error!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public List<String> getWords() {
        return words;
    }

    public static WordReader getInstance() {
        return instance;
    }
}
