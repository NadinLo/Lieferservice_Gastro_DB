package com.company;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scannerForInt = new Scanner(System.in);

    private static Scanner scannerForString = new Scanner(System.in);

    private static DecimalFormat df = new DecimalFormat("##.##");

    public static void main(String[] args) {
        int decision = 0;
        while (decision == 0) {
            System.out.println("Hello chef! What is your aim to do?\n" +
                    "Edeting the menu -- press 1\n " +
                    "Analyze Data -- press 2\n " +
                    "Quit program -- press 3");
            decision = scannerForInt.nextInt();
            if (decision == 1) {
                decision = editingMenu();
            } else if (decision == 2) {
                decision = analyzeData();
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
            System.out.println("2) show all menu types");
            System.out.println("3) show complete list of menus");
            System.out.println("4) edit ingredients");
            System.out.println("5) edit menus");
            System.out.println("6) finish editing program");

            decision = scannerForInt.nextInt();
            if (decision == 1) {
                //show complete list of ingredients
                decision = printIngredientList();
            } else if (decision == 2){
                //show all menu types
                decision = printMenuTypes();
            } else if (decision == 3) {
                //show complete menu
                decision = printCompleteMenu();
            } else if (decision == 4) {
                //edit ingredients
                decision = editIngredients();
            } else if (decision == 5) {
                //edit menu
                decision = editMenus();
            } else if (decision == 6) {
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

            decision = scannerForInt.nextInt();
            if (decision == 1){
                //add ingredients
                System.out.println("Enter the name of the new ingredient");
                String name = scannerForString.nextLine();
                System.out.println("Is this ingredient vegetarian? Enter 'true' or 'false'");
                boolean vegetarian = scannerForInt.nextBoolean();
                System.out.println("Enter the price of the ingredient");
                double price = scannerForInt.nextDouble();
                decision = addIngredient(name, vegetarian, price);
            }
            else if (decision == 2){
                //delete ingredents
                System.out.println("Enter the ingredient number of the ingredient you want to take off the list");
                int ingredID = scannerForInt.nextInt();
                decision = deleteIngredient (ingredID);

            }
            else if (decision == 3){
                //edit price
                int ingredID;
                double price;
                System.out.println("Enter the ingredient number of the ingredient you want to update the price.");
                ingredID = scannerForInt.nextInt();
                System.out.println("Enter now the new wanted price");
                price = scannerForInt.nextDouble();
                decision = updatePriceIngred(ingredID, price);
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

            decision = scannerForInt.nextInt();
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
                    name = scannerForString.nextLine();
                    System.out.println("Enter the Type id of the menu's type:");
                    menuType = scannerForInt.nextInt();
                    System.out.println("Enter the price the menu should cost:");
                    price = scannerForInt.nextDouble();
                    int ingredientID;
                    System.out.println("Add one ingredient. Therefor enter the right ingredient number and press enter.\n" +
                            "You need at least 2 ingredients and maximum 10. If you want to finish press -1");
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Currently your menu has " + i + " ingredients");
                        ingredientID = scannerForInt.nextInt();
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
                    String yOrn = scannerForString.nextLine();
                    if (yOrn.equalsIgnoreCase("Y")){
                        creationOK = true;}
                    else if (yOrn.equalsIgnoreCase("N")){
                        System.out.println("no Problem. Enter everything again.");
                    }
                }
                decision = addNewMenu(name, menuType, price, ingredients);
            }

            else if (decision == 2){
                //update ingredients in menu
                //which menu/ delete or add ingredient
                System.out.println("Please enter the  number of the menu you want to edit.");
                int menu = scannerForInt.nextInt();
                printMenuIngreds(menu);
                System.out.println("Now enter one after another the number of the ingredient you want to add. If you want " +
                        "to delete single ingredients from the menu list just write \"-\" before the number." +
                        "Finish your entry with \"0\"");
                ArrayList <Integer> editIngreds = new ArrayList<>();
                while (!editIngreds.contains(0)) {
                    editIngreds.add(scannerForInt.nextInt());
                }
                decision = updateIngredInMenu(menu, editIngreds);
            }

            else if (decision == 3){
                //update price
                int menuID;
                double price;
                System.out.println("Enter the number of the menu you want to update the price.");
                menuID = scannerForInt.nextInt();
                System.out.println("Enter now the new wanted price");
                price = scannerForInt.nextDouble();
                decision = updatePriceMenu (menuID, price);
            }

            else if (decision == 4){
                //delete menu
                System.out.println("Which menu do you want to delete? Please enter the number.");
                int menuNo = scannerForInt.nextInt();
                decision = deleteMenu(menuNo);
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

    private static int updatePriceIngred(int id, double price){
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

    private static int printMenuTypes (){
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String query = "SELECT * FROM `menu_gruppe`";
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
        return 0;
    }

    private static int addNewMenu (String name, int menuType, double price, ArrayList<Integer> ingredients){
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String command = "INSERT INTO `menu`" +
                    "(`name`, `menu_gruppe`, preis)" +
                    "VALUES ('" + name + "', " + menuType + ", " + price + ")";
            Statement stmt = conn.createStatement();
            int ok = stmt.executeUpdate(command);
            int number = 0;
            String query = "SELECT `menu_nr` FROM `menu` WHERE `name` = '" + name + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                number = rs.getInt("menu_nr");
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
        return 0;
    }

    private static void printMenuIngreds (int menu){
        Connection conn;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String query = "SELECT zutatenmix.menü_id, menu.name AS 'Menü', zutatenmix.zutaten_id, zutaten.name AS 'Zutat', " +
                    "zutaten.preis, zutaten.vegetarisch FROM `zutatenmix` " +
                    "INNER JOIN zutaten ON zutaten.id = zutatenmix.zutaten_id " +
                    "INNER JOIN menu ON menu.menu_nr = zutatenmix.menü_id " +
                    "WHERE `menü_id` = " + menu;
            String menuName = null;
            ArrayList<Integer> ingredID = new ArrayList<>();
            ArrayList<String> ingredName = new ArrayList<>();
            ArrayList<Double> ingredPrice = new ArrayList<>();
            ArrayList<Boolean> vegetarian = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                menuName = rs.getString("Menü");
                ingredID.add(rs.getInt("zutaten_id"));
                ingredName.add(rs.getString("Zutat"));
                ingredPrice.add(rs.getDouble("preis"));
                vegetarian.add(rs.getBoolean("vegetarisch"));
            }
            System.out.println(menu + ") " + menuName);
            for (int i = 0; i < ingredID.size(); i++) {
                System.out.print(ingredID.get(i) + " - " + ingredName.get(i) + " [" + df.format(ingredPrice.get(i)) + "€]");
                if (vegetarian.get(i)){
                    System.out.println(" => vegetarian");
                } else {
                    System.out.println(" => non vegetarian");
                }
            }
        } catch (SQLException ex){
            throw new Error("Something went wrong with updating a menu's ingredients.", ex);
        }
    }

    private static int updateIngredInMenu (int menu, ArrayList<Integer> editIntreds){
        Connection conn;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String command;
            for (Integer editIntred : editIntreds) {
                if (editIntred < 0) {
                    command = "DELETE FROM `zutatenmix` " +
                            "WHERE `zutaten_id` = " + (-editIntred) + "AND menu_id = " + menu;
                    stmt.executeUpdate(command);
                } else if (editIntred > 0) {
                    command = "INSERT INTO `zutatenmix`(`menü_id`, `zutaten_id`) " +
                            "VALUES (" + menu + "," + editIntred + ")";
                    stmt.executeUpdate(command);
                } else {
                    System.out.println("menu is now updated");
                }
            }
        } catch (SQLException ex){
            throw new Error("Something went wrong with updating a menu's ingredients.", ex);
        }

        return 0;
    }

    private static int updatePriceMenu (int id, double price) {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String command = "UPDATE `menu` SET `preis`= " + price + " WHERE `menu_nr` = " + id;
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

    private  static int deleteMenu (int menuNo) {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/lieferservice_gastro?user=root";
            conn = DriverManager.getConnection(url);
            String command = //"DELETE FROM zutatenmix WHERE zutatenmix.menü_id = " + menuNo + "; " +
                    "DELETE FROM `menu` WHERE `menu_nr` = " + menuNo;
            Statement stmt = conn.createStatement();
            int ok = stmt.executeUpdate(command);
            if (ok == 1){
                System.out.println("The menu was successfully deleted");
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
                //todo: decision = [Methode]
            } else if (decision == 5) {
                //What was soled the most and how often?
                //todo: decision = [Methode]
            } else if (decision == 6) {
                //Order of the sold menus - the most successful is named first
                //todo: decision = [Methode]
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
        Connection conn = null;
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
        Connection conn = null;
        int amountOrdersPerCustomer = 0;
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

    private static int ordersPerLocation (){
        Connection conn = null;
        int amountOrdersPerLocation = 0;;;
        String location;
        //todo: implement category guest customer and registered customer
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


}
