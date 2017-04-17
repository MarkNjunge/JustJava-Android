package com.marknkamau.justjava.presenters;

import android.content.Context;

import com.marknkamau.justjava.views.CartActivityView;
import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.RealmUtils;

import java.util.List;

public class CartActivityPresenter {
    private CartActivityView activityView;
    private RealmUtils realmUtils;

    public CartActivityPresenter(CartActivityView activityView, Context context) {
        this.activityView = activityView;
        realmUtils = new RealmUtils(context);
    }

    public void loadCart() {
        List<CartItem> cartItems = realmUtils.getAllCartItems();
        int totalCost = realmUtils.getTotalCost();
        if (cartItems.isEmpty()) {
            activityView.displayEmptyCart();
        } else {
            activityView.displayCart(cartItems, totalCost);
        }

    }

    public void clearCart(RealmUtils.RealmActionCompleted realmActionCompleted) {
        realmUtils.deleteAllItems(realmActionCompleted);
    }
}
