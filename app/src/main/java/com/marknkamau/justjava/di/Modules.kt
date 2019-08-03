package com.marknkamau.justjava.di

import android.preference.PreferenceManager
import androidx.room.Room
import com.marknkamau.justjava.data.local.CartDatabase
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.local.PreferencesRepositoryImpl
import com.marknkamau.justjava.ui.cart.CartPresenter
import com.marknkamau.justjava.ui.cart.CartView
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsPresenter
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsView
import com.marknkamau.justjava.ui.login.LogInPresenter
import com.marknkamau.justjava.ui.login.LogInView
import com.marknkamau.justjava.ui.main.MainPresenter
import com.marknkamau.justjava.ui.main.MainView
import com.marknkamau.justjava.ui.viewOrder.ViewOrderPresenter
import com.marknkamau.justjava.ui.viewOrder.ViewOrderView
import com.marknkamau.justjava.ui.previousOrders.PreviousOrdersPresenter
import com.marknkamau.justjava.ui.previousOrders.PreviousOrdersView
import com.marknkamau.justjava.ui.profile.ProfilePresenter
import com.marknkamau.justjava.ui.profile.ProfileView
import com.marknkamau.justjava.ui.signup.SignUpPresenter
import com.marknkamau.justjava.ui.signup.SignUpView
import com.marknkamau.justjava.utils.NotificationHelper
import com.marknkamau.justjava.utils.NotificationHelperImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(androidContext())) }
    single { Room.databaseBuilder(androidContext(), CartDatabase::class.java, "cart-db").build() }
    single { get<CartDatabase>().cartDao() }
    single<NotificationHelper> { NotificationHelperImpl(androidContext()) }
    single<CoroutineDispatcher>(named("Main")) { Dispatchers.Main }
    factory { (view: LogInView) -> LogInPresenter(view, get(), get(), get(), get(named("Main"))) }
    factory { (view: SignUpView) -> SignUpPresenter(view, get(), get(), get(), get(named("Main"))) }
    factory { (view: MainView) -> MainPresenter(view, get(named("Main"))) }
    factory { (view: DrinkDetailsView) -> DrinkDetailsPresenter(view, get(), get(named("Main"))) }
    factory { (view: CartView) -> CartPresenter(view, get(), get(), get(), get(), get(named("Main"))) }
    factory { (view: ViewOrderView) -> ViewOrderPresenter(view, get(), get(), get(), get(named("Main"))) }
    factory { (view: ProfileView) -> ProfilePresenter(view, get(), get(), get(), get(), get(named("Main"))) }
    factory { (view: PreviousOrdersView) -> PreviousOrdersPresenter(view, get(), get(), get(named("Main"))) }
}