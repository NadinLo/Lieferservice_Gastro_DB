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
        //Menü erstellen
        //todo: Zutatenmix
        System.out.println("NEW MENU");
        ArrayList<Integer> ingredients = null;
        boolean creationOK = false;
        String name = null;
        int menuType = 0;
        double price = 0;
        // Menü-Eigenschaften wählen und Auswahl bestätigen
        while (!creationOK) {
            ingredients = new ArrayList<>();
            System.out.println("Enter the name for the new menu:");
            name = scanner.nextLine();
            System.out.println("Enter the id of the menu's type:");
            menuType = scanner.nextInt();
            System.out.println("Enter the price the menu should cost:");
            price = scanner.nextDouble();
            int id = 0;
            System.out.println("Add one ingredient. Therefor enter the right id and press enter.\n" +
                    "You need to add at least 2 ingredients and maximum 10. If you want to finish press -1");
            for (int i = 0; i < 10; i++) {
                System.out.println("Currently your menu has " + i + " ingredients");
                id = scanner.nextInt();
                if(id != -1) {
                    ingredients.add(id);
                } else { break;}
            }
            System.out.print("Your creation:\n" +
                    "menu name: " + name + "\n" +
                    "menu type: " + menuType + "\n" +
                    "price: " + price + "\n" +
                    "ingredients: ");
            for (int i = 0; i < ingredients.size(); i++) {
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
        addNewMenu(name, menuType, price, ingredients);


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

    private static void addNewMenu (String name, int menuType, double price, ArrayList<Integer> ingredients){
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String command = "INSERT INTO `menü`" +
                    "(`name`, `menü_gruppe`, preis" +
                    "VALUES ('" + name + "', " + menuType + ", " + price + ")";
            Statement stmt = conn.createStatement();
            int ok = stmt.executeUpdate(command);
            int number = 0;
            String query = "SELECT `menü_nr.` FROM `menü` WHERE `name` = '" + name + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                number = rs.getInt("menü_nr.");
            }
            int ok2 = 0;
            for (int i = 0; i < ingredients.size(); i++) {
                command = "INSERT INTO `zutatenmix`(`menü_id`, `zutaten_id`) " +
                        "VALUES (" + number + "," + ingredients.get(i);
                ok2 = stmt.executeUpdate(command);
            }
            if(ok == 1 && ok2 == 1){
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
