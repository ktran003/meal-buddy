package persistence;

import model.FoodItem;
import model.GroceryList;
import model.GroceryTools;
import model.MonthlyBudget;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            GroceryTools gt = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyGroceryTools() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyApp.json");
        try {
            GroceryTools gt = reader.read();
            GroceryList gl = gt.getGroceryList();
            MonthlyBudget b = gt.getMonthlyBudget();

            assertEquals(0, gl.checkNumItems());
            assertEquals(0, gl.getFoodItems().size());

            assertEquals(0,b.getTargetMonthlyBudget());
            assertEquals(0, b.getCurrentSpending());
            assertEquals(0, b.getSpendingHistory().size());
            assertEquals(0, b.getPurchaseDates().size());
            assertTrue(b.getBudgetStatus());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


    @Test
    void testReaderGeneralGroceryTools() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralApp.json");
        try {
            GroceryTools gt = reader.read();
            GroceryList gl = gt.getGroceryList();
            MonthlyBudget b = gt.getMonthlyBudget();

            ArrayList<FoodItem> foodItemList = gl.getFoodItems();
            assertEquals(2, foodItemList.size());
            assertEquals(15, gl.checkNumItems());

            checkFoodItem("bread", "Bakery", foodItemList.get(0));
            checkFoodItem("chicken", "Meat & Fish", foodItemList.get(1));

            assertEquals(1000, b.getTargetMonthlyBudget());
            assertEquals(3, b.getPurchaseDates().size());
            assertEquals(1, b.getPurchaseDates().get(0));
            assertEquals(10, b.getPurchaseDates().get(1));
            assertEquals(21, b.getPurchaseDates().get(2));

            assertEquals(3, b.getSpendingHistory().size());
            assertEquals(100, b.getSpendingHistory().get(0));
            assertEquals(250, b.getSpendingHistory().get(1));
            assertEquals(80, b.getSpendingHistory().get(2));


        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
