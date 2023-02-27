package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents the two grocery tools that are involved in a functioning meal buddy app
public class GroceryTools implements Writable {
    private GroceryList groceryList;
    private MonthlyBudget monthlyBudget;

    // EFFECTS: creates a set of grocery tools with a new grocery list and new budget
    public GroceryTools() {
        groceryList = new GroceryList();
        monthlyBudget = new MonthlyBudget(0);
    }

    // EFFECTS: returns the grocery list of the meal buddy app
    public GroceryList getGroceryList() {
        return groceryList;
    }

    // EFFECTS: returns the budget of the meal buddy app
    public MonthlyBudget getMonthlyBudget() {
        return monthlyBudget;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("groceryManager", groceryList.toJson());
        json.put("budget", monthlyBudget.toJson());
        return json;
    }
}
