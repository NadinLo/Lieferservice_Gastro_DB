package com.company.view;

import com.company.model.Ingredient;
import com.company.model.Meal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class MealView {
    DecimalFormat df = new DecimalFormat("##.##");
    Scanner scannerForInt = new Scanner(System.in);
    Scanner scannerForString = new Scanner(System.in);

    private void printHeader (){
        System.out.println("_Meal_type_____|_Meal_No_|_Meal________________|_Vegi_|_Price______");
    }

    public void printMenu (ArrayList<Meal> menu){
        printHeader();
        for (int i = 0; i < menu.size(); i++) {
            if (i > 0 && menu.get(i).getMenuType().equalsIgnoreCase(menu.get(i-1).getMenuType())){
                System.out.println("               | " + (menu.get(i).getId() + "      ").substring(0,8) +
                        "| " + (menu.get(i).getName() + "                      ").substring(0,20) +
                        "| " + (menu.get(i).isVegetarian() + "          ").substring(0,5) +
                        "| " + (df.format(menu.get(i).getMenuPrice()) + " €"));
            } else {
                System.out.println(" " + (menu.get(i).getMenuType() + "          ").substring(0,14) +
                        "| " + (menu.get(i).getId() + "      ").substring(0,8) +
                        "| " + (menu.get(i).getName() + "                      ").substring(0,20) +
                        "| " + (menu.get(i).isVegetarian() + "          ").substring(0,5) +
                        "| " + (df.format(menu.get(i).getMenuPrice()) + " €"));
            }
            printIngredientsMenu(menu.get(i).getIngredients());
        }
    }

    private void printIngredientsMenu (ArrayList<Ingredient> ingredients){
        for (Ingredient ingredient : ingredients) {
            System.out.println("               |         |" +
                    " - " + (ingredient.getName() + "                     ").substring(0, 18) + "|      |            ");
        }
    }

    public void printMealTypes (ArrayList<String> types){
        for (int i = 0; i < types.size(); i++) {
            System.out.println((" " + i + 1 + "   ").substring(0,4) + "| " + types.get(i));
        }
    }

    public String giveMealName () {
        System.out.println("enter the name for the new meal.");
        return scannerForString.nextLine();
    }

    public String giveMealType (ArrayList<String> types) {
        printMealTypes(types);
        System.out.println("enter the number of the appropriate meal type.");
        return types.get(scannerForInt.nextInt() - 1);
    }

    public double giveMealPrice () {
        System.out.println("How much should the new meal be?");
        return scannerForInt.nextDouble();
    }

    public boolean confirmNewMeal (ArrayList<Meal> meals) {
        printMenu(meals);
        System.out.println("Are you ok with your choice? Enter 'y' for yes or 'n' for no.");
        if (scannerForString.nextLine().equalsIgnoreCase("y")) {
            return true;
        } else {
            return false;
        }
    }

    public int chooseMealToChange (ArrayList<Meal> menu) {
        printMenu(menu);
        System.out.println("Enter the number of meal which you want to change");
        return scannerForInt.nextInt();
    }




}
