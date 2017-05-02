package com.marknkamau.justjava.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.marknkamau.justjava.activities.about.AboutActivity;
import com.marknkamau.justjava.activities.login.LogInActivity;
import com.marknkamau.justjava.activities.profile.ProfileActivity;

public class MenuActions {
    public static void ActionLogIn(Context context) {
        context.startActivity(new Intent(context, LogInActivity.class));
    }

    public static void ActionLogOut(Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        PreferencesInteraction.clearPreferences(context);
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
    }

    public static void ActionProfile(Context context) {
        context.startActivity(new Intent(context, ProfileActivity.class));
    }

    public static void ActionAbout(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }
}
