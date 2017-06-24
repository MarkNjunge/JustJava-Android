package com.marknkamau.justjava.ui.signup;

import android.content.SharedPreferences;
import android.os.Handler;

import com.marknkamau.justjava.models.UserDefaults;
import com.marknkamau.justjava.utils.Constants;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.FirebaseDBUtil;

class SignUpActivityPresenter {
    private SignUpActivityView activityView;
    private SharedPreferences sharedPreferences;

    SignUpActivityPresenter(SignUpActivityView activityView, SharedPreferences sharedPreferences) {
        this.activityView = activityView;
        this.sharedPreferences = sharedPreferences;
    }

    void createUser(final String email, final String password, final String name, final String phone, final String address) {
        activityView.disableUserInteraction();

        FirebaseAuthUtils.INSTANCE.createUser(email, password, new FirebaseAuthUtils.AuthActionListener() {
            @Override
            public void actionSuccessful(String response) {
                signInUser(email, password, name, phone, address);
            }

            @Override
            public void actionFailed(String response) {
                activityView.enableUserInteraction();
                activityView.displayMessage(response);
            }
        });
    }

    private void signInUser(String email, String password, final String name, final String phone, final String address) {
        FirebaseAuthUtils.INSTANCE.signIn(email, password, new FirebaseAuthUtils.AuthActionListener() {
            @Override
            public void actionSuccessful(String response) {
                setUserDisplayName(name, phone, address);
            }

            @Override
            public void actionFailed(String response) {
                activityView.enableUserInteraction();
                activityView.displayMessage(response);
            }
        });
    }

    private void setUserDisplayName(final String name, final String phone, final String address) {
        FirebaseAuthUtils.INSTANCE.setUserDisplayName(name, new FirebaseAuthUtils.AuthActionListener() {
            @Override
            public void actionSuccessful(String response) {
                FirebaseDBUtil.INSTANCE.setUserDefaults(new UserDefaults(name, phone, address), new FirebaseDBUtil.UploadListener() {
                    @Override
                    public void taskSuccessful() {
                        activityView.enableUserInteraction();
                        saveToSharedPreferences(name, phone, address);
                        activityView.displayMessage("Sign up successfully");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                activityView.finishActivity();
                            }
                        }, 500);

                    }

                    @Override
                    public void taskFailed(String response) {
                        activityView.enableUserInteraction();
                        activityView.displayMessage(response);
                    }
                });

            }

            @Override
            public void actionFailed(String response) {
                activityView.enableUserInteraction();
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
