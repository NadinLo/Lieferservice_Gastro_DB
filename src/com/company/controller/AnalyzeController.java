package com.company.controller;

import com.company.model.Order;
import com.company.model.OrderRepository;
import com.company.model.User;
import com.company.model.UserRepository;
import com.company.view.AnalyzeView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AnalyzeController {
    AnalyzeView analyzeView = new AnalyzeView();
    OrderRepository orderRepository = new OrderRepository();
    UserRepository userRepository = new UserRepository();


    public void start () {
        int decision = 0;
        while (decision != 7) {

            decision = analyzeView.analyzeMenu();
            if (decision == 1) {
                //how many orders were taken place till now?
                analyzeView.countAllOrders(orderRepository.findAll());
            } else if (decision == 2) {
                //how many orders were taken place for each customer?
                ArrayList<User> users = userRepository.findAll();
                ArrayList<Order> orders = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    if (users.size() > 1 && 9 != users.size() -1 ) {
                        if (i > 0) {
                            if (!(users.get(i).getLocation().equalsIgnoreCase(users.get(i - 1).getLocation()) &&
                            users.get(i).getAddress().equalsIgnoreCase(users.get(i-1).getAddress()) &&
                                    users.get(i).getName().equalsIgnoreCase(users.get(i-1).getName()))) {
                                analyzeView.countOrdersPerCustomer (users.get(i - 1), orders);
                                orders.clear();
                            }
                        }
                        orders.add(orderRepository.findOne(users.get(i).getOrderNo()));
                    } else {
                        orders.add(orderRepository.findOne(users.get(i).getOrderNo()));
                        analyzeView.countOrdersPerCustomer(users.get(i), orders);
                    }
                }

            } else if (decision == 3) {
                //how many orders were taken place for each location?
                ArrayList<User> users = userRepository.findAll();
                ArrayList<Order> orders = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    if (users.size() > 1 && 9 != users.size() -1 ) {
                        if (i > 0) {
                            if (!users.get(i).getLocation().equalsIgnoreCase(users.get(i - 1).getLocation())) {
                                analyzeView.countPerLocation (users.get(i - 1), orders);
                                orders.clear();
                            }
                        }
                        orders.add(orderRepository.findOne(users.get(i).getOrderNo()));
                    } else {
                        orders.add(orderRepository.findOne(users.get(i).getOrderNo()));
                        analyzeView.countPerLocation(users.get(i), orders);
                    }
                }
            } else if (decision == 4) {
                //Sales in total/ per customer/ per location => to next menu
                analyzeSale(0);
            } else if (decision == 5) {
                //todo:
                //What was soled the most and how often?
            } else if (decision == 6) {
                //todo:
                //Order of the sold menus - the most successful is named first
            } else if (decision == 7) {
                System.out.println("ok - just finished this editing program");
            } else {
                System.out.println("This input wasn't correct.");
            }
        }
    }

    private void analyzeSale (int a) {
        while (a != 4) {
            a = analyzeView.analyzeSaleMenu();

            if (a == 1) {
                //in total
                analyzeView.salesInTotal(orderRepository.findAll());
            } else if (a == 2) {
                //per customer
                ArrayList<User> users = userRepository.findAll();
                ArrayList<Order> orders = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    if (users.size() > 1 && 9 != users.size() -1 ) {
                        if (i > 0) {
                            if (!(users.get(i).getLocation().equalsIgnoreCase(users.get(i - 1).getLocation()) &&
                                    users.get(i).getAddress().equalsIgnoreCase(users.get(i-1).getAddress()) &&
                                    users.get(i).getName().equalsIgnoreCase(users.get(i-1).getName()))) {
                                analyzeView.salesPerCustomer (users.get(i - 1), orders);
                                orders.clear();
                            }
                        }
                        orders.add(orderRepository.findOne(users.get(i).getOrderNo()));
                    } else {
                        orders.add(orderRepository.findOne(users.get(i).getOrderNo()));
                        analyzeView.salesPerCustomer(users.get(i), orders);
                    }
                }

            } else if (a == 3) {
                //todo: per location
                ArrayList<User> users = userRepository.findAll();
                ArrayList<Order> orders = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
                    if (users.size() > 1 && 9 != users.size() -1 ) {
                        if (i > 0) {
                            if (!users.get(i).getLocation().equalsIgnoreCase(users.get(i - 1).getLocation())) {
                                analyzeView.salesPerLocation (users.get(i - 1), orders);
                                orders.clear();
                            }
                        }
                        orders.add(orderRepository.findOne(users.get(i).getOrderNo()));
                    } else {
                        orders.add(orderRepository.findOne(users.get(i).getOrderNo()));
                        analyzeView.salesPerLocation (users.get(i), orders);
                    }
                }
            } else if (a == 4) {
                System.out.println("ok - just finished this editing program");
            } else {
                System.out.println("This input wasn't correct.");
            }
        }
    }

}
