package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a food item that can be purchased at the grocery store which has a name, category, and purchased status.
public class FoodItem implements Writable {
    private String name;
    private boolean purchasedStatus;
    private String foodCategory;

    // REQUIRES: foodCategory must be one of: Bakery, Dairy, Frozen, Meat & Fish, Pantry, Produce, Snack, or Misc.
    // EFFECTS: creates a food item; sets the food category, sets name to itemName, and sets purchasedStatus to false
    //          which indicates the item is not purchased.
    public FoodItem(String foodCategory, String itemName) {
        name = itemName;
        this.foodCategory = foodCategory;
        purchasedStatus = false;
    }

    // MODIFIES: this
    // EFFECTS: changes the purchasedStatus of the food item to true.
    public void markAsPurchased() {
        purchasedStatus = true;
    }

    // EFFECTS: returns the purchasedStatus of the food item. True means the food item has been purchased. False means
    //          the food item has not been purchased.
    public boolean checkStatus() {
        return purchasedStatus;
    }

    // EFFECTS: returns the category the food item is in.
    public String getFoodCategory() {
        return foodCategory;
    }

    // EFFECTS: returns the name of the food item.
    public String getFoodName() {
        return name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("purchasedStatus", purchasedStatus);
        json.put("category", foodCategory);
        return json;
    }
}
