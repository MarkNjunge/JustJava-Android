package com.marknkamau.justjava.activities.signup;

public interface SignUpActivityView {
    void enableUserInteraction();
    void disableUserInteraction();
    void displayMessage(String message);
    void finishActivity();
}
