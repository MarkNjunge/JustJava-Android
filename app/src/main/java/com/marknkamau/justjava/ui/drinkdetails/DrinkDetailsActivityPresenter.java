package com.marknkamau.justjava.ui.drinkdetails;

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
        FirebaseAuthUtils.INSTANCE.logOut();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.INSTANCE.getDEF_NAME());
        editor.remove(Constants.INSTANCE.getDEF_PHONE());
        editor.remove(Constants.INSTANCE.getDEF_ADDRESS());
        editor.apply();
    }
}
