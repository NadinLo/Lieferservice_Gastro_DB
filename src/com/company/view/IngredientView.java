package com.company.view;

import com.company.model.Ingredient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class IngredientView {
    DecimalFormat df = new DecimalFormat("##.##");
    Scanner scannerForInt = new Scanner(System.in);
    Scanner scannerForString = new Scanner(System.in);

    public void printAllIngredients (ArrayList<Ingredient> ingredients){
        System.out.println("ALL INGREDIENTS----------------------------------------");
        System.out.println("_id_|_name__________________|_is_veggi_|_price_________");
        for (Ingredient ingredient : ingredients) {
            System.out.println((" " + ingredient.getId() + "   ").substring(0, 4) + "| " +
                    (ingredient.getName() + "                      ").substring(0, 22) + "| " +
                    (ingredient.isVegetarian() + "         ").substring(0, 9) + "| " +
                    df.format(ingredient.getSinglePrice()) + " €");
        }
        System.out.println("------------------------------------------------\n");
    }

    public Ingredient addIngredient () {
        System.out.println("Enter the name of the new ingredient");
        String name = scannerForString.nextLine();
        System.out.println("Is this ingredient vegetarian? Enter 'true' or 'false'");
        boolean vegetarian = scannerForInt.nextBoolean();
        System.out.println("Enter the price of the ingredient");
        double price = scannerForInt.nextDouble();

        return new Ingredient(0, name, vegetarian, price);
    }

    public int deleteIngredient () {
        System.out.println("Enter the number of the ingredient you want to take off of the list");
        return scannerForInt.nextInt();
    }

    public int IngredientId () {
        System.out.println("Which ingredient do you want to change?");
        return scannerForInt.nextInt();
    }

    public double changeSinglePrice () {
        System.out.println("enter the new Price");
        return scannerForInt.nextDouble();
    }

    public ArrayList<Integer> mealIngredients (ArrayList<Ingredient> ingredients){
        printAllIngredients(ingredients);
        ArrayList<Integer> mealIngredients = new ArrayList<>();
        System.out.println("Add one after another the ingredients your meal should have. " +
                "Therefor enter the ingredient number and press enter.\n" +
                "You need at least 2 ingredients and maximum 10. If you want to finish press 0");
        for (int i = 0; i < 11; i++) {
            System.out.println("Currently your menu has " + i + " ingredient(s)");
            int ingredientID = scannerForInt.nextInt();
            if(ingredientID != 0) {
                mealIngredients.add(ingredientID);
            } else { break;}
        }
        return mealIngredients;
    }

    public int editMealIngredient() {
        System.out.println("Now enter one after another the number of the ingredient you want to add. If you want " +
                "to delete single ingredients from the menu list just write \"-\" before the number." +
                "Finish your entry with \"0\"");
        return scannerForInt.nextInt();
    }
}
