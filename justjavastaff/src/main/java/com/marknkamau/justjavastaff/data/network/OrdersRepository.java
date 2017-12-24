package com.marknkamau.justjavastaff.data.network;

import com.marknkamau.justjavastaff.models.Order;

import java.util.List;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

public interface OrdersRepository {
    void getOrders(OrdersListener listener);

    interface BaseListener {
        void onError(String reason);
    }

    interface OrdersListener extends BaseListener {
        void onSuccess(List<Order> orders);
    }
}
