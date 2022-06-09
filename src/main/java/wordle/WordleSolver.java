package wordle;

import java.util.*;

public class WordleSolver {
    public static void main(String[] args) {
        ArrayList<String> masterWordleList = new ArrayList<>();
        CreateMasterList test = new CreateMasterList(masterWordleList);
        Scanner input = new Scanner(System.in);

        // Read the file to create the master word list
        test.readFile();
        masterWordleList = test.getMasterWordList();
        ArrayList<String> possibleWords = masterWordleList;
        // Where the green letters appear
        HashMap<Integer, Character> greenLettersLocations = new HashMap<>();
        ArrayList<Character> yellowLetters = new ArrayList<>();
        ArrayList<Character> blackLetters = new ArrayList<>();

        greenLettersLocations.put(1, '?');
        greenLettersLocations.put(2, '?');
        greenLettersLocations.put(3, '?');
        greenLettersLocations.put(4, '?');
        greenLettersLocations.put(5, '?');

        String wordToGuess = generateRandomWord(masterWordleList);
        System.out.println(wordToGuess);
        //String wordToGuess = "MONTH";
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
            updateLetters(guessColors, blackLetters, yellowLetters, greenLettersLocations, currGuess, wordToGuess);
            updatePossibleWordsList(possibleWords, currGuess, guessColors, greenLettersLocations, yellowLetters, blackLetters);
            displayRemainingWords(possibleWords);
        }
    }

    // NOT WORKING PROPERLY FOR SOME REASON
    public static void showColors(String guess, String wordToGuess) {
        String buildString = "";

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (containsLetter(wordToGuess, guess.charAt(i)) && wordToGuess.charAt(i) != guess.charAt(i)) {
                buildString += 'Y';
            } else if (!containsLetter(wordToGuess, guess.charAt(i))) {
                buildString += 'B';
            } else {
                buildString += 'G';
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

    public static void updatePossibleWordsList(ArrayList<String> possibleWords, String currGuess, String colorCodes, HashMap<Integer, Character> greenLetters, ArrayList<Character> yellowLetters, ArrayList<Character> blackLetters) {
        // Start by eliminating every word that contains letters that are blacked out
        for (String word : possibleWords) {
            // Iterating through each word in possibleWords
            for (Character letter : blackLetters) {
                // Iterating through each letter in blackLetters
                if (containsLetter(word, letter)) {
                    // Delete the word from possibleWords
                    possibleWords.remove(word);
                }
            }
            for (Character letter : yellowLetters) {
                // Iterating through each letter in yellow letters
                if (!containsLetter(word, letter)) {
                    // Delete the word from possibleWords
                    possibleWords.remove(word);
                }
            }
        }

    }

    public static boolean containsLetter(String word, char letter) {
        if (word.length() == 0) {
            return false;
        }
        return word.charAt(0) == letter || containsLetter(word.substring(1), letter);
    }

    public static void updateLetters(String colorCodes, ArrayList<Character> blackLetters, ArrayList<Character> yellowLetters, HashMap<Integer, Character> greenLetters, String guess, String wordToGuess) {
        for (int i = 0; i < colorCodes.length(); i++) {
            if (colorCodes.charAt(i) == 'G') {
                // WORK ON THIS
                greenLetters.put(i, guess.charAt(i));
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
