# Wordle CLI Game (Java)

🎉 A simple command-line version of the popular Wordle game, implemented in Java.

## Game Rules
- The player has **5 attempts** to guess a hidden 5-letter word.
- Correct letter **and** correct position → **GREEN**.
- Correct letter in the word but wrong position → **YELLOW**.
- Letters not in the word → displayed normally (gray).
- Duplicate letters are handled correctly according to Wordle rules.

## Features
- Command-line interface (CLI), no network required.
- Random word selection from a local word list (`words.txt`).
- ANSI colors for feedback in the terminal.
- Unit tests for core functionality using JUnit 5.

## Setup

### Requirements
- Java 21+
- Maven (for running tests)

### Project Structure