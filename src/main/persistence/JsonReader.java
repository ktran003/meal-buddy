package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.FoodItem;
import model.GroceryTools;
import org.json.*;

// based on sample workroom application; link below
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

// Represents a reader that reads grocery tools from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads grocery tool from file and returns it;
    // throws IOException if an error occurs reading data from file
    public GroceryTools read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGroceryTools(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses grocery tools from JSON object and returns it
    private GroceryTools parseGroceryTools(JSONObject jsonObject) {
        GroceryTools gt = new GroceryTools();
        JSONObject jsonGroceryList = jsonObject.getJSONObject("groceryManager");
        JSONObject jsonBudget = jsonObject.getJSONObject("budget");

        addGroceryList(gt, jsonGroceryList);
        addBudget(gt, jsonBudget);
        return gt;
    }

    // MODIFIES: gt
    // EFFECTS: parses the grocery list from JSON object and adds them to grocery tools
    private void addGroceryList(GroceryTools gt, JSONObject jsonObject) {
        JSONArray jsonItemQuantities = jsonObject.getJSONArray("itemQuantities");
        JSONArray jsonGroceryList = jsonObject.getJSONArray("groceryList");
        int pos = 0;

        for (Object jsonFoodItem : jsonGroceryList) {
            JSONObject nextFoodItem = (JSONObject) jsonFoodItem;
            JSONObject nextItemQuantity = (JSONObject) jsonItemQuantities.get(pos);
            addFoodItems(gt, nextFoodItem, nextItemQuantity);
            pos++;
        }
    }

    // MODIFIES: gt
    // EFFECTS: parses list of food items and their associated quantities from JSON objects and adds it to grocery tools
    private void addFoodItems(GroceryTools gt, JSONObject foodItem, JSONObject itemQuantity) {
        int quantity = itemQuantity.getInt("quantity");
        FoodItem food = addFoodItem(foodItem);
        gt.getGroceryList().addFoodItem(food, quantity);

    }

    // MODIFIES: gt
    // EFFECTS: parses a food item object from JSON object and adds it to grocery tools
    private FoodItem addFoodItem(JSONObject foodItem) {
        String name = foodItem.getString("name");
        Boolean purchasedStatus = foodItem.getBoolean("purchasedStatus");
        String category = foodItem.getString("category");
        FoodItem food = new FoodItem(category, name);

        return food;
    }

    // MODIFIES: gt
    // EFFECTS: parses the budget from JSON object and adds them to grocery tools
    private void addBudget(GroceryTools gt, JSONObject jsonObject) {
        double targetMonthlyBudget = jsonObject.getDouble("monthlyBudget");
        JSONArray jsonSpendingHistory = jsonObject.getJSONArray("spendingHistory");
        JSONArray jsonPurchaseDates = jsonObject.getJSONArray("purchaseDates");
        int pos = 0;

        gt.getMonthlyBudget().increaseBudget(targetMonthlyBudget);

        for (Object jsonAmountSpent : jsonSpendingHistory) {
            JSONObject nextAmountSpent = (JSONObject) jsonAmountSpent;
            JSONObject nextPurchaseDate = (JSONObject) jsonPurchaseDates.get(pos);
            logPurchase(gt, nextAmountSpent, nextPurchaseDate);
            pos++;
        }
    }

    // MODIFIES: gt
    // EFFECTS: parses the purchase log from two JSON objects and adds them to grocery tools
    private void logPurchase(GroceryTools gt, JSONObject amountSpent, JSONObject purchaseDate) {
        double amount = amountSpent.getDouble("spendingAmount");
        int date = purchaseDate.getInt("date");
        gt.getMonthlyBudget().logGroceryTrip(amount, date);

    }

}

