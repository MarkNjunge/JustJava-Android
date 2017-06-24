package com.marknkamau.justjava.ui.main;

import android.content.SharedPreferences;

import com.marknkamau.justjava.models.DataProvider;
import com.marknkamau.justjava.utils.Constants;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;

class MainActivityPresenter {
    private MainActivityView activityView;
    private SharedPreferences sharedPreferences;

    MainActivityPresenter(MainActivityView activityView, SharedPreferences sharedPreferences) {
        this.activityView = activityView;
        this.sharedPreferences = sharedPreferences;
    }

    void getCatalogItems(){
        activityView.displayCatalog(DataProvider.drinksList);
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
