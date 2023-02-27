package persistence;

import model.FoodItem;
import model.GroceryList;
import model.GroceryTools;
import model.MonthlyBudget;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{

    @Test
    void testWriterInvalidFile() {
        try {
            GroceryTools gt = new GroceryTools();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }


    @Test
    void testWriterEmptyGroceryTools() {
        try {
            GroceryTools gt = new GroceryTools();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyApp.json");
            writer.open();
            writer.write(gt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyApp.json");
            gt = reader.read();

            GroceryList gl = gt.getGroceryList();
            MonthlyBudget b = gt.getMonthlyBudget();

            assertEquals(0, gl.checkNumItems());
            assertEquals(0, gl.getFoodItems().size());

            assertEquals(0,b.getTargetMonthlyBudget());
            assertEquals(0, b.getCurrentSpending());
            assertEquals(0, b.getSpendingHistory().size());
            assertEquals(0, b.getPurchaseDates().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralGroceryTools() {
        try {
            GroceryTools gt = new GroceryTools();
            GroceryList gl = gt.getGroceryList();
            MonthlyBudget b = gt.getMonthlyBudget();

            gl.addFoodItem(new FoodItem("Dairy", "milk"), 5);
            gl.addFoodItem(new FoodItem("Meat & Fish", "salmon"), 3);
            b.increaseBudget(500);
            b.logGroceryTrip(100,10);
            b.logGroceryTrip(110, 12);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralApp.json");
            writer.open();
            writer.write(gt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralApp.json");
            gt = reader.read();

            ArrayList<FoodItem> foodItemList = gl.getFoodItems();
            assertEquals(2, foodItemList.size());
            assertEquals(8, gl.checkNumItems());

            checkFoodItem("milk", "Dairy", foodItemList.get(0));
            checkFoodItem("salmon", "Meat & Fish", foodItemList.get(1));

            assertEquals(500, b.getTargetMonthlyBudget());
            assertEquals(2, b.getPurchaseDates().size());
            assertEquals(10, b.getPurchaseDates().get(0));
            assertEquals(12, b.getPurchaseDates().get(1));

            assertEquals(2, b.getSpendingHistory().size());
            assertEquals(100, b.getSpendingHistory().get(0));
            assertEquals(110, b.getSpendingHistory().get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
