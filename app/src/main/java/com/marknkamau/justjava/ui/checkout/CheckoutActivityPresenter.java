package com.marknkamau.justjava.ui.checkout;

import android.content.SharedPreferences;

import com.marknkamau.justjava.models.Order;
import com.marknkamau.justjava.ui.signup.SignUpActivity;
import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.Constants;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.FirebaseDBUtil;
import com.marknkamau.justjava.utils.RealmUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CheckoutActivityPresenter {
    private CheckoutActivityView activityView;
    private SharedPreferences sharedPreferences;

    CheckoutActivityPresenter(CheckoutActivityView activityView, SharedPreferences sharedPreferences) {
        this.activityView = activityView;
        this.sharedPreferences = sharedPreferences;
        updateLoggedInStatus();
    }

    void logOut(){
        FirebaseAuthUtils.INSTANCE.logOut();
        activityView.setDisplayToLoggedOut();
        activityView.setLoggedInStatus(false);
        activityView.invalidateMenu();
    }

    void updateLoggedInStatus(){
        if (FirebaseAuthUtils.INSTANCE.getCurrentUser() != null){
            Map<String, String> defaults = new HashMap<>();
            defaults.put(Constants.INSTANCE.getDEF_NAME(), sharedPreferences.getString(SignUpActivity.DEF_NAME, ""));
            defaults.put(Constants.INSTANCE.getDEF_PHONE(), sharedPreferences.getString(SignUpActivity.DEF_PHONE, ""));
            defaults.put(Constants.INSTANCE.getDEF_ADDRESS(), sharedPreferences.getString(SignUpActivity.DEF_ADDRESS, ""));
            activityView.setDisplayToLoggedIn(FirebaseAuthUtils.INSTANCE.getCurrentUser(), defaults);
            activityView.setLoggedInStatus(true);
        }else {
            activityView.setDisplayToLoggedOut();
            activityView.setLoggedInStatus(false);
        }
        activityView.invalidateMenu();
    }

    void placeOrder(Order order) {
        activityView.showUploadBar();
        final RealmUtils realmUtils = new RealmUtils();

        final List<CartItem> cartItems = realmUtils.getAllCartItems();
        int itemsCount = cartItems.size();
        int totalPrice = realmUtils.getTotalPrice();


        order.setItemsCount(itemsCount);
        order.setTotalPrice(totalPrice);

        FirebaseDBUtil.INSTANCE.placeNewOrder(order, realmUtils.getAllCartItems(), new FirebaseDBUtil.UploadListener() {
            @Override
            public void taskSuccessful() {
                activityView.hideUploadBar();
                activityView.showMessage("Order placed");
                activityView.finishActivity();
                realmUtils.deleteAllItems();
            }

            @Override
            public void taskFailed(String reason) {
                activityView.hideUploadBar();
                activityView.showMessage(reason);
            }
        });

    }
}
