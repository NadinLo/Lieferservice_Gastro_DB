package com.company.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository {
    private DBConnector dbConnector;

    public UserRepository() {
        this.dbConnector = DBConnector.getInstance();
    }

    @Override
    public ArrayList <User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        ResultSet rs = dbConnector.fetchData("SELECT bestellnr, kunde.name ,kunde.straße_hnr, " +
                "belieferte_ortschaften.name, belieferte_ortschaften.plz " +
                "FROM kunde " +
                "INNER JOIN belieferte_ortschaften on kunde.ortschaft = belieferte_ortschaften.id " +
                "GROUP BY belieferte_ortschaften.name, kunde.straße_hnr, kunde.name");
        try {
            while (rs.next()){
                User user = new User(rs.getString("kunde.name"),rs.getString("kunde.straße_hnr"),
                        rs.getString("belieferte_ortschaften.plz"),rs.getString("belieferte_ortschaften.name"));
                user.setOrderNo(rs.getInt("bestellnr"));
                users.add(user);
            }
            return users;
        } catch (SQLException ex){
            System.out.println("couldn't find customer data");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return null;
    }

    @Override
    public Object findOne(int id) {
        return null;
    }

    @Override
    public boolean create (Object entity) {
        User user = (User) entity;
        return dbConnector.insert("INSERT INTO `kunde`(`bestellnr`, `name`, `straße_hnr`, `ortschaft`) " +
                "VALUES (" + user.getOrderNo() + ", '" + user.getName() + "', '" + user.getAddress() + "', " + user.getLocationId() + ")");
    }

    public boolean isLocationInsideDeliveryArea (User user){
        ResultSet rs = dbConnector.fetchData("SELECT * FROM `belieferte_ortschaften` " +
                "WHERE name LIKE '" + user.getLocation() + "'");
        try {
            if (rs.next()) {
                user.setLocationId(rs.getInt("id"));
                user.setMinutesToDeliver(rs.getTime("lieferzeit"));
                return true;
            } else {
                System.out.println("Sorry, we do not deliver to this location");
                return false;
            }
        } catch (SQLException ex){
            System.out.println("couldn't get data of location");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return false;
    }

    private boolean statusInProgress (Order order) {
        ResultSet rs = dbConnector.fetchData("SELECT `abgeschlossen` FROM `bestellung` WHERE `bestellnr` = " + order.getOrderNo());
        try {
            if (rs.next()){
                if (rs.getInt("abgeschlossen") == 0){
                    return true;
                }
            }
        } catch (SQLException ex){
            System.out.println("couldn't get status of order");
            ex.printStackTrace();
        }
        return false;
    }

    public void deleteCustomerData (Order order) {
        if (statusInProgress(order)){
            dbConnector.delete("DELETE FROM `kunde` WHERE `bestellnr` = " + order.getOrderNo());
        }
    }
}
