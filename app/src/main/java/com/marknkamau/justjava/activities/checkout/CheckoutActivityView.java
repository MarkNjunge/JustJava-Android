package com.marknkamau.justjava.activities.checkout;

import com.google.firebase.auth.FirebaseUser;

public interface CheckoutActivityView {
    void setLoggedInStatus(Boolean status);
    void invalidateMenu();
    void setDisplayToLoggedIn(FirebaseUser user);
    void setDisplayToLoggedOut();
    void showUploadBar();
    void hideUploadBar();
    void finishActivity();
    void showMessage(String message);
}
