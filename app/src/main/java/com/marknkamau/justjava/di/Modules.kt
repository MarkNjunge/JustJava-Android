package com.marknkamau.justjava.di

import android.preference.PreferenceManager
import androidx.room.Room
import com.marknkamau.justjava.data.local.CartDatabase
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.local.PreferencesRepositoryImpl
import com.marknkamau.justjava.ui.cart.CartPresenter
import com.marknkamau.justjava.ui.cart.CartView
import com.marknkamau.justjava.ui.checkout.CheckoutPresenter
import com.marknkamau.justjava.ui.checkout.CheckoutView
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsPresenter
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsView
import com.marknkamau.justjava.ui.login.LogInPresenter
import com.marknkamau.justjava.ui.login.LogInView
import com.marknkamau.justjava.ui.main.MainPresenter
import com.marknkamau.justjava.ui.main.MainView
import com.marknkamau.justjava.ui.previousOrder.PreviousOrderPresenter
import com.marknkamau.justjava.ui.previousOrder.PreviousOrderView
import com.marknkamau.justjava.ui.profile.ProfilePresenter
import com.marknkamau.justjava.ui.profile.ProfileView
import com.marknkamau.justjava.ui.signup.SignUpPresenter
import com.marknkamau.justjava.ui.signup.SignUpView
import com.marknkamau.justjava.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) }
    single { Room.databaseBuilder(androidContext(), CartDatabase::class.java, "cart-db").build() }
    single { get<CartDatabase>().cartDao() }
    single { NotificationHelper(androidContext()) }
    factory { (view: LogInView) -> LogInPresenter(view, get(), get(), get(), Dispatchers.Main) }
    factory { (view: SignUpView) -> SignUpPresenter(view, get(), get(), get(), Dispatchers.Main) }
    factory { (view: MainView) -> MainPresenter(view, Dispatchers.Main) }
    factory { (view: DrinkDetailsView) -> DrinkDetailsPresenter(view, get(), Dispatchers.Main) }
    factory { (view: CartView) -> CartPresenter(view, get(), Dispatchers.Main) }
    factory { (view: CheckoutView) -> CheckoutPresenter(view, get(), get(), get(), get(), Dispatchers.Main) }
    factory { (view: PreviousOrderView) -> PreviousOrderPresenter(view, get(), get(), get(), Dispatchers.Main) }
    factory { (view: ProfileView) -> ProfilePresenter(view, get(), get(), get(), get(), Dispatchers.Main) }
}