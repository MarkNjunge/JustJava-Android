package com.marknkamau.justjava.dagger;

import com.marknkamau.justjava.activities.cart.CartActivity;
import com.marknkamau.justjava.activities.checkout.CheckoutActivity;
import com.marknkamau.justjava.activities.drinkdetails.DrinkDetailsActivity;
import com.marknkamau.justjava.activities.login.LogInActivity;
import com.marknkamau.justjava.activities.main.MainActivity;
import com.marknkamau.justjava.activities.profile.ProfileActivity;
import com.marknkamau.justjava.activities.signup.SignUpActivity;

import dagger.Component;

@Component(modules = {SharedPreferencesModule.class})
public interface AppComponent {
    void inject(LogInActivity logInActivity);
    void inject(ProfileActivity profileActivity);
    void inject(SignUpActivity signUpActivity);
    void inject(CartActivity cartActivity);
    void inject(DrinkDetailsActivity drinkDetailsActivity);
    void inject(MainActivity mainActivity);
    void inject(CheckoutActivity signUpActivity);
}
