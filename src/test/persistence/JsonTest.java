package persistence;

import model.FoodItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JsonTest {
    protected void checkFoodItem(String name, String category, FoodItem food) {
        assertEquals(name, food.getFoodName());
        assertFalse(food.checkStatus());
        assertEquals(category, food.getFoodCategory());
    }

}
