package com.wordlecli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleJUnitTest {

    @Test
    @DisplayName("Feedback correctly marks green, yellow, and gray letters")
    public void testGenerateFeedbackGreenAndYellow() {
        String answer = "water";
        String guess = "otter";

        String feedback = Wordle.generateFeedback(guess, answer);
        String[] parts = feedback.split(" ");

        // O -> gray
        assertFalse(parts[0].contains("\u001B[32m") || parts[0].contains("\u001B[33m"));
        // T -> gray (no extra T left, first T in answer is used in green)
        assertFalse(parts[1].contains("\u001B[32m") || parts[1].contains("\u001B[33m"));
        // T -> green (third letter matches)
        assertTrue(parts[2].contains("\u001B[32m"));
        // E -> green
        assertTrue(parts[3].contains("\u001B[32m"));
        // R -> green
        assertTrue(parts[4].contains("\u001B[32m"));
    }

    @Test
    @DisplayName("Feedback correctly handles duplicate letters")
    public void testGenerateFeedbackDuplicateLetters() {
        String answer = "paper";
        String guess = "pappy";

        String feedback = Wordle.generateFeedback(guess, answer);

        // Check that the first P is green
        assertTrue(feedback.contains("\u001B[32mP\u001B[0m"));
        // Check that A is green
        assertTrue(feedback.contains("\u001B[32mA\u001B[0m"));
        // Second P is green (third letter)
        assertTrue(feedback.contains("\u001B[32mP\u001B[0m"));
        // Extra P (fourth letter) is gray (not green or yellow)
        assertFalse(feedback.contains("\u001B[33mP\u001B[0m")); // no yellow
        // Y is gray
        assertFalse(feedback.contains("\u001B[32mY\u001B[0m")); // no green
        assertFalse(feedback.contains("\u001B[33mY\u001B[0m")); // no yellow
    }

    @Test
    @DisplayName("Valid guess must be exactly 5 letters")
    public void testValidGuess() {
        assertTrue(Wordle.isValidGuess("water"));
        assertFalse(Wordle.isValidGuess("dog"));
        assertFalse(Wordle.isValidGuess("horses"));
        assertFalse(Wordle.isValidGuess(null));
    }

    @Test
    @DisplayName("Random word should always be from the given list")
    public void testChooseRandomWordFromList() {
        List<String> words = new ArrayList<>();
        words.add("water");
        words.add("otter");
        words.add("hound");

        String word = Wordle.chooseRandomWord(words);
        assertTrue(words.contains(word));
    }

    @Test
    @DisplayName("Words should load from resources correctly")
    public void testLoadWordsFromResources() {
        List<String> words = Wordle.loadWords();
        assertNotNull(words);
        assertFalse(words.isEmpty());

        for (String word : words) {
            assertEquals(5, word.length());
        }

        assertTrue(words.contains("water"));
        assertTrue(words.contains("otter"));
        assertTrue(words.contains("hound"));
        assertTrue(words.contains("pizza"));
        assertTrue(words.contains("eagle"));
        assertTrue(words.contains("fruit"));
        assertTrue(words.contains("paper"));
    }
}