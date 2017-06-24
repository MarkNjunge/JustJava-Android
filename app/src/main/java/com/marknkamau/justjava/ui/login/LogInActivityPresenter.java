package com.marknkamau.justjava.ui.login;

import android.content.SharedPreferences;
import android.os.Handler;

import com.marknkamau.justjava.utils.Constants;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.FirebaseDBUtil;

class LogInActivityPresenter {
    private LogInActivityView activityView;
    private SharedPreferences sharedPreferences;

    LogInActivityPresenter(LogInActivityView activityView, SharedPreferences sharedPreferences) {
        this.activityView = activityView;
        this.sharedPreferences = sharedPreferences;
    }

    void checkSignInStatus() {
        if (FirebaseAuthUtils.INSTANCE.getCurrentUser() != null) {
            activityView.closeActivity();
        }
    }

    void signIn(String email, String password) {
        activityView.showDialog();
        FirebaseAuthUtils.INSTANCE.signIn(email, password, new FirebaseAuthUtils.AuthActionListener() {
            @Override
            public void actionSuccessful(String response) {
                getUserDefaults();
            }

            @Override
            public void actionFailed(String response) {
                activityView.displayMessage(response);
            }
        });
    }

    private void getUserDefaults() {
        FirebaseDBUtil.INSTANCE.getUserDefaults(new FirebaseDBUtil.UserDetailsListener() {
            @Override
            public void taskSuccessful(String name, String phone, String deliveryAddress) {
                saveToSharedPreferences(name, phone, deliveryAddress);
                activityView.dismissDialog();
                activityView.displayMessage("Sign in successful");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activityView.finishSignUp();
                    }
                }, 500);
            }

            @Override
            public void taskFailed(String response) {
                activityView.displayMessage(response);
            }
        });
    }

    void resetUserPassword(String email) {
        FirebaseAuthUtils.INSTANCE.sendPasswordResetEmail(email, new FirebaseAuthUtils.AuthActionListener() {
            @Override
            public void actionSuccessful(String response) {
                activityView.displayMessage(response);
            }

            @Override
            public void actionFailed(String response) {
                activityView.displayMessage(response);
            }
        });
    }
    private void saveToSharedPreferences(String name, String phone, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.INSTANCE.getDEF_NAME(), name);
        editor.putString(Constants.INSTANCE.getDEF_PHONE(), phone);
        editor.putString(Constants.INSTANCE.getDEF_ADDRESS(), address);

        editor.apply();
    }
}
