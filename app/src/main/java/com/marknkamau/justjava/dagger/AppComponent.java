package com.marknkamau.justjava.dagger;

import com.marknkamau.justjava.ui.cart.CartActivity;
import com.marknkamau.justjava.ui.checkout.CheckoutActivity;
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsActivity;
import com.marknkamau.justjava.ui.login.LogInActivity;
import com.marknkamau.justjava.ui.main.MainActivity;
import com.marknkamau.justjava.ui.profile.ProfileActivity;
import com.marknkamau.justjava.ui.signup.SignUpActivity;

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
