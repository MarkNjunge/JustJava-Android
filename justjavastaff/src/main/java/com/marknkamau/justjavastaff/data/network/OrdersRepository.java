package com.marknkamau.justjavastaff.data.network;

import com.marknkamau.justjavastaff.models.Order;
import com.marknkamau.justjavastaff.models.OrderItem;
import com.marknkamau.justjavastaff.models.OrderStatus;

import java.util.List;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

public interface OrdersRepository {
    void getOrders(OrdersListener listener);

    void getOrderItems(String orderId, OrderItemsListener listener);

    void updateOderStatus(String orderId, OrderStatus status, BasicListener listener);

    interface BaseListener {
        void onError(String reason);
    }

    interface BasicListener extends BaseListener {
        void onSuccess();
    }

    interface OrdersListener extends BaseListener {
        void onSuccess(List<Order> orders);
    }

    interface OrderItemsListener extends BaseListener {
        void onSuccess(List<OrderItem> items);
    }
}
