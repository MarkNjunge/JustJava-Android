package com.marknkamau.justjava.activities.drinkdetails;

import android.content.SharedPreferences;

import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.Constants;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.RealmUtils;

class DrinkDetailsActivityPresenter {
    private DrinkDetailsActivityView activityView;
    private SharedPreferences sharedPreferences;

    DrinkDetailsActivityPresenter(DrinkDetailsActivityView activityView, SharedPreferences sharedPreferences) {
        this.activityView = activityView;
        this.sharedPreferences = sharedPreferences;
    }

    void addToCart(CartItem cartItem){
        new RealmUtils().saveNewItem(cartItem);
        activityView.displayMessage("Item added to cart");
        activityView.finishActivity();
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
