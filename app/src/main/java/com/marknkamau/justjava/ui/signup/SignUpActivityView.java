package com.marknkamau.justjava.ui.signup;

public interface SignUpActivityView {
    void enableUserInteraction();
    void disableUserInteraction();
    void displayMessage(String message);
    void finishActivity();
}
