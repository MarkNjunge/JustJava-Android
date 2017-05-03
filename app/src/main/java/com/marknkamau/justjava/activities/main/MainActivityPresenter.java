package com.marknkamau.justjava.activities.main;

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
        FirebaseAuthUtils.logOut();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.DEF_NAME);
        editor.remove(Constants.DEF_PHONE);
        editor.remove(Constants.DEF_ADDRESS);
        editor.apply();
    }
}
