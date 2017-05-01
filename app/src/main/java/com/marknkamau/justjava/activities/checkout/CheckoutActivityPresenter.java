package com.marknkamau.justjava.activities.checkout;

import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.FirebaseDBUtil;
import com.marknkamau.justjava.utils.RealmUtils;

import java.util.List;
import java.util.Map;

class CheckoutActivityPresenter {
    private CheckoutActivityView activityView;

    CheckoutActivityPresenter(CheckoutActivityView activityView) {
        this.activityView = activityView;
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
            activityView.setDisplayToLoggedIn(FirebaseAuthUtils.getCurrentUser());
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
