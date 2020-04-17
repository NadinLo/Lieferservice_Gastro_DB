package com.company;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    private static DecimalFormat df = new DecimalFormat("##.##");

    public static void main(String[] args) {
        int decision = 0;
        while (decision == 0) {
            System.out.println("Hello chef! What is your aim to do?\n" +
                    "Edeting the menu -- press 1\n " +
                    "Analyze Data -- press 2\n " +
                    "Quit program -- press 3");
            decision = scanner.nextInt();
            if (decision == 1) {
                decision = editingMenu();
            } else if (decision == 2) {
                //todo: analyzeData();
            } else if (decision == 3) {
                System.exit(0);
            } else {
                System.out.println("This input wasn't correct.");
                decision = 0;
            }
        }

    }

    private static int editingMenu(){
        int decision = 0;
        while (decision == 0) {
            System.out.println("You have now following possibilities:");
            System.out.println("1) show complete list of ingredients");
            System.out.println("2) show complete list of menus");
            System.out.println("3) edit ingredients");
            System.out.println("4) edit menus");
            System.out.println("5) finish editing program");

            decision = scanner.nextInt();
            if (decision == 1) {
                //show complete list of ingredients
                decision = printIngredientList();
            } else if (decision == 2) {
                //show complete menu
                decision = printCompleteMenu();
            } else if (decision == 3) {
                //edit ingredients
                decision = editIngredients();
            } else if (decision == 4) {
                //edit menu
                decision = editMenus();
            } else if (decision == 5) {
                System.out.println("ok - just finished this editing program");
            } else {
                System.out.println("This input wasn't correct.");
                decision = 0;
            }
        }
        return 0;

        //todo: Liefergebiet verwalten
        //todo: Lieferzonen verwalten
    }

    private static int editIngredients (){

        int decision = 0;
        while (decision == 0) {
            System.out.println("You have now following possibilities:");
            System.out.println("1) add ingredients to the list");
            System.out.println("2) delete ingredients from list");
            System.out.println("3) edit price");
            System.out.println("4) finish editing program");

            decision = scanner.nextInt();
            if (decision == 1){
                //add ingredients
                System.out.println("Enter the name of the new ingredient");
                String name = scanner.nextLine();
                System.out.println("Is this ingredient vegetarian? Enter 'true' or 'false'");
                boolean vegetarian = scanner.nextBoolean();
                System.out.println("Enter the price of the ingredient");
                double price = scanner.nextDouble();
                decision = addIngredient(name, vegetarian, price);
            }
            else if (decision == 2){
                //delete ingredents
                System.out.println("Enter the ingredient number of the ingredient you want to take off the list");
                int ingredID = scanner.nextInt();
                decision = deleteIngredient (ingredID);

            }
            else if (decision == 3){
                //edit price
                int ingredID;
                double price;
                System.out.println("Enter the ingredient number of the ingredient you want to update the price.");
                ingredID = scanner.nextInt();
                System.out.println("Enter now the new wanted price");
                price = scanner.nextDouble();
                decision = updatePrice(ingredID, price);
            }
            else if (decision == 4){
                System.out.println("ok - just finished the editing program");
            }
            else {
                System.out.println("This input wasn't correct. Try again.");
                decision = 0;
            }
        }
        return 0;
    }

    private static int editMenus (){
        int decision = 0;
        while (decision == 0) {
            System.out.println("You have now following possibilities:");
            System.out.println("1) add menu");
            System.out.println("2) update ingredients in a specific menu");
            System.out.println("3) update price");
            System.out.println("4) delete menu");
            System.out.println("5) finish editing program");

            decision = scanner.nextInt();
            if (decision == 1){
                //add menu
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
                    System.out.println("Enter the ingredientID of the menu's type:");
                    menuType = scanner.nextInt();
                    System.out.println("Enter the price the menu should cost:");
                    price = scanner.nextDouble();
                    int ingredientID = 0;
                    System.out.println("Add one ingredient. Therefor enter the right ingredient number and press enter.\n" +
                            "You need at least 2 ingredients and maximum 10. If you want to finish press -1");
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Currently your menu has " + i + " ingredients");
                        ingredientID = scanner.nextInt();
                        if(ingredientID != -1) {
                            ingredients.add(ingredientID);
                        } else { break;}
                    }
                    System.out.print("Your creation:\n" +
                            "menu name: " + name + "\n" +
                            "menu type: " + menuType + "\n" +
                            "price: " + df.format(price) + "\n" +
                            "ingredients: ");
                    for (Integer ingredient : ingredients) {
                        System.out.print(getIngredient(ingredient) + ", ");
                    }
                    System.out.println();
                    System.out.println("Are you ok with this? enter Yes (Y)/ No (N)");
                    String yOrn = scanner.next();
                    if (yOrn.equalsIgnoreCase("Y")){
                        creationOK = true;}
                    else if (yOrn.equalsIgnoreCase("N")){
                        System.out.println("no Problem. Enter everything again.");
                    }
                }
                addNewMenu(name, menuType, price, ingredients);
                decision = 0;
            }

            else if (decision == 2){
                //update ingredients in menu
                //todo:
            }

            else if (decision == 3){
                //update price
                //todo: unten stehenden Code auf Menü abändern
                int ingredID;
                double price;
                System.out.println("Enter the ingredient number of the ingredient you want to update the price.");
                ingredID = scanner.nextInt();
                System.out.println("Enter now the new wanted price");
                price = scanner.nextDouble();
                decision = updatePrice(ingredID, price);
            }

            else if (decision == 4){
                //delete menu
                //todo:
            }

            else if (decision == 5){
                System.out.println("Ok - just finished the editing program");
            }

            else {
                System.out.println("This input wasn't correct. Try again.");
                decision = 0;
            }
        }
        return 0;
    }

    private static int printIngredientList () {
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
        return 0;
    }

    private static int printCompleteMenu (){
        ArrayList<String> menyTypes = new ArrayList<>();
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            //getMenuType
            try {
                String query = "SELECT * FROM `menu_gruppe`";
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    menyTypes.add(rs.getString("name"));
                }
            } catch (SQLException ex){
                throw new Error("something went wrong with getMenuType", ex);
            }
            //getMenus
            System.out.println("___________________________________________");
            try {
                for (String menyType : menyTypes) {
                    System.out.println("=> " + menyType + ": ");
                    String query = "SELECT * " +
                            "FROM menu " +
                            "INNER JOIN menu_gruppe ON menu.menu_gruppe = menu_gruppe.id " +
                            "WHERE menu_gruppe.name = '" + menyType + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        int menuNo = rs.getInt("menu_nr");
                        String menuName = rs.getString("name");
                        double menuPrice = rs.getDouble("preis");

                        System.out.println("\t" + menuNo + ")\t" + menuName + "\t[" + df.format(menuPrice) + "€]");
                        System.out.print("\t\twith: ");
                        //getIngredientsOfMenu
                        ArrayList<Boolean> isIngredVeggi = new ArrayList<>();
                        try {
                            Statement subStmt = conn.createStatement();
                            String subQuery = "SELECT zutaten.name, zutaten.vegetarisch " +
                                    "FROM zutatenmix " +
                                    "INNER JOIN zutaten ON zutatenmix.zutaten_id = zutaten.id " +
                                    "WHERE zutatenmix.menü_id = " + menuNo;
                            ResultSet subRs = subStmt.executeQuery(subQuery);
                            while (subRs.next()) {
                                String ingredName = subRs.getString("name");
                                isIngredVeggi.add(subRs.getBoolean("vegetarisch"));
                                System.out.print(ingredName + ", ");
                            }
                            if (!isIngredVeggi.contains(false)) {
                                System.out.println("\n\t\t(vegetarian)");
                            } else {
                                System.out.println();
                            }
                        } catch (SQLException ex) {
                            throw new Error("something went wrong with getIngredientsOfMenu", ex);
                        }
                    }
                }
                System.out.println("___________________________________________");
            } catch (SQLException ex){
                throw new Error("something went wrong with getMenus", ex);
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
        return 0;
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

    private static int addIngredient (String name, boolean vegetarian, double price) {

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
        return 0;
    }

    private static int deleteIngredient (int id){
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
        return 0;
    }

    private static int updatePrice (int id, double price){
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
        return 0;
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
                    "(`name`, `menü_gruppe`, preis)" +
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
            for (Integer ingredient : ingredients) {
                command = "INSERT INTO `zutatenmix`(`menü_id`, `zutaten_id`) " +
                        "VALUES (" + number + "," + ingredient + ")";
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
