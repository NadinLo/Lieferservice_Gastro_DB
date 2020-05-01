package com.company.controller;

import com.company.model.IngredientRepository;
import com.company.model.Meal;
import com.company.model.MealRepository;
import com.company.view.EditingView;
import com.company.view.IngredientView;
import com.company.view.MealView;

import java.util.ArrayList;

public class EditingController {
    EditingView editingView = new EditingView();
    IngredientView ingredientView = new IngredientView();
    IngredientRepository ingredientRepository = new IngredientRepository();
    MealRepository mealRepository = new MealRepository();
    MealView mealView = new MealView();

    public void start () {
        int decision = 0;
        while (decision != 6) {
            decision = editingView.editingMenu();
            if (decision == 1) {
                //show complete list of ingredients
                ingredientView.printAllIngredients(ingredientRepository.findAll());
            } if (decision == 2){
                //show all menu types
                mealView.printMealTypes(mealRepository.getAllMealTypes());
            } if (decision == 3) {
                //show complete menu
                mealView.printMenu(mealRepository.findAll());
            } if (decision == 4) {
                //edit ingredients
                startEditIngredient(editingView.editingIngredients());
            } if (decision == 5) {
                //edit menu
                startEditingMeals(editingView.editingMeals());
            } if (decision == 6) {
                System.out.println("ok - just finished this editing program");
            } else {
                System.out.println("This input wasn't correct.");
            }
        }
    }

    private void startEditIngredient (int a) {
        while (a != 4) {

            if (a == 1) {
                //add ingredients
                ingredientRepository.create(ingredientView.addIngredient());
            } if (a == 2) {
                //delete ingredents
                ingredientRepository.delete(ingredientView.deleteIngredient());
            } if (a == 3) {
                //edit price
                ingredientRepository.updatePrice(ingredientView.IngredientId(), ingredientView.changeSinglePrice());
            } else if (a == 4) {
                System.out.println("ok - just finished the editing program");
            }
        }
    }

    private void startEditingMeals (int a) {
        while (a != 5) {
            if (a == 1){
                //add menu
                Meal meal;
                while (true) {
                    meal = new Meal(0,
                            mealView.giveMealName(),
                            mealView.giveMealType(mealRepository.getAllMealTypes()),
                            mealView.giveMealPrice());
                    ArrayList<Integer> ingredients = ingredientView.mealIngredients(ingredientRepository.findAll());
                    for (Integer ingredientId : ingredients) {
                        meal.getIngredients().add(ingredientRepository.findOne(ingredientId));
                    }
                    ArrayList<Meal> meals = new ArrayList<>();
                    meals.add(meal);
                    if (mealView.confirmNewMeal(meals)) {
                        break;
                    }
                }
                mealRepository.create(meal);
            }
            if (a == 2) {
                //update ingredients in menu
                //which menu/ delete or add ingredient
                int mealId = mealView.chooseMealToChange(mealRepository.findAll());
                int ingredientId = 1;
                while (ingredientId != 0) {
                    ingredientId = ingredientView.editMealIngredient();
                    mealRepository.updateMealIngredient(mealId, ingredientId);
                }
            }

            if (a == 3){
                //update price
                int menuID;
                double price;
                System.out.println("Enter the number of the menu you want to update the price.");
                menuID = scannerForInt.nextInt();
                System.out.println("Enter now the new wanted price");
                price = scannerForInt.nextDouble();
                decision = updatePriceMenu (menuID, price);
            }

            if (a == 4){
                //delete menu
                System.out.println("Which menu do you want to delete? Please enter the number.");
                int menuNo = scannerForInt.nextInt();
                decision = deleteMenu(menuNo);
            }
            if (a == 5){
                System.out.println("Ok - just finished the editing program");
            }
        }
    }
}
