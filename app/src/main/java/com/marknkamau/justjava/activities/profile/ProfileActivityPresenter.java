package com.marknkamau.justjava.activities.profile;

import android.content.Context;

import com.marknkamau.justjava.models.PreviousOrder;
import com.marknkamau.justjava.models.UserDefaults;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;
import com.marknkamau.justjava.utils.FirebaseDBUtil;
import com.marknkamau.justjava.utils.PreferencesInteraction;

import java.util.List;

class ProfileActivityPresenter {
    private ProfileActivityView activityView;
    private Context context;

    ProfileActivityPresenter(ProfileActivityView activityView, Context context) {
        this.activityView = activityView;
        this.context = context;
        getPreviousOrders();
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
                        PreferencesInteraction.setDefaults(context, name, phone, address);
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
}
