package com.marknkamau.justjava.activities.checkout;

import android.content.SharedPreferences;

import com.marknkamau.justjava.activities.signup.SignUpActivity;
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
        FirebaseAuthUtils.logOut();
        activityView.setDisplayToLoggedOut();
        activityView.setLoggedInStatus(false);
        activityView.invalidateMenu();
    }

    void updateLoggedInStatus(){
        if (FirebaseAuthUtils.getCurrentUser() != null){
            Map<String, String> defaults = new HashMap<>();
            defaults.put(Constants.DEF_NAME, sharedPreferences.getString(SignUpActivity.DEF_NAME, ""));
            defaults.put(Constants.DEF_PHONE, sharedPreferences.getString(SignUpActivity.DEF_PHONE, ""));
            defaults.put(Constants.DEF_ADDRESS, sharedPreferences.getString(SignUpActivity.DEF_ADDRESS, ""));
            activityView.setDisplayToLoggedIn(FirebaseAuthUtils.getCurrentUser(), defaults);
            activityView.setLoggedInStatus(true);
        }else {
            activityView.setDisplayToLoggedOut();
            activityView.setLoggedInStatus(false);
        }
        activityView.invalidateMenu();
    }

    void placeOrder(Map<String, Object> orderDetails) {
        activityView.showUploadBar();
        final RealmUtils realmUtils = new RealmUtils();

        final List<CartItem> cartItems = realmUtils.getAllCartItems();
        int itemsCount = cartItems.size();
        int totalCost = realmUtils.getTotalCost();

        orderDetails.put(FirebaseDBUtil.ITEMS_COUNT, itemsCount);
        orderDetails.put(FirebaseDBUtil.TOTAL_PRICE, totalCost);

        FirebaseDBUtil.placeNewOrder(orderDetails, realmUtils.getAllCartItems(), new FirebaseDBUtil.PlaceOrderListener() {
            @Override
            public void orderSuccessful() {
                activityView.hideUploadBar();
                activityView.showMessage("Order placed");
                activityView.finishActivity();
                realmUtils.deleteAllItems();
            }

            @Override
            public void orderFailed(String response) {
                activityView.hideUploadBar();
                activityView.showMessage(response);
            }
        });

    }
}
