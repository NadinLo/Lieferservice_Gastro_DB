package com.company.view;

import com.company.model.Order;
import com.company.model.User;

import java.util.ArrayList;
import java.util.Scanner;

public class AnalyzeView {
    Scanner scannerForInt = new Scanner(System.in);

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

    public void countOrdersPerCustomer (ArrayList<User> users){
        int count = 1;
        for (int i = 0; i < users.size(); i++) {
            if (users.size() > 1) {
                if (i > 0) {
                    if (users.get(i).getLocation().equalsIgnoreCase(users.get(i - 1).getLocation()) &&
                            users.get(i).getAddress().equalsIgnoreCase(users.get(i - 1).getAddress()) &&
                            users.get(i).getName().equalsIgnoreCase(users.get(i - 1).getName()) &&
                            i < users.size() - 1) {
                        count++;
                    }
                    else if ( !(users.get(i).getLocation().equalsIgnoreCase(users.get(i - 1).getLocation()) &&
                            users.get(i).getAddress().equalsIgnoreCase(users.get(i - 1).getAddress()) &&
                            users.get(i).getName().equalsIgnoreCase(users.get(i - 1).getName())) || i == users.size()-1) {
                        System.out.println(users.get(i-1).getName() + ", " + users.get(i-1).getAddress() + ", " +
                                users.get(i-1).getLocation() + " ordered " + count + " times");
                        count = 1;
                    }
                }
            }

        }
    }

    public void countPerLocation (ArrayList<User> users){
        int count = 1;
        for (int i = 0; i < users.size(); i++) {
            if (users.size() > 1) {
                if (i > 0) {
                    if (users.get(i).getLocation().equalsIgnoreCase(users.get(i - 1).getLocation()) &&
                            i < users.size() - 1) {
                        count++;
                    }
                    else if ( !(users.get(i).getLocation().equalsIgnoreCase(users.get(i - 1).getLocation())) || i == users.size()-1) {
                        System.out.println(count + " orders from " + users.get(i-1).getLocation());
                        count = 1;
                    }
                }
            }

        }
    }
}
