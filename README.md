# Meal Buddy - A CPSC 210 Personal Project

## What is it?

***Meal Buddy*** is a groccery and meal planning application created for university 
students and other young adults who are navigating the daunting process of 
living alone and cooking meals. 

To help make this transition into adulthood and independence easier, 
Meal Buddy serves as an all-in-one interactive tool that will help individuals
better manage their grocery shopping and meal planning. Some ***key features*** that 
I may include in the Meal Buddy app include:

- **Budget Tracker** - users can set a monthly budget and log purchases 
throughout the month
- **Meal Calendar** - users can plan and rate meals they have made
- **Grocery Shopping List** - users can record food items that they need to purchase
- **Pantry Tracker** - users can log groceries that they have purchased/consumed
to prevent food spoilage

## Why does this project interest me?
As a fellow university student living across the country from my family, 
learning to live alone  has undoubtedly been a difficult process. In 
sharing this experience with my roommates and friends, it is evident that 
this is a challenge that is not unique to me alone.

When I moved out of first year residence, one of the biggest changes was I 
no longer had a meal plan and needed to learn how to feed and sustain myself. Starting 
off, I noticed that due to a lack of planning I would order food/eat out 
more than I should, food would spoil in the fridge, and I would be taking 
multiple trips to the store every week. Evidently, my strategy to *"wing it"* 
was quite inefficient and ineffective.

In developing Meal Buddy, I'm hoping to take what I have learned thus far and
create a tool that will help me avoid some of these past mistakes and perhaps 
help others do the same. 

## User Stories

**Phase 1:**
- As a user, I want to be able to set a monthly budget amount.
- As a user, I want to be able to add a food item to a grocery shopping list
- As a user, I want to be able to sort my grocery shopping list by food category.
- As a user, I want to be able to check a food item as purchased.
- As a user, I want to be able to log the cost of my grocery shopping trip.
- As a user, I want to be able to view my grocery spending by week.

**Phase 2:**
- As a user, at any point, I want to be able to save the current state of the meal buddy app to file.
- As a user, I want to be able to reload a previous meal buddy app from file and resume 
exactly where I left off.


**Phase 3: Instructions for Grader**

- You can ***generate the first required event*** by selecting grocery manager from the main menu and then selecting "add item
to grocery list".


- You can ***generate the second required event*** by selecting grocery manager from the main menu and then selecting "search
grocery list" Searching by item name will return the quantity of that food item within the grocery list. Searching by 
food category will return all food items of the given category within the grocery list. To view the entire grocery list, select "View
Grocery List" from the main menu.


- You can locate my ***visual component*** by selecting budget manager and then selecting "View budget breakdown". If grocery
purchases have been logged for this month, a bar graph displaying spending by week will appear.


- You can ***save the state of my application*** by selecting "Save Meal Buddy" from the main menu.


- You can ***reload the state of my application*** by selecting "Load Meal Buddy" from the main menu.

**Phase 4: Task 2 Representative Sample of Events**

Thu Aug 11 02:17:48 PDT 2022
Added food item to grocery list: Carrot. Quantity on list: 3

Thu Aug 11 02:17:53 PDT 2022
Added food item to grocery list: Carrot. Total quantity on list: 8

Thu Aug 11 02:17:57 PDT 2022
Added food item to grocery list: Milk. Quantity on list: 2

Thu Aug 11 02:18:03 PDT 2022
Added food item to grocery list: Cheese. Quantity on list: 5

Thu Aug 11 02:18:09 PDT 2022
Added food item to grocery list: Lettuce. Quantity on list: 2

Thu Aug 11 02:18:14 PDT 2022
Removed food item from grocery list: Lettuce

Thu Aug 11 02:18:16 PDT 2022
Marked food item from grocery list as purchased: Carrot

Thu Aug 11 02:18:23 PDT 2022
Added food item to grocery list: Carrot. Quantity on list: 5

**Phase 4: Task 3**

If I had more time, to improve the design of my project, I would refactor in the following ways:

- There is relatively low cohesion in the MealBuddyApp Class as there are two different "categories" 
of related behaviours that could be refactored into separate classes. To improve this, I would 
separate the functionality related to the budget and the functionality related to the grocery 
list by creating new BudgetUI and GroceryUI classes. 


- From the UML class diagram that I drew, I noticed that the association between the MealBuddyApp class
and the GroceryList and MonthlyBudget classes isn't necessary as there is already an indirect association
through grocery tools. As a result, the direct association can be removed. If required, local variables for 
a GroceryList and a MonthlyBudget could be created as dependencies.


- The actionPerformed method in the RemoveItemAction and MarkAsPurchasedAction classes are very similar. 
To reduce repetition, a new method can be called and created to encompass these similarities. 


- For each action that can be performed, when the user input is invalid (e.g. a negative amount entered
when requesting to increase the budget) an error message appears that is relatively similar each time. This 
can be refactored into a new method that creates a JOptionPane message dialog with title "Invalid Request" 
and a specific message describing the error which could be taken as a String parameter.