package com.marknkamau.justjava.activities.profile;

import com.marknkamau.justjava.models.PreviousOrder;

import java.util.List;
import java.util.Map;

interface ProfileActivityView {
    void displayUserDefaults(Map<String, String> defaults);
    void showProgressBar();
    void hideProgressBar();
    void displayNoPreviousOrders();
    void displayPreviousOrders(List<PreviousOrder> orderList);
    void displayMessage(String message);
}
