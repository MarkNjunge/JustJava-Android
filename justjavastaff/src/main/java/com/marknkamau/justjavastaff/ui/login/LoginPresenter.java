package com.marknkamau.justjavastaff.ui.login;

import com.marknkamau.justjavastaff.authentication.AuthenticationService;
import com.marknkamau.justjavastaff.models.Employee;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class LoginPresenter {
    private AuthenticationService auth;
    private LoginView view;

    LoginPresenter(AuthenticationService auth, LoginView view) {
        this.auth = auth;
        this.view = view;
    }

    void signIn(String email, String password) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+");
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            view.displayMessage("The email is not valid");
            return;
        }

        auth.signIn(email, password, new AuthenticationService.AuthListener() {
            @Override
            public void onSuccess(Employee employee) {
                view.onSignedIn();
            }

            @Override
            public void onError(String reason) {
                view.displayMessage(reason);
            }
        });
    }

}
