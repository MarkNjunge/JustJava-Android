package com.marknkamau.justjava.activities.checkout;

import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public interface CheckoutActivityView {
    void setLoggedInStatus(Boolean status);
    void invalidateMenu();
    void setDisplayToLoggedIn(FirebaseUser user, Map<String, String> defaults);
    void setDisplayToLoggedOut();
    void showUploadBar();
    void hideUploadBar();
    void finishActivity();
    void showMessage(String message);
}
