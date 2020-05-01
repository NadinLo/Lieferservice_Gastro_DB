package com.company.model;

import com.mysql.cj.jdbc.ha.NdbLoadBalanceExceptionChecker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MealRepository implements IRepository {

    private DBConnector dbConnector;
    private IngredientRepository ingredientRepository = new IngredientRepository();

    public MealRepository (){
        this.dbConnector = DBConnector.getInstance();
    }

    @Override
    public ArrayList <Meal> findAll() {
        ArrayList<Meal> meals = new ArrayList<>();
        ResultSet rs = dbConnector.fetchData("SELECT menu.menu_nr, menu.name, menu_gruppe.name, menu.preis " +
                "FROM `menu` " +
                "INNER JOIN menu_gruppe ON menu.menu_gruppe = menu_gruppe.id");
        try {
            while (rs.next()) {
                Meal meal = new Meal(rs.getInt("menu.menu_nr"), rs.getString("menu.name"),
                        rs.getString("menu_gruppe.name"), rs.getDouble("menu.preis"));

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ResultSet subRs = dbConnector.fetchData("SELECT zutaten.id, zutaten.name, zutaten.vegetarisch, zutaten.preis " +
                        "FROM `zutatenmix` " +
                        "INNER JOIN zutaten ON zutatenmix.zutaten_id = zutaten.id " +
                        "WHERE zutatenmix.menü_id = " + rs.getInt("menu.menu_nr"));
                try {
                    while (subRs.next()) {
                        ingredients.add(new Ingredient(subRs.getInt("zutaten.id"), subRs.getString("zutaten.name"),
                                subRs.getBoolean("zutaten.vegetarisch"), subRs.getDouble("zutaten.preis")));

                        if (!subRs.getBoolean("zutaten.vegetarisch")) {
                            meal.setVegetarian(false);
                        }
                        meal.setIngredients(ingredients);
                    }
                } catch (SQLException ex) {
                    System.out.println("couldn't find ingredients");
                    ex.printStackTrace();
                } // closeConnection?? does it break the current process?

                meals.add(meal);

            }
            return meals;
        } catch (SQLException ex) {
            System.out.println("couldn't get all meals");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return null;
    }

    @Override
    public Meal findOne(int id) {
        Meal meal = null;
        ResultSet rs = dbConnector.fetchData("SELECT menu.name, menu_gruppe.name, menu.preis " +
                "FROM `menu` " +
                "INNER JOIN menu_gruppe ON menu.menu_gruppe = menu_gruppe.id " +
                "WHERE menu_nr = " + id);
        try {
            while (rs.next()){
                meal = new Meal(id, rs.getString("menu.name"), rs.getString("menu_gruppe.name"), rs.getDouble("menu.preis"));

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ResultSet subRs = dbConnector.fetchData("SELECT zutaten.id, zutaten.name, zutaten.vegetarisch, zutaten.preis " +
                        "FROM `zutatenmix` " +
                        "INNER JOIN zutaten ON zutatenmix.zutaten_id = zutaten.id " +
                        "WHERE zutatenmix.menü_id = " + id);
                try {
                    while (subRs.next()){
                        ingredients.add(new Ingredient(subRs.getInt("zutaten.id"), subRs.getString("zutaten.name"),
                                subRs.getBoolean("zutaten.vegetarisch"), subRs.getDouble("zutaten.preis")));

                        if (!subRs.getBoolean("zutaten.vegetarisch")){
                            meal.setVegetarian(false);
                        }
                    }
                } catch (SQLException ex){
                    System.out.println("couldn't find ingredients");
                    ex.printStackTrace();
                } // closeConnection?? does it break the current process?
                meal.setIngredients(ingredients);
            }
            return meal;

        } catch (SQLException ex){
            System.out.println("couldn't get all meals");
            ex.printStackTrace();
        } finally {
            dbConnector.closeConnection();
        }
        return null;
    }

    @Override
    public boolean create(Object entity) {
        Meal meal = (Meal) entity;
        if(dbConnector.insert("INSERT INTO `menu`(`menu_nr`, `name`, `menu_gruppe`, `preis`) " +
                "VALUES (null, '" + meal.getName() + "', " +
                "(SELECT menu_gruppe.id FROM menu_gruppe WHERE menu_gruppe.name = '" + meal.getMenuType() + "'), " +
                meal.getMenuPrice() + ")")){
            ResultSet rs = dbConnector.fetchData("SELECT MAX(menu_nr) FROM menu");
            try {
                if (rs.next()){
                    meal.setId(rs.getInt("MAX(menu_nr)"));
                    return insertMealIngredients(meal);
                }
            } catch (SQLException ex){
                System.out.println("couldn't get created id of the menu");
                ex.printStackTrace();
            } finally {
                dbConnector.closeConnection();
            }
        }
        return false;
    }

    private boolean insertMealIngredients (Meal meal){
        for (int i = 0; i < meal.getIngredients().size(); i++) {
            if(!dbConnector.insert("INSERT INTO `zutatenmix`(`menü_id`, `zutaten_id`) VALUES (" + meal.getId() +
                    ", " + meal.getIngredients().get(i).getId() + ")")){
                return false;
            }
        }
        return true;
    }

    public ArrayList <String> getAllMealTypes () {
        ResultSet rs = dbConnector.fetchData("SELECT * FROM `menu_gruppe`");
        ArrayList <String> types = new ArrayList<>();
        try {
            while (rs.next()){
                types.add(rs.getString("name"));
            }
        } catch (SQLException ex){
            System.out.println("couldn't get type data");
            ex.printStackTrace();
        }
        return types;
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

    public void deleteOrderDetails (Order order) {
        if (statusInProgress(order)){
            for (int i = 0; i < order.getChosenMeals().size(); i++) {
                if(!dbConnector.delete("DELETE FROM `zutaten_hinzuf` WHERE `id_detail_auswahl` = " + order.getChosenMeals().get(i).getOrderDetailsID()) ||
                        !dbConnector.delete("DELETE FROM `zutaten_entfernen` WHERE `id_detail_auswahl` = " + order.getChosenMeals().get(i).getOrderDetailsID())){
                    return;
                }
            }
        }
    }

    public void updateMealIngredient (int mealId, int ingredientID){
        Meal meal = findOne(mealId);
        if (ingredientID < 0) {
            ingredientID = -ingredientID;
            for (int i = 0; i < meal.getIngredients().size(); i++) {
                if (meal.getIngredients().get(i).getId() == ingredientID) {
                    dbConnector.delete("DELETE FROM `zutatenmix` WHERE `menü_id` = " + mealId + "AND `zutaten_id` = " + ingredientID);
                    ingredientID = 0;
                }
            } if (ingredientID != 0){
                System.out.println("Since this ingredient is not part of the meal it is not possible to delete it.");
            }

        }
        if (ingredientID > 0) {
            for (int i = 0; i < meal.getIngredients().size(); i++) {
                if (meal.getIngredients().get(i).getId() == ingredientID) {
                    System.out.println("This ingredient is already part of the meal. It won't be added again");
                    return;
                }
            }
            dbConnector.insert("INSERT INTO `zutatenmix`(`menü_id`, `zutaten_id`) VALUES (" + mealId + ", " + ingredientID +")");
        }
    }
}
