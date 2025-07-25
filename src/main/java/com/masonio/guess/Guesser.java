package com.masonio.guess;

import com.masonio.io.WordReader;

import java.util.*;

public class Guesser {
    private final Map<Integer, Set<Character>> excluded = new HashMap<>();

    public String makeGuess(Map<Integer, Character> green, Character[] yellow) {
        green.replaceAll((key, value) -> Character.toLowerCase(value));
        green.forEach((key, value) -> {
            if (excluded.containsKey(key)) {
                excluded.get(key).remove(value);
            }
        });

        List<String> s = WordReader.getInstance().getWords()
                .stream()
                .filter(str -> {
                    //Filter for strings with excluded values (grayed letters)
                    char[] chars = str.toLowerCase().toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        if (excluded.containsKey(i) && excluded.get(i).contains(chars[i])) {
                            return false;
                        }
                    }

                    return true;
                })
                .filter(str -> {
                    //Filter for strings with green values in right space
                    char[] chars = str.toLowerCase().toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        if (green.containsKey(i) && !(green.get(i) == chars[i])) {
                            return false;
                        }
                    }

                    return true;
                })
                .filter(str -> {
                    //Filter for strings with yellow values
                    for (char c : yellow) {
                        if (!str.contains(String.valueOf(Character.toLowerCase(c)))) {
                            return false;
                        }
                    }

                    return true;
                })
                .toList();
        if (s.isEmpty()) {
            throw new NoSuchElementException("Solution appears to be impossible");
        }

        Random r = new Random();
        String str = s.get(r.nextInt(s.size()));
        return str.toUpperCase();
    }

    public void addExclusion(int key, Character value) {
        if (!excluded.containsKey(key)) {
            HashSet<Character> set = new HashSet<>();
            set.add(Character.toLowerCase(value));
            excluded.put(key, set);
        } else {
            excluded.get(key).add(Character.toLowerCase(value));
        }
    }

    public void addExclusion(Character value) {
        for (int i = 0; i < 5; i++) {
            this.addExclusion(i, value);
        }
    }
}
