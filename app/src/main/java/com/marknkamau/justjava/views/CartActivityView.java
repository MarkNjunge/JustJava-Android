package com.marknkamau.justjava.views;

import com.marknkamau.justjava.models.CartItem;

import java.util.List;

public interface CartActivityView {
    void displayCart(List<CartItem> cartItems, int totalCost);
    void displayEmptyCart();
}
