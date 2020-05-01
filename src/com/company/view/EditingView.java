package com.company.view;

import java.util.Scanner;

public class EditingView {
    private Scanner scannerForInt = new Scanner(System.in);

    public int editingMenu (){
        System.out.println("You have now following possibilities:");
        System.out.println("1) show complete list of ingredients");
        System.out.println("2) show all menu types");
        System.out.println("3) show complete list of menus");
        System.out.println("4) edit ingredients");
        System.out.println("5) edit menus");
        System.out.println("6) finish editing program");

        return scannerForInt.nextInt();
    }

    public int editingIngredients () {
        System.out.println("You have now following possibilities:");
        System.out.println("1) add ingredients to the list");
        System.out.println("2) delete ingredients from list");
        System.out.println("3) edit price");
        System.out.println("4) finish editing program");

        return scannerForInt.nextInt();
    }

    public int editingMeals () {
        System.out.println("You have now following possibilities:");
        System.out.println("1) add menu");
        System.out.println("2) update ingredients in a specific menu");
        System.out.println("3) update price");
        System.out.println("4) delete menu");
        System.out.println("5) finish editing program");

        return scannerForInt.nextInt();
    }
}
