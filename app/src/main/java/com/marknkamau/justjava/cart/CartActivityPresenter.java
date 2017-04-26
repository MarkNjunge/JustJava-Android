package com.marknkamau.justjava.cart;

import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.RealmUtils;

import java.util.List;

import timber.log.Timber;

class CartActivityPresenter {
    private CartActivityView activityView;
    private RealmUtils realmUtils;

    CartActivityPresenter(CartActivityView activityView) {
        this.activityView = activityView;
        realmUtils = new RealmUtils();

    }

    void loadItems() {
        Timber.i("Loading items...");
        List<CartItem> cartItems = realmUtils.getAllCartItems();
        int totalCost = realmUtils.getTotalCost();
        activityView.displayCartTotal(totalCost);
        if (cartItems.isEmpty()) {
            activityView.displayEmptyCart();
        } else {
            activityView.displayCart(cartItems);
        }
    }

    void clearCart() {
        realmUtils.deleteAllItems();
        loadItems();
    }
}


