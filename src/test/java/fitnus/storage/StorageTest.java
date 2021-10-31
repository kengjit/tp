package fitnus.storage;

import fitnus.database.EntryDatabase;
import fitnus.database.FoodDatabase;
import fitnus.exception.FitNusException;
import fitnus.tracker.Food;
import fitnus.tracker.Gender;
import fitnus.tracker.MealType;
import fitnus.utility.User;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageTest {
    private static final String ROOT = System.getProperty("user.dir");
    private static final Path FILE_PATH_FOOD_DATA_VALID = Paths.get(ROOT, "data/test",
            "FoodDatabaseValid.txt");
    private static final Path FILE_PATH_FOOD_DATA_INVALID = Paths.get(ROOT, "data/test",
            "FoodDatabaseInvalid.txt");
    private static final Path FILE_PATH_FOOD_DATA_SAVE = Paths.get(ROOT, "data/test",
            "FoodDatabaseSave.txt");
    private static final Path FILE_PATH_ENTRY_DATA_VALID = Paths.get(ROOT, "data/test",
            "EntryDatabaseValid.txt");
    private static final Path FILE_PATH_ENTRY_DATA_INVALID = Paths.get(ROOT, "data/test",
            "EntryDatabaseInvalid.txt");
    private static final Path FILE_PATH_ENTRY_DATA_SAVE = Paths.get(ROOT, "data/test",
            "EntryDatabaseSave.txt");

    private static final Path FILE_PATH_USER_DATA = Paths.get(ROOT, "data/test", "user.txt");
    private static final Path FILE_PATH_WEIGHT_DATA = Paths.get(ROOT, "data/test", "weight.txt");

    //Utility method
    private static void saveData(String filePath, String content) throws IOException {
        File file = new File(filePath);
        FileWriter fw;
        fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }

    @Test
    void initialiseFoodDatabase_validStorageFile_preloadSuccess()
            throws IOException, FitNusException {
        FoodDatabase database = new FoodDatabase();
        FileInputStream stream;
        stream = new FileInputStream(FILE_PATH_FOOD_DATA_VALID.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        database.preloadDatabase(reader);
        reader.close();
        assertEquals(" 1.ramen (600 Kcal) Type: MEAL" + System.lineSeparator()
                + " 2.rice (800 Kcal) Type: MEAL" + System.lineSeparator(), database.listFoods());
    }

    @Test
    void initialiseFoodDatabase_invalidStorageFile_throwsFitNusException()
            throws IOException, FitNusException {
        FoodDatabase database = new FoodDatabase();
        FileInputStream stream;
        stream = new FileInputStream(FILE_PATH_FOOD_DATA_INVALID.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        assertThrows(FitNusException.class, () -> database.preloadDatabase(reader));
        database.preloadDatabase(reader);
        reader.close();
    }

    @Test
    void initialiseFoodDatabase_invalidFilePath_throwsFileNotFoundException() {
        assertThrows(FileNotFoundException.class, () ->
                new FileInputStream(""));
    }

    @Test
    void initialiseEntryDatabase_validStorageFile_preloadSuccess()
            throws IOException {
        EntryDatabase database = new EntryDatabase();
        FileInputStream stream;
        stream = new FileInputStream(FILE_PATH_ENTRY_DATA_VALID.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        database.preloadDatabase(reader);
        reader.close();
        assertEquals(" 1.[2021-10-25] Lunch: ramen (600 Kcal) Type: MEAL"
                + System.lineSeparator() + " 2.[2021-10-25] Lunch: rice (800 Kcal) Type: MEAL"
                + System.lineSeparator(), database.listEntries());
    }

    @Test
    void initialiseEntryDatabase_invalidStorageFile_nothingPreloaded()
            throws IOException {
        EntryDatabase database = new EntryDatabase();
        FileInputStream stream;
        stream = new FileInputStream(FILE_PATH_ENTRY_DATA_INVALID.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        database.preloadDatabase(reader);
        reader.close();
        assertEquals("Sorry, there is not any record stored", database.listEntries());
    }

    @Test
    void initialiseEntryDatabase_invalidFilePath_throwsFileNotFoundException() {
        assertThrows(FileNotFoundException.class, () ->
                new FileInputStream(""));
    }

    @Test
    void initialiseUser() {
        // Adeline can do this?
    }

    @Test
    void initialiseWeightProgress() {
        // Adeline can do this?
    }

    @Test
    void saveFoodDatabase_validFilePath_saveSuccessfully() throws FitNusException, IOException {
        FoodDatabase database = new FoodDatabase();
        database.addFood("ramen", 400, Food.FoodType.MEAL);
        database.addFood("rice", 900, Food.FoodType.SNACK);

        String data = database.convertDatabaseToString();
        saveData(FILE_PATH_FOOD_DATA_SAVE.toString(), data);
        BufferedReader reader1 = new BufferedReader(new FileReader(FILE_PATH_FOOD_DATA_SAVE.toString()));

        assertEquals("ramen | 400 | MEALrice | 900 | SNACK",
                reader1.lines().collect(Collectors.joining()));
    }

    @Test
    void saveFoodDatabase_invalidFilePath_throwsFileNotFoundException() throws FitNusException {
        FoodDatabase database = new FoodDatabase();
        database.addFood("ramen", 400, Food.FoodType.MEAL);
        database.addFood("rice", 900, Food.FoodType.SNACK);

        String data = database.convertDatabaseToString();
        assertThrows(FileNotFoundException.class, () -> saveData("", data));
    }

    @Test
    void saveEntryDatabase_validFilePath_saveSuccessfully() throws IOException {
        EntryDatabase database = new EntryDatabase();
        Food prata = new Food("Prata", 100, Food.FoodType.MEAL);
        Food chickenRice = new Food("Chicken Rice", 325, Food.FoodType.SNACK);
        database.addEntry(MealType.DINNER, prata);
        database.addEntry(MealType.DINNER, chickenRice);

        String data = database.convertDatabaseToString();
        saveData(FILE_PATH_ENTRY_DATA_SAVE.toString(), data);
        BufferedReader reader1 = new BufferedReader(new FileReader(FILE_PATH_ENTRY_DATA_SAVE.toString()));

        assertEquals("Dinner | Prata | 100 | " + LocalDate.now() + " | MEALDinner "
                        + "| Chicken Rice | 325 | " + LocalDate.now() + " | SNACK",
                reader1.lines().collect(Collectors.joining()));
    }

    @Test
    void saveEntryDatabase_invalidFilePath_throwsFileNotFoundException() {
        EntryDatabase database = new EntryDatabase();
        Food prata = new Food("Prata", 100, Food.FoodType.MEAL);
        Food chickenRice = new Food("Chicken Rice", 325, Food.FoodType.SNACK);
        database.addEntry(MealType.DINNER, prata);
        database.addEntry(MealType.DINNER, chickenRice);

        String data = database.convertDatabaseToString();
        assertThrows(FileNotFoundException.class, () -> saveData("", data));
    }

    @Test
    void saveUserData() {
        //Adeline
    }

    @Test
    void saveWeightData() {
        //Adeline
    }
}