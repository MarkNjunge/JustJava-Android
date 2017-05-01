package com.marknkamau.justjava.activities.cart;

import com.marknkamau.justjava.models.CartItem;

import java.util.List;

interface CartActivityView {
    void displayCart(List<CartItem> cartItems);
    void displayEmptyCart();
    void displayCartTotal(int totalCost);
}
