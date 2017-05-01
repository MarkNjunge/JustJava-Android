package com.marknkamau.justjava.activities.login;

import android.os.Handler;

import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.FirebaseDBUtil;

class LogInActivityPresenter {
    private LogInActivityView activityView;

    LogInActivityPresenter(LogInActivityView activityView) {
        this.activityView = activityView;
    }

    void checkSignInStatus() {
        if (FirebaseAuthUtils.getCurrentUser() != null) {
            activityView.closeActivity();
        }
    }

    void signIn(String email, String password) {
        activityView.showDialog();
        FirebaseAuthUtils.signIn(email, password, new FirebaseAuthUtils.AuthActionListener() {
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
        FirebaseDBUtil.getUserDefaults(new FirebaseDBUtil.UserDetailsListener() {
            @Override
            public void dataReceived(String name, String phone, String deliveryAddress) {
                activityView.saveUserDefaults(name, phone, deliveryAddress);

                activityView.dismissDialog();
                activityView.displayMessage("Sign in successful");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activityView.finishSignUp();
                    }
                }, 100);
            }

            @Override
            public void actionFailed(String response) {
                activityView.displayMessage(response);
            }
        });
    }

    void resetUserPassword(String email) {
        FirebaseAuthUtils.sendPasswordResetEmail(email, new FirebaseAuthUtils.AuthActionListener() {
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
}
