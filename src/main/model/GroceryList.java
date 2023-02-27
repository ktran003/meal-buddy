package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents the food items that have been added to the grocery list.
public class GroceryList implements Writable {
    private ArrayList<FoodItem> foodItems;
    private ArrayList<Integer> quantityItemList;

    //EFFECTS: creates an empty groceryList and empty quantityItemList.
    public GroceryList() {
        foodItems = new ArrayList<>();
        quantityItemList = new ArrayList<>();
    }

    // REQUIRES: num > 0
    // MODIFIES: this
    // EFFECTS: if food item is not in grocery list, then add food item to the grocery list and the desired
    //          amount to the quantity item list. If food item is already in grocery list, increase the desired number
    //          in the quantity item list by the specified amount.
    public void addFoodItem(FoodItem food, int num) {
        String name = food.getFoodName();
        boolean isAlreadyInList = false;

        for (FoodItem f : foodItems) {
            if (f.getFoodName().equals(name)) {
                int position = foodItems.indexOf(f);
                int amount = quantityItemList.get(position);
                quantityItemList.set(position, amount + num);
                EventLog.getInstance().logEvent(new Event("Added food item to grocery list: " + food.getFoodName()
                        + ". Total quantity on list: " + quantityItemList.get(position)));
                isAlreadyInList = true;
            }
        }

        if (!isAlreadyInList) {
            foodItems.add(food);
            quantityItemList.add(num);
            EventLog.getInstance().logEvent(new Event("Added food item to grocery list: " + food.getFoodName()
                    + ". Quantity on list: " +  num));
        }
    }

    // REQUIRES: groceryList must contain the food item
    // EFFECTS: removes unwanted food items and their quantities from the grocery list. *Does not change the status of
    //          the food item.
    public void removeFoodItem(FoodItem food) {
        int position = foodItems.indexOf(food);
        quantityItemList.remove(position);
        foodItems.remove(food);
        EventLog.getInstance().logEvent(new Event("Removed food item from grocery list: " + food.getFoodName()));
    }

    // REQUIRES: groceryList must contain the food item
    // MODIFIES: this and food
    // EFFECTS: removes the food item and quantity from the grocery list and the quantity item list. Changes the status
    //          of the food item to 'purchased'.
    public void purchaseItem(FoodItem food) {
        int position = foodItems.indexOf(food);
        food.markAsPurchased();
        quantityItemList.remove(position);
        foodItems.remove(food);
        EventLog.getInstance().logEvent(new Event("Marked food item from grocery list as purchased: "
                + food.getFoodName()));
    }

    // EFFECTS: returns the number of items on the grocery list.
    public int checkNumItems() {
        int sum = 0;
        for (int i : quantityItemList) {
            sum += i;
        }
        return sum;
    }

    // MODIFIES: this
    // EFFECTS: returns the food items in the  grocery list in the given category.
    public ArrayList<FoodItem> findItemsInCategory(String category) {
        ArrayList<FoodItem> categorizedGroceryList = new ArrayList<>();
        for (FoodItem food : foodItems) {
            if (food.getFoodCategory().equals(category)) {
                categorizedGroceryList.add(food);
            }
        }
        return categorizedGroceryList;
    }

    // EFFECTS: if the food item is in the grocery list return its accompanying quantity, if the food item is not on the
    //          grocery list return 0.
    public int searchItem(FoodItem food) {
        if (foodItems.contains(food)) {
            int position = foodItems.indexOf(food);
            return quantityItemList.get(position);
        }
        return 0;
    }

    // EFFECTS: returns the current list of food items on the grocery list.
    public ArrayList<FoodItem> getFoodItems() {
        return foodItems;
    }

    // EFFECTS: returns the current quantity list of food items on the grocery list.
    public ArrayList<Integer> getQuantityItemList() {
        return quantityItemList;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("groceryList", groceryListToJson());
        json.put("itemQuantities", itemQuantitiesToJson());
        return json;
    }

    // EFFECTS: returns foodItems in this grocery list as a JSON array
    private JSONArray groceryListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (FoodItem f : foodItems) {
            jsonArray.put(f.toJson());
        }
        
        return jsonArray;
    }

    // EFFECTS: returns item quantities in this grocery list as a JSON array
    private JSONArray itemQuantitiesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (int q : quantityItemList) {
            JSONObject json = new JSONObject();
            json.put("quantity", q);
            jsonArray.put(json);
        }
        return jsonArray;
    }

}
