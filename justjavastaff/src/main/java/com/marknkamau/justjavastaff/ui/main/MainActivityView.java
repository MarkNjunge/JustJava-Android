package com.marknkamau.justjavastaff.ui.main;

import com.marknkamau.justjavastaff.models.CustomerOrder;

import java.util.List;

public interface MainActivityView {
    void setOrderFlags(boolean pending, boolean inProgress, boolean completed, boolean delivered, boolean cancelled);
    void displayMessage(String message);
    void displayAvailableOrders(List<CustomerOrder> orders);
    void displayNoOrders();
}
