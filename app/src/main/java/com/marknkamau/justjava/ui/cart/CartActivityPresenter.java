package com.marknkamau.justjava.ui.cart;

import android.content.SharedPreferences;

import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.Constants;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.RealmUtils;

import java.util.List;

import timber.log.Timber;

class CartActivityPresenter {
    private CartActivityView activityView;
    private SharedPreferences sharedPreferences;
    private RealmUtils realmUtils;

    CartActivityPresenter(CartActivityView activityView, SharedPreferences sharedPreferences) {
        this.activityView = activityView;
        this.sharedPreferences = sharedPreferences;
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

    void logUserOut(){
        FirebaseAuthUtils.logOut();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.DEF_NAME);
        editor.remove(Constants.DEF_PHONE);
        editor.remove(Constants.DEF_ADDRESS);
        editor.apply();
    }
}


