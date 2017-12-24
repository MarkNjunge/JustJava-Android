package com.marknkamau.justjavastaff.ui.main;

import com.marknkamau.justjavastaff.models.Order;

import java.util.List;

public interface MainView {
    void displayMessage(String message);
    void displayAvailableOrders(List<Order> orders);
    void displayNoOrders();
}
