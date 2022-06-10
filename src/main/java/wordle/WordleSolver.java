package wordle;

import java.util.*;

public class WordleSolver {
    public static void main(String[] args) {
        ArrayList<String> possibleWords = new ArrayList<>();
        CreateMasterList newGame = new CreateMasterList(possibleWords);
        Scanner input = new Scanner(System.in);

        // Read the file to create the master word list
        newGame.readFile();
        possibleWords = newGame.getMasterWordList();
        // Where the green letters appear
        HashMap<Integer, Character> greenLettersLocations = new HashMap<>();
        ArrayList<Character> yellowLetters = new ArrayList<>();
        ArrayList<Character> blackLetters = new ArrayList<>();

        greenLettersLocations.put(1, '?');
        greenLettersLocations.put(2, '?');
        greenLettersLocations.put(3, '?');
        greenLettersLocations.put(4, '?');
        greenLettersLocations.put(5, '?');

        String wordToGuess = generateRandomWord(possibleWords);
        System.out.println(wordToGuess);
        String currGuess = "";
        String guessColors;
        int guessCount = 0;

        while (!isGameOver(currGuess, wordToGuess, guessCount)) {
            // Will continue while the user has not guessed the word or ran out of guesses
            System.out.println("Enter your guess:");
            currGuess = input.nextLine();

            showColors(currGuess, wordToGuess);

            System.out.println("\nEnter the color codes (GGBBY):");
            guessColors = input.nextLine();

            // Update the letters and remove the words that are impossible
            updateLetters(guessColors, blackLetters, yellowLetters, greenLettersLocations, currGuess);


//
//            System.out.println("\nBlack Letters:");
//            for (Character letter : blackLetters) {
//                System.out.print(letter + " ");
//            }
//            System.out.println();
//            System.out.println("Yellow Letters:");
//            for (Character letter : yellowLetters) {
//                System.out.print(letter + " ");
//            }
//            System.out.println();
//            System.out.println("Green Letters:");
//            for (int i = 1; i < greenLettersLocations.size()+1; i++) {
//                System.out.print(greenLettersLocations.get(i));
//            }
//            System.out.println();

            possibleWords = updatePossibleWordsList(possibleWords, blackLetters);
            displayRemainingWords(possibleWords);

            displayLetterStatuses(blackLetters, yellowLetters, greenLettersLocations);
        }
    }

    public static void displayLetterStatuses(ArrayList<Character> blackLetters, ArrayList<Character> yellowLetters, HashMap<Integer, Character> greenLetters) {
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

        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_GREEN + "Previous Guess:" + ANSI_RESET);
        for (int i = 1; i < greenLetters.size()+1; i++) {
            System.out.print(ANSI_GREEN + greenLetters.get(i) + ANSI_RESET);
        }
        System.out.println();
    }

    // NOT WORKING PROPERLY FOR SOME REASON
    public static void showColors(String guess, String wordToGuess) {
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
        System.out.println(buildString);
    }

    public static String generateRandomWord(ArrayList<String> masterWordleList) {
        Random random = new Random();
        return masterWordleList.get(Integer.parseInt(String.valueOf(random.nextInt(masterWordleList.size()))));
    }

    public static boolean isGameOver(String currGuess, String wordToGuess, int guessCount) {
        // If the user ran out of attempts
        if (guessCount >= 6) {
            return true;
        }
        // If the user guesses the word or not
        return Objects.equals(currGuess, wordToGuess);
    }

    public static ArrayList<String> updatePossibleWordsList(ArrayList<String> possibleWords, ArrayList<Character> blackLetters) {
        ArrayList<String> usableWords = new ArrayList<>();
        // Remove the words that contain the black letters
        for (String word : possibleWords) {
            if (!containsLetter(word, blackLetters.get(0))) {
                //System.out.println("X " + word);
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
        for (int i = 0; i < colorCodes.length(); i++) {
            if (colorCodes.charAt(i) == 'G') {
                greenLetters.put(i+1, guess.charAt(i));
            } else if (colorCodes.charAt(i) == 'Y') {
                yellowLetters.add(guess.charAt(i));
            } else if (colorCodes.charAt(i) == 'B') {
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
