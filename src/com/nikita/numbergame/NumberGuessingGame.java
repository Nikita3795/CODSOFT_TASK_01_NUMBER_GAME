package com.nikita.numbergame;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    private static final String LEADERBOARD_FILE = "leaderboard.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        boolean playAgain = true;
        int totalScore = 0;
        int bestScore = loadBestScore();

        System.out.println("🎮 Welcome to the Number Guessing Game! 🎮");
        System.out.println("🏆 Current Best Score: " + bestScore + " points!");

        while (playAgain) {
            int difficulty = chooseDifficulty(scanner);
            int maxAttempts = getMaxAttempts(difficulty);
            int numberToGuess = random.nextInt(100) + 1;
            int attempts = 0;
            boolean guessedCorrectly = false;

            System.out.println("\n🔢 I have picked a number between 1 and 100. Can you guess it?");
            System.out.println("💡 Hint: " + getHint(numberToGuess));

            while (attempts < maxAttempts) {
                System.out.print("\n🎯 Enter your guess: ");
                int userGuess = scanner.nextInt();
                attempts++;

                if (userGuess == numberToGuess) {
                    System.out.println("🎉 Congratulations! You guessed the number in " + attempts + " attempts.");
                    guessedCorrectly = true;
                    int roundScore = (maxAttempts - attempts + 1) * 10; // Higher score for fewer attempts
                    totalScore += roundScore;
                    System.out.println("🏆 Round Score: " + roundScore + " | Total Score: " + totalScore);
                    break;
                } else if (userGuess > numberToGuess) {
                    System.out.println("📈 Too high! Try again.");
                } else {
                    System.out.println("📉 Too low! Try again.");
                }

                System.out.println("💡 Hint: " + getHint(numberToGuess));
                System.out.println("🔁 Attempts left: " + (maxAttempts - attempts));
            }

            if (!guessedCorrectly) {
                System.out.println("❌ You ran out of attempts! The number was: " + numberToGuess);
            }

            System.out.print("\n🔄 Do you want to play again? (yes/no): ");
            playAgain = scanner.next().equalsIgnoreCase("yes");
        }

        System.out.println("\n🎮 Game Over! Your Final Score: " + totalScore);
        if (totalScore > bestScore) {
            System.out.println("🏅 New High Score! Saving to Leaderboard...");
            saveBestScore(totalScore);
        }

        scanner.close();
    }

    private static int chooseDifficulty(Scanner scanner) {
        System.out.println("\n🎚️ Choose Difficulty Level:");
        System.out.println("1️⃣ Easy (15 attempts)");
        System.out.println("2️⃣ Medium (10 attempts)");
        System.out.println("3️⃣ Hard (5 attempts)");
        System.out.print("🕹️ Enter your choice (1-3): ");

        int choice = scanner.nextInt();
        return (choice >= 1 && choice <= 3) ? choice : 2; // Default to Medium
    }

    private static int getMaxAttempts(int difficulty) {
        return switch (difficulty) {
            case 1 -> 15; // Easy
            case 3 -> 5;  // Hard
            default -> 10; // Medium
        };
    }

    private static String getHint(int number) {
        if (number % 2 == 0) {
            return "The number is EVEN.";
        } else {
            return "The number is ODD.";
        }
    }

    private static int loadBestScore() {
        try (BufferedReader br = new BufferedReader(new FileReader(LEADERBOARD_FILE))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0; // Default best score if file not found
        }
    }

    private static void saveBestScore(int score) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LEADERBOARD_FILE))) {
            bw.write(String.valueOf(score));
        } catch (IOException e) {
            System.out.println("⚠️ Error saving the leaderboard.");
        }
    }
}
