package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MonthlyBudgetTest {
    private FoodItem milk;
    private FoodItem apples;
    private FoodItem crackers;
    private MonthlyBudget julyBudget;
    private GroceryList groceryList1;

    @BeforeEach
    public void setup() {
        milk = new FoodItem("dairy", "milk");
        apples = new FoodItem("fruit", "apples");
        crackers = new FoodItem("snack", "crackers");

        groceryList1 = new GroceryList();
        groceryList1.addFoodItem(milk, 3);
        groceryList1.addFoodItem(apples, 2);
        groceryList1.addFoodItem(crackers, 3);

        julyBudget = new MonthlyBudget(300);
    }

    @Test
    public void testConstructor() {
        assertEquals(300, julyBudget.getTargetMonthlyBudget());
        assertEquals(300, julyBudget.getAmountRemainingAmountInBudget());
        assertEquals(0, julyBudget.getCurrentSpending());
        assertTrue(julyBudget.getBudgetStatus());
        assertTrue(julyBudget.getSpendingHistory().isEmpty());
        assertTrue(julyBudget.getPurchaseDates().isEmpty());
    }

    @Test
    public void testIncreaseBudgetWithinCurrentBudget() {
        assertEquals(301, julyBudget.increaseBudget(1));
        assertEquals(301, julyBudget.getAmountRemainingAmountInBudget());
        assertTrue(julyBudget.getBudgetStatus());

        assertEquals(501, julyBudget.increaseBudget(200));
        assertEquals(501, julyBudget.getAmountRemainingAmountInBudget());
        assertTrue(julyBudget.getBudgetStatus());

    }

    @Test
    public void testIncreaseBudgetOverCurrentBudget() {
        julyBudget.logGroceryTrip(400, 25);
        assertEquals(-100, julyBudget.getAmountRemainingAmountInBudget());
        assertFalse(julyBudget.getBudgetStatus());

        assertEquals(400, julyBudget.increaseBudget(100));
        assertEquals(0, julyBudget.getAmountRemainingAmountInBudget());
        assertTrue(julyBudget.getBudgetStatus());

        julyBudget.logGroceryTrip(100, 26);
        assertEquals(-100, julyBudget.getAmountRemainingAmountInBudget());
        assertFalse(julyBudget.getBudgetStatus());

        assertEquals(401, julyBudget.increaseBudget(1));
        assertEquals(-99, julyBudget.getAmountRemainingAmountInBudget());
        assertFalse(julyBudget.getBudgetStatus());
    }

    @Test
    public void testDecreaseBudget() {
        assertEquals(299,julyBudget.decreaseBudget(1));
        assertEquals(299, julyBudget.getAmountRemainingAmountInBudget());
        assertTrue(julyBudget.getBudgetStatus());

        assertEquals(100, julyBudget.decreaseBudget(199));
        assertEquals(100, julyBudget.getAmountRemainingAmountInBudget());
        assertTrue(julyBudget.getBudgetStatus());
    }

    @Test
    public void testLogGroceryTrip() {
        assertEquals(299,         julyBudget.logGroceryTrip(1, 25));
        assertEquals(1, julyBudget.getCurrentSpending());
        assertTrue(julyBudget.getBudgetStatus());
        assertEquals(1, julyBudget.getSpendingHistory().get(0));
        assertEquals(25, julyBudget.getPurchaseDates().get(0));


        assertEquals(199, julyBudget.logGroceryTrip(100, 25));
        assertEquals(101, julyBudget.getCurrentSpending());
        assertTrue(julyBudget.getBudgetStatus());
        assertEquals(2, julyBudget.getSpendingHistory().size());
        assertEquals(1, julyBudget.getSpendingHistory().get(0));
        assertEquals(25, julyBudget.getPurchaseDates().get(0));
        assertEquals(100, julyBudget.getSpendingHistory().get(1));
        assertEquals(25, julyBudget.getPurchaseDates().get(1));

        assertEquals(-101, julyBudget.logGroceryTrip(300, 28));
        assertEquals(401, julyBudget.getCurrentSpending());
        assertFalse(julyBudget.getBudgetStatus());
        assertEquals(3, julyBudget.getSpendingHistory().size());
        assertEquals(1, julyBudget.getSpendingHistory().get(0));
        assertEquals(25, julyBudget.getPurchaseDates().get(0));
        assertEquals(100, julyBudget.getSpendingHistory().get(1));
        assertEquals(25, julyBudget.getPurchaseDates().get(1));
        assertEquals(300, julyBudget.getSpendingHistory().get(2));
        assertEquals(28, julyBudget.getPurchaseDates().get(2));
    }
}
