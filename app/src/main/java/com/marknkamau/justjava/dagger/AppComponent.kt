package com.marknkamau.justjava.dagger

import com.marknkamau.justjava.ui.cart.CartActivity
import com.marknkamau.justjava.ui.checkout.CheckoutActivity
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.ui.profile.ProfileActivity
import com.marknkamau.justjava.ui.signup.SignUpActivity

import dagger.Component

@Component(modules = arrayOf(PreferencesRepositoryModule::class, FirebaseModule::class))
interface AppComponent {
    fun inject(logInActivity: LogInActivity)
    fun inject(profileActivity: ProfileActivity)
    fun inject(signUpActivity: SignUpActivity)
    fun inject(cartActivity: CartActivity)
    fun inject(drinkDetailsActivity: DrinkDetailsActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(checkoutActivity: CheckoutActivity)
}
