package com.marknkamau.justjavastaff;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.marknkamau.justjavastaff.authentication.AuthServiceImpl;
import com.marknkamau.justjavastaff.authentication.AuthenticationService;

public class JustJavaStaffApp extends Application {
    private SharedPreferences preferences;
    private AuthenticationService auth;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        auth = new AuthServiceImpl();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public AuthenticationService getAuth() {
        return auth;
    }
}
