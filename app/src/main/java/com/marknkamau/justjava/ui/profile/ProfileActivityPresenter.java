package com.marknkamau.justjava.ui.profile;

import android.content.SharedPreferences;

import com.marknkamau.justjava.ui.signup.SignUpActivity;
import com.marknkamau.justjava.models.PreviousOrder;
import com.marknkamau.justjava.models.UserDefaults;
import com.marknkamau.justjava.utils.Constants;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.FirebaseDBUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ProfileActivityPresenter {
    private ProfileActivityView activityView;
    private SharedPreferences sharedPreferences;

    ProfileActivityPresenter(ProfileActivityView activityView, SharedPreferences sharedPreferences) {
        this.activityView = activityView;
        this.sharedPreferences = sharedPreferences;
        getUserDefaults();
        getPreviousOrders();
    }

    private void getUserDefaults(){
        Map<String, String> defaults = new HashMap<>();
        defaults.put(Constants.DEF_NAME, sharedPreferences.getString(SignUpActivity.DEF_NAME, ""));
        defaults.put(Constants.DEF_PHONE, sharedPreferences.getString(SignUpActivity.DEF_PHONE, ""));
        defaults.put(Constants.DEF_ADDRESS, sharedPreferences.getString(SignUpActivity.DEF_ADDRESS, ""));

        activityView.displayUserDefaults(defaults);
    }

    private void getPreviousOrders() {
        FirebaseDBUtil.getPreviousOrders(new FirebaseDBUtil.PreviousOrdersListener() {
            @Override
            public void valuesRetrieved(List<PreviousOrder> previousOrders) {
                activityView.displayPreviousOrders(previousOrders);
            }

            @Override
            public void noValuesPresent() {
                activityView.displayNoPreviousOrders();
            }

            @Override
            public void actionFailed(String response) {
                activityView.displayMessage(response);
            }
        });
    }

    void updateUserDefaults(final String name, final String phone, final String address) {
        activityView.showProgressBar();
        FirebaseAuthUtils.setUserDisplayName(name, new FirebaseAuthUtils.AuthActionListener() {
            @Override
            public void actionSuccessful(String response) {
                FirebaseDBUtil.setUserDefaults(new UserDefaults(name, phone, address), new FirebaseDBUtil.SetUserDefaultsListener() {
                    @Override
                    public void taskSuccessful() {
                        saveToSharedPreferences(name, phone, address);
                        activityView.hideProgressBar();
                        activityView.displayMessage("Default values updated");
                    }

                    @Override
                    public void taskFailed(String response) {
                        activityView.hideProgressBar();
                        activityView.displayMessage(response);
                    }
                });
            }

            @Override
            public void actionFailed(String response) {
                activityView.displayMessage(response);
            }
        });
    }

    private void saveToSharedPreferences(String name, String phone, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.DEF_NAME, name);
        editor.putString(Constants.DEF_PHONE, phone);
        editor.putString(Constants.DEF_ADDRESS, address);

        editor.apply();
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
