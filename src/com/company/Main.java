package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Zutaten zufügen
                // addIngredient(Name, vegetarisch (true/false), preis (double));
        //Zutaten aus liste löschen
                // deleteIngredient (int id)
        //Preis bearbeiten
                // updatePrice (int id, double price)
        //Zutatenliste anzeigen
                printIngredientList();
        //Menügruppen anzeigen
                printMenuTypes();
        //todo: Menü erstellen
        System.out.println("NEW MENU");
        boolean creationOK = false;
        ArrayList<Integer> ingredients = null;
        String name = null;
        int menuType = 0;
        // Menü-Eigenschaften wählen und Auswahl bestätigen
        while (!creationOK) {
            ingredients = new ArrayList<>(10);
            System.out.println("Enter the name for the new menu:");
            name = scanner.nextLine();
            System.out.println("Enter the id of the menu's type:");
            menuType = scanner.nextInt();
            int id = 0;
            System.out.println("Add one ingredient. Therefor enter the right id and press enter.\n" +
                    "You need to add at least 2 ingredients and maximum 10. If you want to finish press -1");
            for (int i = 0; i < 10; i++) {
                System.out.println("Currently your menu has " + i + " ingredients");
                id = scanner.nextInt();
                if(id != -1) {
                    ingredients.add(id);
                }
                else{
                    while (i < 10){
                        ingredients.add(null);
                        i++;
                    }
                }
            }
            System.out.print("Your creation:\n" +
                    "menu name: " + name + "\n" +
                    "menu type: " + menuType + "\n" +
                    "ingredients: ");
            for (int i = 0; i < ingredients.size() && ingredients.get(i) != null; i++) {
                System.out.print(getIngredient(ingredients.get(i)) + ", ");
            }
            System.out.println();
            System.out.println("Are you ok with this? enter Yes (Y)/ No (N)");
            String decision = scanner.next();
            if (decision.equalsIgnoreCase("Y")){
            creationOK = true;}
            else if (decision.equalsIgnoreCase("N")){
                System.out.println("no Problem. Enter everything again.");
            }
        }
        addNewMenu(name, menuType, ingredients);


        //todo: Liefergebiet verwalten
        //todo: Lieferzonen verwalten
    }

    private static String getIngredient(int id) {
        Connection conn = null;
        String name = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String query = "SELECT name FROM `zutaten` WHERE id = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                name = rs.getString("name");
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
        return name;
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
            System.out.print(
                    "ALL INGREDIENTS-------------------------\n" +
                    "id\t| name\t\t\t | is veggi | price\n" +
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
            System.out.println("----------------------------------------\n");

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

    private static void deleteIngredient (int id){
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String command = "DELETE FROM `zutaten` WHERE `id` = " + id;
            Statement stmt = conn.createStatement();
            int ok = stmt.executeUpdate(command);
            if(ok == 1){
                System.out.println("ingredient was successfully deleted");
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

    private static void updatePrice (int id, double price){
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String command = "UPDATE `zutaten` SET `preis`=" + price + " WHERE `id` = " + id;
            Statement stmt = conn.createStatement();
            int ok = stmt.executeUpdate(command);
            if(ok == 1){
                System.out.println("price was successfully updated");
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

    private static void printMenuTypes (){
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String query = "SELECT * FROM `menü_gruppe`";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.print("" +
                    "ALL MENU TYPES----------\n" +
                    "id\t| name\n" +
                    "------------------------\n");
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");

                System.out.println(id + "\t| " + name);
            }
            System.out.println("------------------------");

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

    private static void addNewMenu (String name, int menuType, ArrayList<Integer> ingredients){
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String command = "INSERT INTO `menü`" +
                    "(`name`, `menü_gruppe`, " +
                    "`zutat_1`, `zutat_2`, `zutat_3`, `zutat_4`, `zutat_5`, `zutat_6`, `zutat_7`, `zutat_8`, `zutat_9`, `zutat_10`) " +
                    "VALUES ('" + name + "', " + menuType + ", " +
                    ingredients.get(0) + ", " +
                    ingredients.get(1) + ", " +
                    ingredients.get(2) + ", " +
                    ingredients.get(3) + ", " +
                    ingredients.get(4) + ", " +
                    ingredients.get(5) + ", " +
                    ingredients.get(6) + ", " +
                    ingredients.get(7) + ", " +
                    ingredients.get(8) + ", " +
                    ingredients.get(9) + ")";
            Statement stmt = conn.createStatement();
            int ok = stmt.executeUpdate(command);
            if(ok == 1){
                System.out.println("menu was successfully added");
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
}
