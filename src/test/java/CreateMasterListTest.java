import org.junit.jupiter.api.Test;
import wordle.CreateMasterList;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateMasterListTest {
    @Test
    public void successfullyLoadsFile() {
        ArrayList<String> masterWordleList = new ArrayList<>();
        CreateMasterList test = new CreateMasterList(masterWordleList);

        // Read the file to create the master word list
        test.readFile();
        masterWordleList = test.getMasterWordList();

        boolean greaterThanZero = masterWordleList.size() > 0;

        assertTrue(greaterThanZero);
    }
}
