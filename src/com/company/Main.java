package com.company;

import com.company.controller.AnalyzeController;
import com.company.controller.EditingController;
import java.util.Scanner;

public class Main {
    private static Scanner scannerForInt = new Scanner(System.in);

    public static void main(String[] args) {

        EditingController editingController = new EditingController();
        AnalyzeController analyzeController = new AnalyzeController();

        int decision = 0;
        while (decision != 4) {
            System.out.println("Hello chef! What is your aim to do?\n" +
                    "Editing the menu -- press 1\n " +
                    "Analyze Data -- press 2\n " +
                    "Check orders -- press 3\n" +
                    "Quit program -- press 4");
            decision = scannerForInt.nextInt();
            if (decision == 1) {
                editingController.start();
            } else if (decision == 2) {
                analyzeController.start();
            } else if (decision == 3) {
                //todo: check orders
            } else if (decision == 4) {
                System.out.println("Thank you and good bye");
            } else {
                System.out.println("This input wasn't correct.");
            }
        }

    }
}
