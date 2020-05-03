package com.company.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
}
