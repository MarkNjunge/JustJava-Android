package com.marknkamau.justjavastaff.ui.main;

import android.content.SharedPreferences;

import com.marknkamau.justjavastaff.data.network.OrdersRepository;
import com.marknkamau.justjavastaff.models.Order;

import java.util.List;

class MainActivityPresenter {
    private final MainView view;
    private final SharedPreferences preferences;
    private OrdersRepository ordersRepository;

    MainActivityPresenter(MainView view, SharedPreferences preferences, OrdersRepository ordersRepository) {
        this.view = view;
        this.preferences = preferences;
        this.ordersRepository = ordersRepository;
    }

    void getOrders() {
        ordersRepository.getOrders(new OrdersRepository.OrdersListener() {
            @Override
            public void onSuccess(List<Order> orders) {
                if (orders.isEmpty()) {
                    view.displayNoOrders();
                } else {
                    view.displayAvailableOrders(orders);
                }
            }

            @Override
            public void onError(String reason) {
                view.displayMessage(reason);
            }
        });
    }
}
