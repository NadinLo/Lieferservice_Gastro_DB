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
                startEditIngredient(0);
            } if (decision == 5) {
                //edit menu
                startEditingMeals(0);
            } if (decision == 6) {
                System.out.println("ok - just finished this editing program");
            } else {
                System.out.println("This input wasn't correct.");
            }
        }
    }

    private void startEditIngredient (int a) {
        while (a != 4) {
            a = editingView.editingIngredients();
            if (a == 1) {
                //add ingredients
                ingredientRepository.create(ingredientView.addIngredient());
            }
            if (a == 2) {
                //delete ingredents
                ingredientView.printAllIngredients(ingredientRepository.findAll());
                ingredientRepository.delete(ingredientView.deleteIngredient());
            }
            if (a == 3) {
                //edit price
                ingredientView.printAllIngredients(ingredientRepository.findAll());
                ingredientRepository.updatePrice(ingredientView.IngredientId(), ingredientView.changeSinglePrice());
            }
            if (a == 4) {
                System.out.println("Finish the editing ingredient program ");
            }
            else {
                System.out.println("Unknown entry. Try again");
            }
        }
    }

    private void startEditingMeals (int a) {
        while (a != 5) {
            a = editingView.editingMeals();
            if (a == 1){
                //add menu
                Meal meal;
                while (true) {
                    meal = new Meal(0,
                            mealView.giveMealName(),
                            mealView.giveMealType(mealRepository.getAllMealTypes()),
                            mealView.giveMealPrice());
                    ArrayList<Integer> ingredients = ingredientView.mealIngredients(ingredientRepository.findAll());
                    for (int i = 0; i < ingredients.size(); i++) {
                        meal.getIngredients().add(ingredientRepository.findOne(ingredients.get(i)));
                    }
                    //to be able to use the print method I need an Array
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
                ingredientView.printAllIngredients(ingredientRepository.findAll());
                int ingredientId = 1;
                while (ingredientId != 0) {
                    ingredientId = ingredientView.editMealIngredient();
                    mealRepository.updateMealIngredient(mealId, ingredientId);
                }
            }
            if (a == 3){
                //update price
                mealRepository.updatePrice(mealView.chooseMealToChange(mealRepository.findAll()), mealView.editPrice());
            }
            if (a == 4){
                //delete menu
                mealRepository.deleteMeal(mealRepository.findOne(mealView.chooseMealToChange(mealRepository.findAll()) ));
            }
            if (a == 5) {
                System.out.println("Finish the meal editing program");
            }
            else {
                System.out.println("Unknown entry. Try again.");
            }
        }
    }
}
