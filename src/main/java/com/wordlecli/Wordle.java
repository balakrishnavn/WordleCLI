package com.wordlecli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Wordle {

    private static final int MAX_ATTEMPTS = 5;
    private static final int WORD_LENGTH = 5;
    private static final String RESOURCE_WORDLIST = "/words.txt"; // file in src/main/resources

    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        List<String> words = loadWords();
        if (words.isEmpty()) {
            System.err.println("Error: Word list is empty or missing in resources!");
            return;
        }

        String answer = chooseRandomWord(words);
        runGame(answer);
    }

    private static void runGame(String answer) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🎉 Welcome to Wordle (CLI version)!");
        System.out.println("Guess the 5-letter word. You have " + MAX_ATTEMPTS + " attempts.\n");

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("Attempt " + attempt + "/" + MAX_ATTEMPTS + ": ");
            String guess = scanner.nextLine().trim().toLowerCase();

            if (!isValidGuess(guess)) {
                System.out.println("Guess must be exactly " + WORD_LENGTH + " letters.");
                attempt--;
                continue;
            }

            String feedback = generateFeedback(guess, answer);
            System.out.println(feedback);

            if (guess.equals(answer)) {
                System.out.println("Correct! You won in " + attempt + " attempts! 🎉");
                return;
            }
        }

        System.out.println("Out of guesses! The word was: " + answer.toUpperCase());
    }

    public static List<String> loadWords() {
        List<String> words = new ArrayList<>();

        try (InputStream is = Wordle.class.getResourceAsStream(RESOURCE_WORDLIST);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                System.err.println("Resource file not found: " + RESOURCE_WORDLIST);
                return words;
            }

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.length() == WORD_LENGTH) {
                    words.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading word list from resources: " + e.getMessage());
        }

        return words;
    }

    static String chooseRandomWord(List<String> words) {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public static boolean isValidGuess(String guess) {
        return guess != null && guess.length() == WORD_LENGTH;
    }

    public static String generateFeedback(String guess, String answer) {
        boolean[] greenPositions = new boolean[WORD_LENGTH];
        Map<Character, Integer> letterCounts = new HashMap<>();

        for (char c : answer.toCharArray()) {
            letterCounts.put(c, letterCounts.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                greenPositions[i] = true;
                letterCounts.put(guess.charAt(i), letterCounts.get(guess.charAt(i)) - 1);
            }
        }

        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            char g = guess.charAt(i);
            if (greenPositions[i]) {
                feedback.append(GREEN).append(Character.toUpperCase(g)).append(RESET).append(" ");
            } else if (letterCounts.getOrDefault(g, 0) > 0) {
                feedback.append(YELLOW).append(Character.toUpperCase(g)).append(RESET).append(" ");
                letterCounts.put(g, letterCounts.get(g) - 1);
            } else {
                feedback.append(Character.toUpperCase(g)).append(" ");
            }
        }

        return feedback.toString().trim();
    }
}