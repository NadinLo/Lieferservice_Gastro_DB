package com.company.view;

import com.company.model.Meal;
import com.company.model.Order;
import com.company.model.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class AnalyzeView {
    Scanner scannerForInt = new Scanner(System.in);
    DecimalFormat df = new DecimalFormat("##.##");

    public int analyzeMenu() {
        System.out.println("You have now following possibilities:");
        System.out.println("1) how many orders were taken place till now?");
        System.out.println("2) how many orders were taken place for each customer?");
        System.out.println("3) how many orders were taken place for each location?");
        System.out.println("4) Sales in total/ per customer/ per location");
        System.out.println("5) What was soled the most and how often?");
        System.out.println("6) Order of the sold menus - the most successful is named first");
        System.out.println("7) finish analyzing program");

        return scannerForInt.nextInt();
    }

    public int analyzeSaleMenu () {
        System.out.println("You have now following possibilities:");
        System.out.println("1) sales in total");
        System.out.println("2) sales per customer");
        System.out.println("3) sales per location?");
        System.out.println("4) finish analyzing program");

        return scannerForInt.nextInt();
    }

    public void countAllOrders (ArrayList<Order> orders) {
        System.out.println(orders.size() + " till now");
    }

    public void countOrdersPerCustomer (User user, ArrayList<Order> orders){
        System.out.println(user.getName() + ", " + user.getAddress() + ", " + user.getLocation() + " ordered " +
                orders.size() + " time(s)");

    }

    public void countPerLocation (User user, ArrayList<Order> orders){
        System.out.println(orders.size() + " order(s) from " + user.getLocation());
    }

    public void salesInTotal (ArrayList<Order> orders){
        double salesInTotal = 0;
        for (Order order : orders) {
            salesInTotal = salesInTotal + order.getPriceInTotal();

        }
        System.out.println("Sales in Total: " + df.format(salesInTotal) + "€");
    }

    public void salesPerCustomer(User user, ArrayList<Order> orders){
        double salesInTotal = 0;
        for (Order order : orders) {
            salesInTotal = salesInTotal + order.getPriceInTotal();
        }
        System.out.println(user.getName() + ", " + user.getAddress() + ", " + user.getLocation() +
                " ordered for " + df.format(salesInTotal) + "€ in total");

    }

    public void salesPerLocation (User user, ArrayList<Order> orders) {
        double salesInTotal = 0;
        for (Order order : orders) {
            salesInTotal = salesInTotal + order.getPriceInTotal();
        }
        System.out.println(df.format(salesInTotal) + "€ in Total from " + user.getLocation());

    }

    public void soldTheMost (ArrayList<Meal> meals, ArrayList<Integer> amounts) {
        if (meals.size()>1){
            for (int i = 0; i < meals.size(); i++) {
                if (amounts.get(i) > amounts.get(i+1)){
                    System.out.println(meals.get(i).getName() + " was/were sold the most (" + amounts.get(i) + " time[s])");
                    break;
                }
                else {
                    System.out.print(meals.get(i).getName() + ", ");
                }

            }
        } else {
            System.out.println(meals.get(0).getName() + " was/were sold the most (" + amounts.get(0) + " time[s])");

        }
    }

    public void listOfSoldTheMost(ArrayList<Meal> meals, ArrayList<Integer> amounts) {
        if (meals.size()>1){
            for (int i = 0; i < meals.size(); i++) {
                if (amounts.get(i) > amounts.get(i+1)){
                    System.out.println(meals.get(i).getName() + " was/were sold the most (" + amounts.get(i) + " time[s])");
                }
                else {
                    System.out.print(meals.get(i).getName() + ", ");
                }

            }
        } else {
            System.out.println(meals.get(0).getName() + " was/were sold the most (" + amounts.get(0) + " time[s])");

        }
    }
}
