package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a grocery budget with a target budget, current spending, status, spending history, and purchase dates.
public class MonthlyBudget implements Writable {
    private double targetMonthlyBudget;
    private double currentSpending;
    private boolean budgetStatus;
    private ArrayList<Double> spendingHistory;
    private ArrayList<Integer> purchaseDates;

    // REQUIRES: int >= 0
    // EFFECTS: target monthly budget is set to spending goal, budgetStatus is set to true which means that the target
    //          budget has not been passed; empty arraylists are created for spending history and purchase dates.
    public MonthlyBudget(int spendingGoal) {
        targetMonthlyBudget = spendingGoal;
        currentSpending = 0;
        budgetStatus = true;
        spendingHistory = new ArrayList<>();
        purchaseDates = new ArrayList<>();
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: increases the target monthly budget by the specified amount. Changes the budget status to true if
    //          increased above current spending. Returns the new target monthly budget.
    public double increaseBudget(double amount) {
        targetMonthlyBudget += amount;
        if (currentSpending <= targetMonthlyBudget) {
            budgetStatus = true;
        }
        return targetMonthlyBudget;
    }

    // REQUIRES: amount > 0; target monthly budget - amount > current spending.
    // MODIFIES: this
    // EFFECTS: decreases the target monthly budget by the specified amount. Returns the new target monthly budget.
    public double decreaseBudget(double amount) {
        targetMonthlyBudget -= amount;
        return targetMonthlyBudget;
    }

    // REQUIRES: amountSpent > 0 and date must be between 1 and 31
    // MODIFIES: this
    // EFFECTS: increases the currentSpending by amount spent, adds the amount spent to spending history, and date to
    //          purchase dates. Changes budget status to false if currentSpending > targetMonthlyBudget. Returns the
    //          amount of money left in the monthly budget.
    public double logGroceryTrip(double amountSpent, int date) {
        currentSpending += amountSpent;
        spendingHistory.add(amountSpent);
        purchaseDates.add(date);
        if (currentSpending > targetMonthlyBudget) {
            budgetStatus = false;
        }
        return targetMonthlyBudget - currentSpending;
    }

    // EFFECTS: returns targetMonthlyBudget - currentSpending.
    public double getAmountRemainingAmountInBudget() {
        return targetMonthlyBudget - currentSpending;
    }

    // EFFECTS: returns targetMonthlyBudget.
    public double getTargetMonthlyBudget() {
        return targetMonthlyBudget;
    }

    // EFFECTS: returns currentSpending.
    public double getCurrentSpending() {
        return currentSpending;
    }

    // EFFECTS: returns true if currentSpending <= targetMonthlyBudget. Returns false otherwise.
    public boolean getBudgetStatus() {
        return budgetStatus;
    }

    // EFFECTS: returns the users spending history.
    public ArrayList<Double> getSpendingHistory() {
        return spendingHistory;
    }

    // EFFECTS: returns the list of days the user made grocery purchases.
    public ArrayList<Integer> getPurchaseDates() {
        return purchaseDates;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("monthlyBudget", targetMonthlyBudget);
        json.put("spendingHistory", spendingHistoryToJson());
        json.put("purchaseDates", purchaseDatesToJson());
        return json;
    }

    // EFFECTS: returns the spending history of this budget as a JSON array
    private JSONArray spendingHistoryToJson() {
        JSONArray jsonArray = new JSONArray();
        for (double s : spendingHistory) {
            JSONObject json = new JSONObject();
            json.put("spendingAmount", s);
            jsonArray.put(json);
        }
        return jsonArray;
    }

    // EFFECTS: returns the purchase dates of this budget as a JSON array
    private JSONArray purchaseDatesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (int d : purchaseDates) {
            JSONObject json = new JSONObject();
            json.put("date", d);
            jsonArray.put(json);
        }
        return jsonArray;
    }

}
