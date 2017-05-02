package com.marknkamau.justjava.activities.profile;

import com.marknkamau.justjava.models.PreviousOrder;

import java.util.List;

interface ProfileActivityView {
    void showProgressBar();
    void hideProgressBar();
    void displayNoPreviousOrders();
    void displayPreviousOrders(List<PreviousOrder> orderList);
    void displayMessage(String message);
}
