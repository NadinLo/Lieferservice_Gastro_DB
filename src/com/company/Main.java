package com.company;

import com.company.controller.AnalyzeController;
import com.company.controller.EditingController;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scannerForInt = new Scanner(System.in);
    private static Scanner scannerForString = new Scanner(System.in);
    private static DecimalFormat df = new DecimalFormat("##.##");

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

    private static int soldTheMost(){
        Connection conn;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String query = "";
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> menuNames = new ArrayList<>();
            ArrayList<Integer> amounts = new ArrayList<>();
            while (rs.next()){
                menuNames.add(rs.getString("menu.name"));
                amounts.add(rs.getInt("COUNT(menu_auswahl.menu_nr)"));
            }
            System.out.println(menuNames.get(0) + " was sold the most (" + amounts.get(0) + " times)");

            for (int i = 1; i < amounts.size(); i++) {
                if (amounts.get(i).equals(amounts.get(0))){
                    System.out.println(menuNames.get(i) + "was also sold " + amounts.get(i) + " times");
                }
            }
            System.out.println("---------------------------------------");
        } catch (SQLException ex){
            throw new Error("something went wrong with the query what was sold the most", ex);
        }

    return 0;
    }

    private static int orderOfSoldMenus(){
        Connection conn;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String query = "SELECT menu.name, COUNT(menu_auswahl.menu_nr) " +
                    "FROM `menu_auswahl` " +
                    "INNER JOIN menu ON menu_auswahl.menu_nr = menu.menu_nr " +
                    "GROUP BY menu_auswahl.menu_nr " +
                    "ORDER BY COUNT(menu_auswahl.menu_nr) DESC ";
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> menuNames = new ArrayList<>();
            ArrayList<Integer> amounts = new ArrayList<>();
            while (rs.next()){
                menuNames.add(rs.getString("menu.name"));
                amounts.add(rs.getInt("COUNT(menu_auswahl.menu_nr)"));
            }
            for (int i = 0; i < amounts.size(); i++) {
                System.out.println(menuNames.get(i) + " was sold the most (" + amounts.get(i) + " times)");
            }
            System.out.println("---------------------------------------");
        } catch (SQLException ex){
            throw new Error("something went wrong with the query what was sold the most", ex);
        }
        return 0;
    }




}
