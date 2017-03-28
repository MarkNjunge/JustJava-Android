package com.marknkamau.justjava.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.marknkamau.justjava.SignUpActivity;

import java.util.HashMap;
import java.util.Map;

public class PreferencesInteraction {
    public static final String DEF_NAME = "defaultName";
    public static final String DEF_PHONE = "defaultPhoneNumber";
    public static final String DEF_ADDRESS = "defaultDeliveryAddress";

    public static void setDefaults(Context context, String name, String phone, String address) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEF_NAME, name);
        editor.putString(DEF_PHONE, phone);
        editor.putString(DEF_ADDRESS, address);

        editor.apply();
    }

    public static Map<String, String> getDefaults(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, String> defaults = new HashMap<>();
        defaults.put(DEF_NAME, preferences.getString(SignUpActivity.DEF_NAME, ""));
        defaults.put(DEF_PHONE, preferences.getString(SignUpActivity.DEF_PHONE, ""));
        defaults.put(DEF_ADDRESS, preferences.getString(SignUpActivity.DEF_ADDRESS, ""));
        return defaults;
    }

    public static void clearPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(DEF_NAME);
        editor.remove(DEF_PHONE);
        editor.remove(DEF_ADDRESS);
        editor.apply();
    }
}
