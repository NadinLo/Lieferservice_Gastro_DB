package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        //todo: Menü erstellen
        //todo: Zutaten zufügen
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
}
