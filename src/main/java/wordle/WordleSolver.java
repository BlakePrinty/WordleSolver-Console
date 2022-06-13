package wordle;

import java.util.*;

public class WordleSolver {
    public static void main(String[] args) {
        ArrayList<String> possibleWords = new ArrayList<>();
        ArrayList<Character> yellowLetters = new ArrayList<>();
        ArrayList<Character> blackLetters = new ArrayList<>();
        HashMap<Integer, Character> greenLettersLocations = new HashMap<>();
        ArrayList<String> guesses = new ArrayList<>();

        CreateMasterList newGame = new CreateMasterList(possibleWords);
        Scanner input = new Scanner(System.in);

        newGame.readFile();
        possibleWords = newGame.getMasterWordList();

        for (int i = 1; i < 6; i++) {
            greenLettersLocations.put(i, '?');
        }

        String wordToGuess = generateRandomWord(possibleWords);
        String currGuess = "";
        String guessColors;
        int guessCount = 0;

        while (!isGameOver(currGuess, wordToGuess, guessCount)) {
            System.out.println("Enter your guess:");
            currGuess = input.nextLine();

            guesses.add(currGuess);

            guessColors = String.valueOf(showColors(currGuess, wordToGuess));

            updateLetters(guessColors, blackLetters, yellowLetters, greenLettersLocations, currGuess);

            for (int i = 0; i < blackLetters.size(); i++) {
                possibleWords = updatePossibleWordsListBlack(i, possibleWords, blackLetters);
            }
            for (int i = 0; i < yellowLetters.size(); i++) {
                possibleWords = updatePossibleWordsListYellow(i, possibleWords, yellowLetters);
            }

            System.out.print(possibleWords.size() + " choice");
            if (possibleWords.size() > 1) {
                System.out.print("s");
            }
            System.out.println("...");

            //System.out.println(possibleWords.size() + " choices...");
            displayRemainingWords(possibleWords);
            displayLetterStatuses(guesses, blackLetters, yellowLetters, greenLettersLocations);
        }

        System.out.println();
        if (Objects.equals(currGuess, wordToGuess)) {
            System.out.println(ANSI_WHITE_BACKGROUND + ANSI_GREEN + "SOLVED!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_WHITE_BACKGROUND + ANSI_RED + "WRONG!" + ANSI_RESET);
        }
    }

    public static void displayLetterStatuses(ArrayList<String> guesses, ArrayList<Character> blackLetters, ArrayList<Character> yellowLetters, HashMap<Integer, Character> greenLetters) {
        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_YELLOW + "Yellow Letters:" + ANSI_RESET);
        for (Character letter : yellowLetters) {
            System.out.print(ANSI_YELLOW + letter + " " + ANSI_RESET);
        }
        System.out.println();

        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_RED + "Black Letters:" + ANSI_RESET);
        for (Character letter : blackLetters) {
            System.out.print(ANSI_RED + letter + " " + ANSI_RESET);
        }
        System.out.println();

        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_GREEN + "Green Letters:" + ANSI_RESET);
        for (int i = 1; i < greenLetters.size()+1; i++) {
            System.out.print(ANSI_GREEN + greenLetters.get(i) + ANSI_RESET);
        }
        System.out.println();

        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_PURPLE + "Previous guesses:" + ANSI_RESET);
        for (String guess : guesses) {
            for (int i = 0; i < guess.length(); i++) {
                if (greenLetters.get(i+1) == guess.charAt(i)) {
                    System.out.print(ANSI_GREEN + guess.charAt(i) + ANSI_RESET);
                } else if (blackLetters.contains(guess.charAt(i))) {
                    System.out.print(ANSI_BLACK + guess.charAt(i) + ANSI_RESET);
                } else if (yellowLetters.contains(guess.charAt(i))) {
                    System.out.print(ANSI_YELLOW + guess.charAt(i) + ANSI_RESET);
                } else {
                    System.out.print(ANSI_GREEN + guess.charAt(i) + ANSI_RESET);
                }
            }
            System.out.println();
        }

    }

    public static StringBuilder showColors(String guess, String wordToGuess) {
        StringBuilder buildString = new StringBuilder();

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (containsLetter(wordToGuess, guess.charAt(i)) && wordToGuess.charAt(i) != guess.charAt(i)) {
                buildString.append('Y');
            } else if (!containsLetter(wordToGuess, guess.charAt(i))) {
                buildString.append('B');
            } else {
                buildString.append('G');
            }
        }
        return buildString;
    }

    public static String generateRandomWord(ArrayList<String> masterWordleList) {
        Random random = new Random();
        return masterWordleList.get(Integer.parseInt(String.valueOf(random.nextInt(masterWordleList.size()))));
    }

    public static boolean isGameOver(String currGuess, String wordToGuess, int guessCount) {
        if (guessCount >= 6) {
            return true;
        }
        return Objects.equals(currGuess, wordToGuess);
    }

    public static ArrayList<String> updatePossibleWordsListBlack(int curPos, ArrayList<String> possibleWords, ArrayList<Character> blackLetters) {
        ArrayList<String> usableWords = new ArrayList<>();

        for (String word : possibleWords) {
            if (!containsLetter(word, blackLetters.get(curPos))) {
                usableWords.add(word);
            }
        }
        return usableWords;
    }

    public static ArrayList<String> updatePossibleWordsListYellow(int curPos, ArrayList<String> possibleWords, ArrayList<Character> yellowLetters) {
        ArrayList<String> usableWords = new ArrayList<>();

        for (String word : possibleWords) {
            if (containsLetter(word, yellowLetters.get(curPos))) {
                usableWords.add(word);
            }
        }
        return usableWords;
    }

    public static boolean containsLetter(String word, char letter) {
        if (word.length() == 0) {
            return false;
        }
        return word.charAt(0) == letter || containsLetter(word.substring(1), letter);
    }

    public static void updateLetters(String colorCodes, ArrayList<Character> blackLetters, ArrayList<Character> yellowLetters, HashMap<Integer, Character> greenLetters, String guess) {
        // Need to add a way for words with multiple occurrences of the same letter
        for (int i = 0; i < colorCodes.length(); i++) {
            if (colorCodes.charAt(i) == 'G') {
                greenLetters.put(i+1, guess.charAt(i));
            } else if (colorCodes.charAt(i) == 'Y' && !yellowLetters.contains(guess.charAt(i))) {
                yellowLetters.add(guess.charAt(i));
            } else if (colorCodes.charAt(i) == 'B' && !blackLetters.contains(guess.charAt(i))) {
                blackLetters.add(guess.charAt(i));
            }
        }
    }

    public static void displayRemainingWords(ArrayList<String> possibleWords) {
        System.out.println("\nList of possible words:");
        for (String word : possibleWords) {
            System.out.println(word);
        }
        System.out.println();
    }

    // Text Colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Backgrounds
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
}
