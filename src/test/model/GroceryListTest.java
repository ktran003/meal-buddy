package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GroceryListTest {
    private GroceryList groceryList1;
    private FoodItem milk;
    private FoodItem bread;
    private FoodItem blackBeans;
    private FoodItem butter;

    @BeforeEach
    public void setup() {
        groceryList1 = new GroceryList();
        milk = new FoodItem("dairy", "milk");
        bread = new FoodItem("bakery", "bread");
        blackBeans = new FoodItem("pantry", "black beans");
    }

    @Test
    public void testConstructor() {
        assertEquals(0, groceryList1.getFoodItems().size());
        assertFalse(groceryList1.getFoodItems().contains(milk));
        assertFalse(groceryList1.getFoodItems().contains(bread));
    }


    @Test
    public void testAddFoodItemNotInList() {
        groceryList1.addFoodItem(milk, 2);
        assertEquals(2, groceryList1.checkNumItems());
        groceryList1.addFoodItem(blackBeans, 1);
        assertEquals(3, groceryList1.checkNumItems());
        groceryList1.addFoodItem(bread, 3);
        assertEquals(6, groceryList1.checkNumItems());

        assertEquals(2, groceryList1.searchItem(milk));
        assertEquals(1, groceryList1.searchItem(blackBeans));
        assertEquals(3, groceryList1.searchItem(bread));

        assertEquals(milk, groceryList1.getFoodItems().get(0));
        assertEquals(blackBeans, groceryList1.getFoodItems().get(1));
        assertEquals(bread, groceryList1.getFoodItems().get(2));

        assertEquals(2, groceryList1.getQuantityItemList().get(0));
        assertEquals(1, groceryList1.getQuantityItemList().get(1));
        assertEquals(3, groceryList1.getQuantityItemList().get(2));
    }

    @Test
    public void testAddFoodItemInList() {
        groceryList1.addFoodItem(milk, 4);
        assertEquals(4, groceryList1.searchItem(milk));
        assertEquals(milk, groceryList1.getFoodItems().get(0));

        groceryList1.addFoodItem(blackBeans, 1);
        groceryList1.addFoodItem(milk, 6);
        assertEquals(10, groceryList1.searchItem(milk));
        assertEquals(milk, groceryList1.getFoodItems().get(0));
    }

    @Test
    public void testRemoveFoodItemInList() {
        groceryList1.addFoodItem(bread, 2);
        groceryList1.addFoodItem(milk, 1);
        groceryList1.addFoodItem(blackBeans, 5);
        assertEquals(8, groceryList1.checkNumItems());

        groceryList1.removeFoodItem(milk);
        assertEquals(7, groceryList1.checkNumItems());
        assertEquals(0, groceryList1.searchItem(milk));

        groceryList1.removeFoodItem(blackBeans);
        assertEquals(2, groceryList1.checkNumItems());
        assertEquals(0, groceryList1.searchItem(blackBeans));
    }

    @Test
    public void testPurchaseItems() {
        groceryList1.addFoodItem(bread, 2);
        groceryList1.addFoodItem(milk, 1);
        groceryList1.addFoodItem(blackBeans, 5);

        groceryList1.purchaseItem(milk);
        assertEquals(7, groceryList1.checkNumItems());
        assertEquals(0, groceryList1.searchItem(milk));

        groceryList1.purchaseItem(blackBeans);
        assertEquals(2, groceryList1.checkNumItems());
        assertEquals(0, groceryList1.searchItem(blackBeans));
    }

    @Test
    public void testFindInCategory() {
        butter = new FoodItem("dairy", "butter");
        groceryList1.addFoodItem(bread, 3);
        groceryList1.addFoodItem(milk, 4);
        groceryList1.addFoodItem(blackBeans, 2);
        groceryList1.addFoodItem(butter, 1);

        ArrayList<FoodItem> dairyGroceries = new ArrayList<>();
        dairyGroceries.add(milk);
        dairyGroceries.add(butter);

        ArrayList<FoodItem> pantryGroceries = new ArrayList<>();
        pantryGroceries.add(blackBeans);

        ArrayList<FoodItem> bakeryGroceries = new ArrayList<>();
        bakeryGroceries.add(bread);

        assertEquals(dairyGroceries, groceryList1.findItemsInCategory("dairy"));
        assertEquals(pantryGroceries, groceryList1.findItemsInCategory("pantry"));
        assertEquals(bakeryGroceries, groceryList1.findItemsInCategory("bakery"));
        assertEquals(4, groceryList1.getFoodItems().size());
    }
}
