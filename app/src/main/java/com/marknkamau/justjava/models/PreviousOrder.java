package com.marknkamau.justjava.models;

public class PreviousOrder {
    private String deliveryAddress;
    private String timestamp;
    private String totalPrice;
    private String orderStatus;

        public PreviousOrder(String deliveryAddress, String timestamp, String totalPrice, String orderStatus) {
        this.deliveryAddress = deliveryAddress;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
