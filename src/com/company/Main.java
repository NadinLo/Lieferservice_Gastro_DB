package com.company;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        //Zutaten zufügen
        //todo: Zutatenliste anzeigen
        printIngredientList();

        //todo: Menü erstellen
        //todo: Menügruppen verwalten
        //todo: Liefergebiet verwalten
        //todo: Lieferzonen verwalten
    }
    private static void addIngredient (String name, boolean vegetarian, double price) {

        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String command = "INSERT INTO `zutaten`(`name`, `vegetarisch`, `preis`) VALUES " +
                    "('" + name + "'," + vegetarian + "," + price +")";
            Statement stmt = conn.createStatement();
            int ok = stmt.executeUpdate(command);
            if(ok == 1){
                System.out.println("ingredient was successfully added");
            } else {
                System.out.println("something went wrong");
            }

        } catch (SQLException ex) {
            throw new Error("Problem", ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void printIngredientList () {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String query = "SELECT * FROM `zutaten`";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.print("id\t| name\t\t\t | is veggi | price\n" +
                    "----------------------------------------\n");
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                name = name.concat("               ");
                name = name.substring(0,15);
                boolean vegetarian = rs.getBoolean("vegetarisch");
                double price = rs.getDouble("preis");
                System.out.println(id + "\t| " + name + "| " + vegetarian + "  \t| " + price);
            }

        } catch (SQLException ex) {
            throw new Error("Problem", ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
