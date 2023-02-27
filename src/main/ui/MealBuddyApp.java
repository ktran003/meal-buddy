package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// based on tellerApp, alarmSystem, and space invaders; link below
// https://github.students.cs.ubc.ca/CPSC210/TellerApp.git
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
// https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase.git

// Meal buddy application which takes input from the user to manage a grocery list and monthly budget.
public class MealBuddyApp {
    private static final String JSON_STORE = "./data/mealBuddy.json";
    private GroceryTools groceryTools;
    private GroceryList groceryList;
    private MonthlyBudget budget;
    private JsonWriter writer;
    private JsonReader reader;
    private String month;
    private MenuUI mainMenu;

    // EFFECTS: runs the meal buddy application
    public MealBuddyApp() throws FileNotFoundException {
        initializeApp();
    }

    // MODIFIES: this
    // EFFECTS: initializes the grocery list, budget, user input, json reader, and json writer of the meal buddy app
    private void initializeApp() {
        groceryTools = new GroceryTools();
        groceryList = groceryTools.getGroceryList();
        budget = groceryTools.getMonthlyBudget();

        writer = new JsonWriter(JSON_STORE);
        reader = new JsonReader(JSON_STORE);
        month = new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime());
        mainMenu = new MenuUI();

    }

    public class MenuUI extends JFrame {
        private JDesktopPane desktop;
        private JPanel contentPane;
        private static final int WIDTH = 800;
        private static final int HEIGHT = 600;

        // EFFECTS: creates the main desktop pane for the ui of the meal buddy app
        public MenuUI() {
            desktop = new JDesktopPane();
            desktop.addMouseListener(new DesktopFocusAction());

            // Create a content panel
            contentPane = new JPanel(new BorderLayout());
            addWelcomeMessage();
            addButtonPanel();
            addMenuBar();

            setContentPane(contentPane);
            setTitle("Meal Buddy Application");
            setSize(WIDTH, HEIGHT);

            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            centreOnScreen();
            setVisible(true);

        }

        // MODIFIES: this
        // EFFECTS: adds a welcome message and instructions to the main menu screen
        private void addWelcomeMessage() {
            JPanel welcomePanel = new JPanel();
            JLabel welcomeMessage = new JLabel("  Welcome to Meal Buddy!");
            JLabel menuInstructions = new JLabel("<html> To begin managing your grocery and budget needs, select one "
                    + "<br>of the buttons to the right:<html>");
            welcomeMessage.setFont(new java.awt.Font("Arial", Font.BOLD, 50));
            menuInstructions.setFont(new java.awt.Font("Arial", Font.PLAIN, 20));

            welcomePanel.add(welcomeMessage);
            welcomePanel.add(menuInstructions);
            contentPane.add(welcomePanel);
        }

        // MODIFIES: this
        // EFFECTS: adds the buttons on the main menu for the various meal buddy functions
        private void addButtonPanel() {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(4, 1));
            buttonPanel.add(new JButton(new BudgetManagerAction()));
            buttonPanel.add(new JButton(new GroceryManagerAction()));
            buttonPanel.add(new JButton(new SaveMealBuddyAction()));
            buttonPanel.add(new JButton(new LoadMealBuddyAction()));

            contentPane.add(buttonPanel, BorderLayout.EAST);
        }

        // MODIFIES: this
        // EFFECTS: adds a menu bar to the top of the desktop panel so users can navigate between the home screen,
        //          budget manager, and grocery manager
        private void addMenuBar() {
            JMenuBar menuBar = new JMenuBar();

            JMenu homeMenu = new JMenu("Home");
            addMenuItem(homeMenu, new HomeAction());
            menuBar.add(homeMenu);

            JMenu budgetManagerMenu = new JMenu("Budget");
            addMenuItem(budgetManagerMenu, new BudgetManagerAction());
            addMenuItem(budgetManagerMenu, new IncreaseBudgetAction());
            addMenuItem(budgetManagerMenu, new DecreaseBudgetAction());
            addMenuItem(budgetManagerMenu, new LogGroceryPurchaseAction());
            addMenuItem(budgetManagerMenu, new ViewBudgetBreakdownAction());
            menuBar.add(budgetManagerMenu);

            JMenu groceryManagerMenu = new JMenu("Grocery");
            addMenuItem(groceryManagerMenu, new GroceryManagerAction());
            addMenuItem(groceryManagerMenu, new AddItemAction());
            addMenuItem(groceryManagerMenu, new RemoveItemAction());
            addMenuItem(groceryManagerMenu, new MarkAsPurchasedAction());
            addMenuItem(groceryManagerMenu, new SearchAction());
            addMenuItem(groceryManagerMenu, new GroceryListBreakdownAction());
            menuBar.add(groceryManagerMenu);

            JMenu quitMenu = new JMenu("Quit");
            addMenuItem(quitMenu, new QuitAction());
            menuBar.add(quitMenu);

            setJMenuBar(menuBar);

        }

        // MODIFIES: this
        // EFFECTS: centres the main application window on desktop
        private void centreOnScreen() {
            int width = Toolkit.getDefaultToolkit().getScreenSize().width;
            int height = Toolkit.getDefaultToolkit().getScreenSize().height;
            setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
        }

        private class HomeAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Go to main menu"
            HomeAction() {
                super("Go to main menu");
            }

            // MODIFIES: MenuUI
            // EFFECTS: takes the user back to the home screen
            @Override
            public void actionPerformed(ActionEvent evt) {
                contentPane.removeAll();
                addWelcomeMessage();
                addButtonPanel();
                contentPane.validate();
                contentPane.repaint();
            }

        }



        // BUDGET MANAGER ACTIONS:
        private class BudgetManagerAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Budget Manager"
            BudgetManagerAction() {
                super("Budget Manager");
            }

            // MODIFIES: MenuUI
            // EFFECTS: takes the user to the budget manager screen
            @Override
            public void actionPerformed(ActionEvent evt) {
                contentPane.removeAll();
                contentPane.add(createBudgetManagerButtons(), BorderLayout.CENTER);
                contentPane.validate();
                contentPane.repaint();
            }
        }

        private class IncreaseBudgetAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Increase Budget"
            IncreaseBudgetAction() {
                super("Increase Budget");
            }

            // MODIFIES: MealBuddyApp
            // EFFECTS: takes user input to increase the current monthly budget
            @Override
            public void actionPerformed(ActionEvent evt) {
                String increaseBudgetBy = JOptionPane.showInputDialog(null,
                        "Please enter the amount to increase the budget by: $",
                        ("Increase Budget Request | Current Budget:" + budget.getTargetMonthlyBudget()),
                        JOptionPane.QUESTION_MESSAGE);

                double amount = Double.parseDouble(increaseBudgetBy);

                if (amount >= 0.0) {
                    budget.increaseBudget(amount);
                    JOptionPane.showMessageDialog(null,
                            ("Your new budget amount is: $" + budget.getTargetMonthlyBudget()),
                            "Increase Budget Request.",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot increase by a negative amount.",
                            "Invalid Budget Increase Request.",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        private class DecreaseBudgetAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Decrease Budget"
            DecreaseBudgetAction() {
                super("Decrease Budget");
            }

            // MODIFIES: MealBuddyApp
            // EFFECTS: takes user input to decrease the current monthly budget
            @Override
            public void actionPerformed(ActionEvent evt) {
                String decreaseBudgetBy = JOptionPane.showInputDialog(null,
                        "Please enter the amount to decrease the budget by: $",
                        ("Decrease Budget Request | Current Budget:" + budget.getTargetMonthlyBudget()),
                        JOptionPane.QUESTION_MESSAGE);

                double amount = Double.parseDouble(decreaseBudgetBy);

                if (amount >= 0.0) {
                    budget.decreaseBudget(amount);
                    JOptionPane.showMessageDialog(null,
                            ("Your new budget amount is: $" + budget.getTargetMonthlyBudget()),
                            "Decrease Budget Request.",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot decrease by a negative amount.",
                            "Invalid Budget Decrease Request.",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        private class LogGroceryPurchaseAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Log Grocery Purchase"
            LogGroceryPurchaseAction() {
                super("Log Grocery Purchase");
            }

            // MODIFIES: MealBuddyApp
            // EFFECTS: takes user input to log a grocery purchase
            @Override
            public void actionPerformed(ActionEvent evt) {
                String inputDate = JOptionPane.showInputDialog(null,
                        "Please enter the date of your grocery trip (1 - 31):",
                        "Log Grocery Purchase Request", JOptionPane.QUESTION_MESSAGE);
                int date = Integer.parseInt(inputDate);

                if (date >= 1 && date <= 31) {
                    String inputAmount = JOptionPane.showInputDialog(null,
                            "Please enter the amount spent on your grocery purchase: $",
                            "Log Grocery Purchase Request", JOptionPane.QUESTION_MESSAGE);
                    double amount = Double.parseDouble(inputAmount);

                    if (amount >= 0.0) {
                        budget.logGroceryTrip(amount, date);
                        JOptionPane.showMessageDialog(null,
                                ("Your have spent $" + budget.getCurrentSpending() + " on groceries this month."),
                                "Log Grocery Purchase Request",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Purchases must be greater than $0.0.",
                                "Invalid Grocery Log Request", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid date.", "Invalid Grocery Log Request",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        private class ViewBudgetBreakdownAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "View Budget Breakdown"
            ViewBudgetBreakdownAction() {
                super("View Budget Breakdown");
            }

            // EFFECTS: creates a budget breakdown bar graph for the user
            @Override
            public void actionPerformed(ActionEvent evt) {
                createBudgetBreakdownChart();
            }
        }

        // GROCERY MANAGER ACTIONS:
        private class GroceryManagerAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Grocery Manager"
            GroceryManagerAction() {
                super("Grocery Manager");
            }

            // MODIFIES: MenuUI
            // EFFECTS: takes the user to the grocery manager screen
            @Override
            public void actionPerformed(ActionEvent evt) {
                contentPane.removeAll();
                contentPane.add(createGroceryManagerButtons(), BorderLayout.CENTER);
                contentPane.validate();
                contentPane.repaint();
            }
        }

        private class AddItemAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Add Item to Grocery List"
            AddItemAction() {
                super("Add Item to Grocery List");
            }

            // MODIFIES: MealBuddyApp
            // EFFECTS: takes user input to add a food item to the grocery list
            @Override
            public void actionPerformed(ActionEvent evt) {
                String foodCategory = JOptionPane.showInputDialog(null, "Enter the category of the item you "
                                + "would like to add: ", "Add Item Request", JOptionPane.QUESTION_MESSAGE);

                String item = JOptionPane.showInputDialog(null, "Enter the name of the item you would "
                                + "like to add: ", "Add Item Request", JOptionPane.QUESTION_MESSAGE);

                String numItem = JOptionPane.showInputDialog(null, "Enter the quantity of the item you would "
                        + "like to add: ", "Add Item Request", JOptionPane.QUESTION_MESSAGE);
                int quantity = Integer.parseInt(numItem);

                if (quantity > 0) {
                    groceryList.addFoodItem(new FoodItem(foodCategory, item), quantity);
                    JOptionPane.showMessageDialog(null, (item + " was successfully added to the grocery list."),
                            "Add Item Request", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Quantity must be greater than 0.",
                            "Invalid Add Item Request", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        private class RemoveItemAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Remove Item From Grocery List"
            RemoveItemAction() {
                super("Remove Item From Grocery List");
            }

            // MODIFIES: MealBuddyApp
            // EFFECTS: takes user input to add a food item from the grocery list
            @Override
            public void actionPerformed(ActionEvent evt) {
                String itemName = JOptionPane.showInputDialog(null,
                        "Enter the name of the item that you would like to remove: ",
                        "Remove Item Request", JOptionPane.QUESTION_MESSAGE);
                ArrayList<FoodItem> itemList = groceryList.getFoodItems();
                boolean isItemInList = findItem(itemList, itemName);

                if (isItemInList) {
                    int position = findPosItem(itemList, itemName);
                    FoodItem item = itemList.get(position);
                    groceryList.removeFoodItem(item);

                    JOptionPane.showMessageDialog(null,
                            (itemName + " was successfully removed from the grocery list."),
                            "Remove Item Request",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "This item is not in the current grocery list",
                            "Invalid Remove Item Request",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        private class MarkAsPurchasedAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Mark Item as Purchased"
            MarkAsPurchasedAction() {
                super("Mark Item as Purchased");
            }

            // MODIFIES: MealBuddyApp
            // EFFECTS: takes user input to mark a food item in the grocery list as purchased
            @Override
            public void actionPerformed(ActionEvent evt) {
                String itemName = JOptionPane.showInputDialog(null,
                        "Enter the name of the item that you would like to mark as purchased: ",
                        "Mark as Purchased Request", JOptionPane.QUESTION_MESSAGE);
                ArrayList<FoodItem> itemList = groceryList.getFoodItems();
                boolean isItemInList = findItem(itemList, itemName);

                if (isItemInList) {
                    int position = findPosItem(itemList, itemName);
                    FoodItem item = itemList.get(position);
                    groceryList.purchaseItem(item);

                    JOptionPane.showMessageDialog(null,
                            (itemName + " was marked as purchased."),
                            "Mark as Purchased Request",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "This item is not in the current grocery list.",
                            "Invalid Mark as Purchased Request",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        private class SearchAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Search Grocery List"
            SearchAction() {
                super("Search Grocery List");
            }

            // EFFECTS: based on user input, will return food items within a given category or with a given name
            @Override
            public void actionPerformed(ActionEvent evt) {
                Object[] options = {"Search by item name", "Search by food category"};
                int n = JOptionPane.showOptionDialog(null, "Select one of the following options: ",
                        "Search Grocery List Request", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

                if (n == 0) {
                    String itemName = JOptionPane.showInputDialog(null,
                            "Enter the name of the item that you would like to search for: ",
                            "Search Grocery List By Item Name Request", JOptionPane.QUESTION_MESSAGE);
                    searchItem(itemName);
                } else {
                    String categoryName = JOptionPane.showInputDialog(null,
                            "Enter the food category that you would like to search for: ",
                            "Search Grocery List By Category Request", JOptionPane.QUESTION_MESSAGE);
                    searchCategory(categoryName);
                }

            }
        }

        private class GroceryListBreakdownAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "View Grocery List"
            GroceryListBreakdownAction() {
                super("View Grocery List");
            }

            // EFFECTS: creates a new frame that displays a table of the given grocery list
            @Override
            public void actionPerformed(ActionEvent evt) {
                ArrayList<FoodItem> foodItems = groceryList.getFoodItems();

                ArrayList<Integer> quantities = groceryList.getQuantityItemList();
                JFrame frame = new JFrame("Grocery List for " + month);
                frame.setSize(200, 500);
                JPanel groceryTable = new JPanel();
                groceryTable.setLayout(new GridLayout(0, 3));
                groceryTable.add(new JLabel("Category:"));
                groceryTable.add(new JLabel("Item name:"));
                groceryTable.add(new JLabel("Quantity:"));

                for (int i = 0; i < foodItems.size(); i++) {
                    String name = foodItems.get(i).getFoodName();
                    String category = foodItems.get(i).getFoodCategory();
                    String quantity = String.valueOf(quantities.get(i));

                    groceryTable.add(new JLabel(category));
                    groceryTable.add(new JLabel(name));
                    groceryTable.add(new JLabel(quantity));
                }

                frame.setContentPane(groceryTable);
                frame.pack();
                frame.setVisible(true);
            }
        }

        private class QuitAction extends AbstractAction {

            QuitAction() {
                super("Quit Meal Buddy App");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ScreenPrinter printer = new ScreenPrinter();
                printer.setVisible(true);
                printer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }



        // SAVE/LOAD MEAL BUDDY ACTIONS:
        private class SaveMealBuddyAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Save Meal Buddy"
            SaveMealBuddyAction() {
                super("Save Meal Buddy");
            }

            // MODIFIES: mealBuddyApp
            // EFFECTS: saves the current state of the meal buddy app
            @Override
            public void actionPerformed(ActionEvent evt) {
                saveMealBuddyApp();
            }
        }

        private class LoadMealBuddyAction extends AbstractAction {

            // EFFECTS: creates an abstract action called "Load Meal Buddy"
            LoadMealBuddyAction() {
                super("Load Meal Buddy");
            }

            // MODIFIES: mealBuddyApp
            // EFFECTS: loads a previous state of the meal buddy app from file
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadMealBuddyApp();
            }
        }


        // MODIFIES: this
        // EFFECTS: Adds an item with given handler to the given menu or new menu item
        private void addMenuItem(JMenu theMenu, AbstractAction action) {
            JMenuItem menuItem = new JMenuItem(action);
            theMenu.add(menuItem);
        }


         // EFFECTS: Represents action to be taken when user clicks desktop
        private class DesktopFocusAction extends MouseAdapter {

            @Override
            public void mouseClicked(MouseEvent e) {
                MenuUI.this.requestFocusInWindow();
            }
        }

        // EFFECTS: creates a panel of buttons for the budget manager screen
        private JPanel createBudgetManagerButtons() {
            JPanel budgetManagerContent = new JPanel();
            budgetManagerContent.setLayout(new GridLayout(4, 1));
            budgetManagerContent.add(new JButton(new IncreaseBudgetAction()));
            budgetManagerContent.add(new JButton(new DecreaseBudgetAction()));
            budgetManagerContent.add(new JButton(new LogGroceryPurchaseAction()));
            budgetManagerContent.add(new JButton(new ViewBudgetBreakdownAction()));

            return budgetManagerContent;

        }

        // EFFECTS: creates a panel of buttons for the grocery manager screen
        private JPanel createGroceryManagerButtons() {
            JPanel groceryManagerContent = new JPanel();
            groceryManagerContent.setLayout(new GridLayout(5, 1));
            groceryManagerContent.add(new JButton(new AddItemAction()));
            groceryManagerContent.add(new JButton(new RemoveItemAction()));
            groceryManagerContent.add(new JButton(new MarkAsPurchasedAction()));
            groceryManagerContent.add(new JButton(new SearchAction()));
            groceryManagerContent.add(new JButton(new GroceryListBreakdownAction()));

            return groceryManagerContent;

        }

        // MODIFIES: this
        // EFFECTS: creates a panel of buttons for the grocery manager screen
        private void createBudgetBreakdownChart() {
            JFrame frame = new JFrame("Spending By Week For " + month + " ($)");
            frame.setSize(WIDTH, HEIGHT);

            double[] amountSpent = new double[]{getBreakdownOfOneWeek(0, 7), getBreakdownOfOneWeek(7, 14),
                    getBreakdownOfOneWeek(14, 21), getBreakdownOfOneWeek(21, 28), getBreakdownOfOneWeek(28, 31)};

            Chart monthlyGraph = new Chart(amountSpent);
            frame.add(monthlyGraph);
            frame.setVisible(true);
        }

        private class Chart extends JPanel {

            private double[] values;
            private final String[] labels;
            private final Color[] colors;

            // REQUIRES: values.size() must be 5
            // EFFECTS: constructs a chart with labels, colours, and data values for the bars
            public Chart(double[] values) {
                this.labels = new String[]{"Week 1", "Week 2", "Week 3", "Week 4", "Week 5"};
                this.values = values;
                this.colors = new Color[]{
                        Color.red, Color.orange, Color.yellow, Color.green, Color.blue};
            }

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Dimension dim = getSize();
                int panelWidth = dim.width;
                int panelHeight = dim.height;
                int barWidth = panelWidth / 5;

                Font labelFont = new Font("Times New Roman", Font.PLAIN, 16);
                FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);
                int labelHeight = labelFontMetrics.getHeight();
                g.setFont(labelFont);
                double scale = (panelHeight - labelHeight - labelHeight) / 750.0;

                for (int i = 0; i < 5; i++) {
                    int positionX = i * barWidth;
                    int positionY = labelHeight + (int) ((750.0 - values[i]) * scale);
                    int barHeight = (int) (values[i] * scale);

                    g.setColor(colors[i]);
                    g.fillRect(positionX, positionY, barWidth, barHeight);
                    g.setColor(Color.black);
                    g.drawRect(positionX, positionY, barWidth, barHeight);

                    int labelWidth = labelFontMetrics.stringWidth(labels[i]);
                    int stringWidth = i * barWidth + (barWidth - labelWidth) / 2;
                    g.drawString(labels[i], stringWidth, panelHeight);
                }
            }
        }
    }

    // EFFECTS: calculates the sum of the grocery purchases in a given week
    private double getBreakdownOfOneWeek(int minDay, int maxDay) {
        double weeklyTotal = 0;

        for (int i : budget.getPurchaseDates()) {
            if (i > minDay && i <= maxDay) {
                int position = budget.getPurchaseDates().indexOf(i);
                weeklyTotal += budget.getSpendingHistory().get(position);
            }
        }
        return weeklyTotal;
    }

    // EFFECTS: returns true if a food item with itemName is in the item list; returns false otherwise.
    private boolean findItem(ArrayList<FoodItem> itemList, String itemName) {
        for (FoodItem f : itemList) {
            if (f.getFoodName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: a food item with itemName must be in the itemList
    // EFFECTS: returns the position of a given food item in an item list
    private int findPosItem(ArrayList<FoodItem> itemList, String itemName) {
        for (FoodItem f : itemList) {
            if (f.getFoodName().equals(itemName)) {
                return groceryList.getFoodItems().indexOf(f);
            }
        }
        return 0;
    }

    // EFFECTS: determines the quantity of a food item on the grocery list; if food item is not on grocery list then
    //          inform the user.
    private void searchItem(String itemName) {
        ArrayList<FoodItem> itemList = groceryList.getFoodItems();
        int quantity = 0;

        for (FoodItem f : itemList) {
            if (f.getFoodName().equals(itemName)) {
                quantity = groceryList.searchItem(f);
            }
        }

        if (quantity == 0) {
            JOptionPane.showMessageDialog(null,
                    ("This food item is not in the current grocery list."),
                    "Search Grocery List By Item Name Request",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    ("Quantity of " + itemName + " in the list: " + quantity),
                    "Search Grocery List By Item Name Request",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // EFFECTS: outputs the food items under a given food category provided by the user
    private void searchCategory(String categoryName) {
        ArrayList<FoodItem> itemsInCategory = groceryList.findItemsInCategory(categoryName);
        StringBuilder listOfItems = new StringBuilder();
        int numItems = 0;

        for (FoodItem f : itemsInCategory) {
            listOfItems.append(f.getFoodName()).append(" ... ");
            numItems++;
        }

        if (numItems == 0) {
            JOptionPane.showMessageDialog(null, "There are no items in the " + categoryName + " category.",
                    "Items in " + categoryName + " category: ",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    listOfItems.toString(), "Items in " + categoryName + " category: ",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // EFFECTS: saves the current state of the meal buddy app to file
    private void saveMealBuddyApp() {
        try {
            writer.open();
            writer.write(groceryTools);
            writer.close();
            JOptionPane.showMessageDialog(null,
                    "Saved Meal Buddy App to " + JSON_STORE,
                    "Save Meal Buddy Request",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to write to file: " + JSON_STORE,
                    "Save Meal Buddy Request",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads meal buddy app from file
    private void loadMealBuddyApp() {
        try {
            groceryTools = reader.read();
            groceryList = groceryTools.getGroceryList();
            budget = groceryTools.getMonthlyBudget();
            JOptionPane.showMessageDialog(null,
                    "Loaded Meal Buddy App from " + JSON_STORE,
                    "Load Meal Buddy Request",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to read from file: " + JSON_STORE,
                    "Load Meal Buddy Request",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
