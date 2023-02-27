package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodItemTest {
    private FoodItem orange;
    private FoodItem celery;

    @BeforeEach
    public void setup() {
        orange = new FoodItem("fruit", "orange");
        celery = new FoodItem("vegetable", "celery");
    }

    @Test
    public void testConstructor() {
        assertEquals("fruit", orange.getFoodCategory());
        assertEquals("orange", orange.getFoodName());
        assertFalse(orange.checkStatus());

        assertEquals("vegetable", celery.getFoodCategory());
        assertEquals("celery", celery.getFoodName());
        assertFalse(orange.checkStatus());
    }

    @Test
    public void testMarkAsPurchased() {
        orange.markAsPurchased();
        assertTrue(orange.checkStatus());
        assertFalse(celery.checkStatus());

        celery.markAsPurchased();
        assertTrue(celery.checkStatus());

    }

}
