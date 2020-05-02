package com.company.controller;

import com.company.model.OrderRepository;
import com.company.model.UserRepository;
import com.company.view.AnalyzeView;

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
                analyzeView.countOrdersPerCustomer(userRepository.findAll());

            } else if (decision == 3) {
                //how many orders were taken place for each location?
                userRepository.findAll();
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
                //todo: in total
            } else if (a == 2) {
                //todo: per customer
            } else if (a == 3) {
                //todo: per location
            } else if (a == 4) {
                System.out.println("ok - just finished this editing program");
            } else {
                System.out.println("This input wasn't correct.");
            }
        }
    }

}
