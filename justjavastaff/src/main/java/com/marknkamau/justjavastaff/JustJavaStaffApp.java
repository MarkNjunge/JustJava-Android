package com.marknkamau.justjavastaff;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class JustJavaStaffApp extends Application {

    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
