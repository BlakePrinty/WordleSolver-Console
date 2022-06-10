package wordle;

import java.util.*;

public class WordleSolver {
    public static void main(String[] args) {
        ArrayList<String> possibleWords = new ArrayList<>();
        CreateMasterList test = new CreateMasterList(possibleWords);
        Scanner input = new Scanner(System.in);

        // Read the file to create the master word list
        test.readFile();
        possibleWords = test.getMasterWordList();
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

            System.out.println("\nBlack Letters:");
            for (Character letter : blackLetters) {
                System.out.print(letter + " ");
            }
            System.out.println();
            System.out.println("Yellow Letters:");
            for (Character letter : yellowLetters) {
                System.out.print(letter + " ");
            }
            System.out.println();
            System.out.println("Green Letters:");
            for (int i = 1; i < greenLettersLocations.size()+1; i++) {
                System.out.print(greenLettersLocations.get(i));
            }
            System.out.println();

            possibleWords = updatePossibleWordsList(possibleWords, blackLetters);
            displayRemainingWords(possibleWords);
        }
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

}
