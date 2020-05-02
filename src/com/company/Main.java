package com.company;

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
                decision = analyzeData();
            } else if (decision == 4) {
                System.out.println("Thank you and good bye");
            } else {
                System.out.println("This input wasn't correct.");
            }
        }

    }

    private static int analyzeData () {
        int decision = 0;
        while (decision == 0) {
            System.out.println("You have now following possibilities:");
            System.out.println("1) how many orders were taken place till now?");
            System.out.println("2) how many orders were taken place for each customer?");
            System.out.println("3) how many orders were taken place for each location?");
            System.out.println("4) Sales in total/ per customer/ per location");
            System.out.println("5) What was soled the most and how often?");
            System.out.println("6) Order of the sold menus - the most successful is named first");
            System.out.println("7) finish analyzing program");

            decision = scannerForInt.nextInt();
            if (decision == 1) {
                //how many orders were taken place till now?
                decision = ordersInTotal();
            } else if (decision == 2) {
                //how many orders were taken place for each customer?
                decision = ordersPerCustomer();
            } else if (decision == 3) {
                //how many orders were taken place for each location?
                decision = ordersPerLocation ();
            } else if (decision == 4) {
                //Sales in total/ per customer/ per location => to next menu
                decision = sales();
            } else if (decision == 5) {
                //What was soled the most and how often?
                decision = soldTheMost();
            } else if (decision == 6) {
                //Order of the sold menus - the most successful is named first
                decision = orderOfSoldMenus();
            } else if (decision == 7) {
                System.out.println("ok - just finished this editing program");
            } else {
                System.out.println("This input wasn't correct.");
                decision = 0;
            }
        }

        return 0;
    }

    private static int ordersInTotal (){
        Connection conn;
        int amountOrdersInTotal = 0;
        try{
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM `bestellung` ";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                amountOrdersInTotal = rs.getInt("COUNT(*)");
            }
            System.out.println(amountOrdersInTotal + " orders were done till now.");
            System.out.println("--------------------------------");

        } catch (SQLException ex){
            throw new Error("somthing went wrong with analyzing of Orders in total", ex);
        }
        return 0;
    }

    private static int ordersPerCustomer (){
        Connection conn;
        int amountOrdersPerCustomer;
        String nameCustomer;
        String address;
        String location;
        //todo: implement category guest customer and registered customer
        try{
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String query = "SELECT COUNT(*), kunde.name ,kunde.straße_hnr, belieferte_ortschaften.name " +
                    "FROM kunde " +
                    "INNER JOIN belieferte_ortschaften on kunde.ortschaft = belieferte_ortschaften.id " +
                    "GROUP BY belieferte_ortschaften.name, kunde.straße_hnr, kunde.name";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                amountOrdersPerCustomer = rs.getInt("COUNT(*)");
                nameCustomer = rs.getString("kunde.name");
                location = rs.getString("belieferte_ortschaften.name");
                address = rs.getString("kunde.straße_hnr");
                System.out.println(nameCustomer + " from " + location + " (" + address + ") has ordered " + amountOrdersPerCustomer + " time(s)");
            }
            System.out.println("---------------------------------------------------------------");
        } catch (SQLException ex){
            throw new Error("somthing went wrong with analyzing of Orders per customer", ex);
        }
        return 0;
    }

    private static int ordersPerLocation () {
        Connection conn;
        int amountOrdersPerLocation;
        String location;
        try{
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String query = "SELECT COUNT(*), belieferte_ortschaften.name " +
                    "FROM kunde " +
                    "INNER JOIN belieferte_ortschaften on kunde.ortschaft = belieferte_ortschaften.id " +
                    "GROUP BY belieferte_ortschaften.name";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                amountOrdersPerLocation = rs.getInt("COUNT(*)");
                location = rs.getString("belieferte_ortschaften.name");
                System.out.println(amountOrdersPerLocation + " order(s) in " + location);
            }
            System.out.println("--------------------------------------");
        } catch (SQLException ex){
            throw new Error("somthing went wrong with analyzing of Orders per location", ex);
        }
        return 0;
    }

    private static int sales(){
        int decision = 0;
        while (decision == 0) {
            System.out.println("You have now following possibilities:");
            System.out.println("1) sales in total");
            System.out.println("2) sales per customer");
            System.out.println("3) sales per location?");
            System.out.println("4) finish analyzing program");

            decision = scannerForInt.nextInt();
            if (decision == 1) {
                //in total
                decision = salesInTotal();
            } else if (decision == 2) {
                //per customer
                decision = salesPerCustomer();
            } else if (decision == 3) {
                //per location
                decision = salesPerLocation ();
            } else if (decision == 4) {
                System.out.println("ok - just finished this editing program");
            } else {
                System.out.println("This input wasn't correct.");
                decision = 0;
            }
        }

        return 0;
    }

    private static int salesInTotal() {
        Connection conn;
        double salesInTotal = 0;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            try {
                Statement stmt = conn.createStatement();
                String query = "SELECT bestellung.bestellnr FROM `bestellung`";
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    int orderNo = rs.getInt("bestellung.bestellnr");
                    double priceOrder = salesSubQueries(orderNo);
                    salesInTotal = salesInTotal + priceOrder;
                }
            } catch (SQLException ex) {
                throw new Error("something went wrong with query the order number");
            }
            System.out.println("sales in Total: " + df.format(salesInTotal) + "€");
            System.out.println("--------------------------------------");
        } catch (SQLException ex) {
            throw new Error("somthing went wrong with analyzing of sales in total", ex);
        }

        return 0;
    }

    private static int salesPerCustomer(){
        Connection conn;
        double salesPerCustomer = 0;
        ArrayList<String> results = new ArrayList<>();
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String customer;
            String x = null;
            String query = "SELECT kunde.bestellnr, kunde.name ,kunde.straße_hnr, belieferte_ortschaften.name " +
                    "FROM kunde " +
                    "INNER JOIN belieferte_ortschaften on kunde.ortschaft = belieferte_ortschaften.id " +
                    "GROUP BY belieferte_ortschaften.name, kunde.straße_hnr, kunde.name";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                int orderNo = rs.getInt("kunde.bestellnr");
                String nameCustomer = rs.getString("kunde.name");
                String address = rs.getString("kunde.straße_hnr");
                String location = rs.getString("belieferte_ortschaften.name");
                customer = nameCustomer + " from " + location + " (" + address + ")";
                double priceOrder = salesSubQueries(orderNo);
                if (!customer.equalsIgnoreCase(x) && x != null) {
                    results.add(x + " ordered for " + df.format(salesPerCustomer) + " €");
                    salesPerCustomer = 0;
                }
                salesPerCustomer = salesPerCustomer + priceOrder;
                x = customer;
            }
            results.add(x + " ordered for " + df.format(salesPerCustomer) + " €");
            for (String result : results) {
                System.out.println(result);
            }
        } catch (SQLException ex){
            throw new Error("something went wrong with salesPerLocation", ex);
        }
        System.out.println("------------------------------------------------------");

        return 0;
    }

    private static int salesPerLocation(){
        Connection conn;
        ArrayList<Integer> locations = new ArrayList<>();
        ArrayList<String> locationNames = new ArrayList<>();
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String query = "SELECT belieferte_ortschaften.id, belieferte_ortschaften.name " +
                    "FROM `belieferte_ortschaften` ORDER BY belieferte_ortschaften.distance";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                locations.add(rs.getInt("belieferte_ortschaften.id"));
                locationNames.add(rs.getString("belieferte_ortschaften.name"));
            }
            for (int i = 0; i < locations.size(); i++) {
                double salesPerLocation = 0;
                query = "SELECT kunde.bestellnr FROM `kunde` WHERE kunde.ortschaft = " + locations.get(i);
                rs = stmt.executeQuery(query);
                while (rs.next()){
                    int orderNo = rs.getInt("kunde.bestellnr");
                    salesPerLocation = salesPerLocation + salesSubQueries(orderNo);
                }
                System.out.println(locationNames.get(i) + ": " + df.format(salesPerLocation) + " € in total");
            }
        } catch (SQLException ex){
            throw new Error("something went wrong with salesPerLocation", ex);
        }
        System.out.println("--------------------------------------");

        return 0;
    }

    private static double salesSubQueries (int orderNo) {
        Connection conn;
        double priceOrder = 0;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            try {
                Statement stmt2 = conn.createStatement();
                String query2 = "SELECT menu.preis," +
                        "menu_auswahl.id_detail_auswahl, " +
                        "menu_auswahl.anzahl " +
                        "FROM `menu_auswahl` " +
                        "INNER JOIN menu ON menu_auswahl.menu_nr = menu.menu_nr " +
                        "WHERE menu_auswahl.bestell_nr = " + orderNo;
                ResultSet rs2 = stmt2.executeQuery(query2);
                while (rs2.next()) {
                    double priceCompleteMenu;
                    double menuPrice = rs2.getDouble("menu.preis");
                    int orderDetailID = rs2.getInt("menu_auswahl.id_detail_auswahl");
                    int amount = rs2.getInt("menu_auswahl.anzahl");
                    try {
                        double sumExtraIngred = 0;
                        double sumDeleteIngred = 0;
                        Statement stmt3 = conn.createStatement();
                        String query3 = "SELECT SUM(zutaten.preis) " +
                                "FROM `zutaten_hinzuf` " +
                                "INNER JOIN zutaten ON zutaten_hinzuf.zutaten_id = zutaten.id " +
                                "WHERE zutaten_hinzuf.id_detail_auswahl = " + orderDetailID;
                        ResultSet rs3 = stmt3.executeQuery(query3);
                        while (rs3.next()) {
                            sumExtraIngred = rs3.getDouble("SUM(zutaten.preis)");
                        }
                        query3 = "SELECT SUM(zutaten.preis) " +
                                "FROM `zutaten_entfernen` " +
                                "INNER JOIN zutaten ON zutaten_entfernen.zutaten_id = zutaten.id " +
                                "WHERE zutaten_entfernen.id_detail_auswahl = " + orderDetailID;
                        rs3 = stmt3.executeQuery(query3);
                        while (rs3.next()) {
                            sumDeleteIngred = rs3.getDouble("SUM(zutaten.preis)");
                        }
                        priceCompleteMenu = amount * (menuPrice + sumExtraIngred - sumDeleteIngred);
                    } catch (SQLException ex) {
                        throw new Error("something went wrong with query OrderDetails", ex);
                    }
                    priceOrder = priceOrder + priceCompleteMenu;
                }
            } catch (SQLException ex) {
                throw new Error("something went wrong with query menuNo and orderDetailID", ex);
            }
        } catch (SQLException ex) {
            throw new Error("something went wrong with query the order number");
        }
        return priceOrder;
    }

    private static int soldTheMost(){
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
