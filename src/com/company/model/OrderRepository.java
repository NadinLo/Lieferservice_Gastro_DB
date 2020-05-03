package com.company.model;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements IRepository {

    private DBConnector dbConnector;
    private MealRepository mealRepository = new MealRepository();
    private IngredientRepository ingredientRepository = new IngredientRepository();

    public OrderRepository() {
        this.dbConnector = DBConnector.getInstance();
    }

    @Override
    public ArrayList<Order> findAll() {
        ArrayList<Order> orders = new ArrayList<>();
        ResultSet rs = dbConnector.fetchData("SELECT * FROM `bestellung` WHERE `abgeschlossen` = 1 OR `abgeschlossen` = 2");
        try {
            while (rs.next()){
                Order order = new Order();
                order.setOrderNo(rs.getInt("bestellnr"));
                order.setChosenMeals(orderDetails (order));
                orders.add(order);
            }
            return orders;
        } catch (SQLException ex){
            System.out.println("no orders found");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return null;
    }

    @Override
    public Order findOne(int id) {

                Order order = new Order();
                order.setOrderNo(id);
                order.setChosenMeals(orderDetails (order));

            return order;

    }

    @Override
    public boolean create(Object entity) {
        Order order = (Order) entity;
        if (dbConnector.insert("INSERT INTO `bestellung`(`abgeschlossen`) VALUES (" + order.getOrderStatus() + ")")) {
            //get orderNo as last AUTO_INCREMENT but since connection was closed LAST_INSERT_ID() is not possible
            //for now MAX(bestellnr) works but not perfect.
            ResultSet rs = dbConnector.fetchData("SELECT MAX(bestellnr) FROM bestellung");
            try {
                if (rs.next()) {
                    order.setOrderNo(rs.getInt("MAX(bestellnr)"));
                    return true;
                }
            } catch (SQLException ex) {
                System.out.println("couldn't get the Order No.");
                ex.printStackTrace();
            } finally {
                dbConnector.closeConnection();
            }
        }
        return false;
    }

    public boolean updateOrderDetails(Order order) {
        //List Chosen Meals (insert in 'menüauswahl': orderno, anzahl, menünummer - get 'detail_auswahl_nr')
        for (int i = 0; i < order.getChosenMeals().size(); i++) {
            if (dbConnector.insert("INSERT INTO `menu_auswahl`(`bestell_nr`, `anzahl`, `menu_nr`) " +
                    "VALUES (" + order.getOrderNo() + ", " + order.getChosenMeals().get(i).getAmount() +
                    ", " + order.getChosenMeals().get(i).getId() + ")")) {
                // get detail_auswahl_id
                ResultSet rs = dbConnector.fetchData("SELECT MAX(id_detail_auswahl) " +
                        "FROM menu_auswahl WHERE bestell_nr = " + order.getOrderNo());
                int orderDetailsID = 0;
                try {
                    if (rs.next()) {
                        orderDetailsID = rs.getInt("MAX(id_detail_auswahl)");
                        order.getChosenMeals().get(i).setOrderDetailsID(orderDetailsID);
                    }
                } catch (SQLException ex) {
                    System.out.println("couldn't get id for order details");
                    ex.printStackTrace();
                } finally {
                    dbConnector.closeConnection();
                }

                //List addIngredient: check if contains sth. before inserting in 'zutatenzufügen': id_detailauswahl, zutaten_id
                if (order.getChosenMeals().get(i).getAddIngredients().size() > 0) {
                    for (int j = 0; j < order.getChosenMeals().get(i).getAddIngredients().size(); j++) {
                        if (!dbConnector.insert("INSERT INTO `zutaten_hinzuf`(`id_detail_auswahl`, `zutaten_id`) " +
                                "VALUES (" + orderDetailsID + ", " +
                                order.getChosenMeals().get(i).getAddIngredients().get(j).getId() + ")")) {
                            return false;
                        }
                    }
                }
                //List deleteIngred: check if contains sth. before inserting in 'zutaten entfernen': id detailaswahl, zutaten_id)
                if (order.getChosenMeals().get(i).getTakeOffIngredients().size() > 0) {
                    for (int j = 0; j < order.getChosenMeals().get(i).getTakeOffIngredients().size(); j++) {
                        if (!dbConnector.insert("INSERT INTO `zutaten_hinzuf`(`id_detail_auswahl`, `zutaten_id`) " +
                                "VALUES (" + orderDetailsID + ", " +
                                order.getChosenMeals().get(i).getTakeOffIngredients().get(j).getId() + ")")) {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public void calculateDeliveryZone (Order order, int locationId){
        ResultSet rs = dbConnector.fetchData("SELECT `id`, `lieferpreis` FROM `lieferzone` " +
                "WHERE distance_min <= (SELECT belieferte_ortschaften.distance FROM belieferte_ortschaften WHERE id = " + locationId + ") " +
                "AND distance_max > (SELECT belieferte_ortschaften.distance FROM belieferte_ortschaften WHERE id = " + locationId + ") ");
        try {
            if (rs.next()){
                order.setDeliveryZone(rs.getInt("id"));
                order.setDeliveryFee(rs.getDouble("lieferpreis"));
            }
        } catch (SQLException ex){
            System.out.println("couldn't get the delivery data");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
    }

    public void updateOrderStatus (int id){
        dbConnector.update("UPDATE `bestellung` SET `abgeschlossen`= 1 WHERE `bestellnr` = " + id);
    }

    public boolean checkCurrentOrder (Order order) {
        if (order.getChosenMeals().size() > 0) {
            return true;
        } else {
            System.out.println("choose a meal first");
            return false;
        }
    }

    public void deleteOrder (Order order) {
        if (statusInProgress(order)){
            dbConnector.delete("DELETE FROM `bestellung` WHERE `bestellnr`= " + order.getOrderNo());
        }
    }

    public void deleteChosenMeals (Order order) {
        if (statusInProgress(order)){
            dbConnector.delete("DELETE FROM `menu_auswahl` WHERE `bestell_nr` = " + order.getOrderNo());
        }
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

    public ArrayList<Meal> orderDetails (Order order) {
        ArrayList<Meal> orderedMeals = new ArrayList<>();
        ResultSet rs = dbConnector.fetchData("SELECT * FROM `menu_auswahl` WHERE `bestell_nr` = " + order.getOrderNo());
        try {
            while (rs.next()){
                int orderDetailId = rs.getInt("id_detail_auswahl");
                int amount = rs.getInt("anzahl");
                int mealNo = rs.getInt("menu_nr");
                Meal meal = mealRepository.findOne(mealNo);
                meal.setAmount(amount);
                meal.setAddIngredients(getAddedIngredients(orderDetailId));
                meal.setTakeOffIngredients(getTookOffIngredients(orderDetailId));
                meal.setMenuPriceInTotal(meal.getMenuPrice()
                        + priceExtraIngredients(meal.getAddIngredients())
                        - priceExtraIngredients(meal.getTakeOffIngredients()));
                orderedMeals.add(meal);
                order.setPriceInTotal(order.getPriceInTotal() + meal.getMenuPriceInTotal());
            }
            return orderedMeals;
        } catch (SQLException ex) {
            System.out.println("couldn't get order Details");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return null;
    }

    private ArrayList <Ingredient> getAddedIngredients(int orderDetailId) {
        ResultSet rs = dbConnector.fetchData("SELECT * FROM `zutaten_hinzuf` WHERE `id_detail_auswahl` = " + orderDetailId);
        ArrayList<Ingredient> addedIngredients = new ArrayList<>();
        try {
            if (rs != null) {
                while (rs.next()) {
                    Ingredient ingredient = ingredientRepository.findOne(rs.getInt("zutaten_id"));
                    addedIngredients.add(ingredient);
                }
                return addedIngredients;
            }
            else {
                System.out.println("no added ingredients");
            }
        } catch (SQLException ex ){
            System.out.println("couldn't find added ingredients");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return null;
    }

    private ArrayList <Ingredient> getTookOffIngredients(int orderDetailId) {
        ResultSet rs = dbConnector.fetchData("SELECT * FROM `zutaten_entfernen` WHERE `id_detail_auswahl` = " + orderDetailId);
        ArrayList<Ingredient> tookOffIngredients = new ArrayList<>();
        try {
            if (rs != null) {
                while (rs.next()) {
                    Ingredient ingredient = ingredientRepository.findOne(rs.getInt("zutaten_id"));
                    tookOffIngredients.add(ingredient);
                }
                return tookOffIngredients;
            }
            else {
                System.out.println("no deleted ingredients");
            }
        } catch (SQLException ex ){
            System.out.println("couldn't find deleted ingredients");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return null;
    }

    private double priceExtraIngredients (ArrayList<Ingredient> ingredients) {
        double priceExtraIngredients = 0;
        if (ingredients.size() != 0){
            for (Ingredient ingredient : ingredients) {
                priceExtraIngredients = priceExtraIngredients + ingredient.getSinglePrice();
            }
        }
        return priceExtraIngredients;
    }

    public ArrayList<ArrayList<Integer>> soldTheMost () {
        ResultSet rs = dbConnector.fetchData("SELECT menu_auswahl.menu_nr, COUNT(menu_auswahl.menu_nr) " +
                "FROM `menu_auswahl` " +
                "GROUP BY menu_auswahl.menu_nr " +
                "ORDER BY COUNT(menu_auswahl.menu_nr) DESC");
        ArrayList<Integer> mealNo = new ArrayList<>();
        ArrayList<Integer> amounts = new ArrayList<>();
        try {
            while (rs.next()) {
                mealNo.add(rs.getInt("menu_auswahl.menu_nr"));
                amounts.add(rs.getInt("COUNT(menu_auswahl.menu_nr)"));
            }
            ArrayList <ArrayList <Integer> > soldTheMost = new ArrayList<>(2);
            soldTheMost.add(mealNo);
            soldTheMost.add(amounts);
            return soldTheMost;
        } catch (SQLException ex) {
            System.out.println("couldn't get data for sold the most");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return null;

    }


}
