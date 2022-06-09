package wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CreateMasterList {
    private final ArrayList<String> masterWordList;

    public CreateMasterList(ArrayList<String> masterWordList) {
        this.masterWordList = masterWordList;
    }

    public void readFile() {
        try {
            File wordleFile = new File("wordleMasterList.txt");
            Scanner reader = new Scanner(wordleFile);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                // Add the word from the txt file to the ArrayList
                masterWordList.add(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getMasterWordList() {
        return masterWordList;
    }

}
