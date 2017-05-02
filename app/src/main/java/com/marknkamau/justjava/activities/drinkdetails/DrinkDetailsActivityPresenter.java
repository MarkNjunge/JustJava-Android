package com.marknkamau.justjava.activities.drinkdetails;

import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.RealmUtils;

class DrinkDetailsActivityPresenter {
    private DrinkDetailsActivityView activityView;

    DrinkDetailsActivityPresenter(DrinkDetailsActivityView activityView) {
        this.activityView = activityView;
    }

    void addToCart(CartItem cartItem){
        new RealmUtils().saveNewItem(cartItem);
        activityView.displayMessage("Item added to cart");
        activityView.finishActivity();
    }
}
