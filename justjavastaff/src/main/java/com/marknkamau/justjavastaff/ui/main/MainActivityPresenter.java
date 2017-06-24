package com.marknkamau.justjavastaff.ui.main;

import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marknkamau.justjavastaff.models.CustomerOrder;
import com.marknkamau.justjavastaff.util.FirebaseUtil;


import java.util.List;

class MainActivityPresenter {
    private final MainActivityView activityView;
    private final SharedPreferences sharedPreferences;

    MainActivityPresenter(MainActivityView activityView, SharedPreferences sharedPreferences) {
        this.activityView = activityView;
        this.sharedPreferences = sharedPreferences;
    }

    void getOrderFlags() {
        activityView.setOrderFlags(
                sharedPreferences.getBoolean("preference_pending_orders", true),
                sharedPreferences.getBoolean("preference_in_progress_orders", false),
                sharedPreferences.getBoolean("preference_completed_orders", false),
                sharedPreferences.getBoolean("preference_delivered_orders", false),
                sharedPreferences.getBoolean("preference_cancelled_orders", false)
        );
    }

    void getOrders(){
        FirebaseUtil.getAllOrders(new FirebaseUtil.DBAllOrdersListener() {
            @Override
            public void ordersObtained(List<CustomerOrder> orders) {
                activityView.displayAvailableOrders(orders);
            }

            @Override
            public void errorOccurred(String response) {

            }

            @Override
            public void eventListenerObtained(DatabaseReference databaseReference, ValueEventListener eventListener) {

            }
        });
    }
}
