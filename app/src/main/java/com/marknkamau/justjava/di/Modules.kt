package com.marknkamau.justjava.di

import android.preference.PreferenceManager
import androidx.room.Room
import com.marknkamau.justjava.data.db.AppDatabase
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.db.DbRepositoryImpl
import com.marknkamau.justjava.data.preferences.PreferencesRepository
import com.marknkamau.justjava.data.preferences.PreferencesRepositoryImpl
import com.marknkamau.justjava.ui.cart.CartViewModel
import com.marknkamau.justjava.ui.login.LogInPresenter
import com.marknkamau.justjava.ui.login.LogInView
import com.marknkamau.justjava.ui.main.MainViewModel
import com.marknkamau.justjava.ui.previousOrders.PreviousOrdersPresenter
import com.marknkamau.justjava.ui.previousOrders.PreviousOrdersView
import com.marknkamau.justjava.ui.productDetails.ProductDetailsViewModel
import com.marknkamau.justjava.ui.profile.ProfilePresenter
import com.marknkamau.justjava.ui.profile.ProfileView
import com.marknkamau.justjava.ui.signup.SignUpPresenter
import com.marknkamau.justjava.ui.signup.SignUpView
import com.marknkamau.justjava.ui.viewOrder.ViewOrderPresenter
import com.marknkamau.justjava.ui.viewOrder.ViewOrderView
import com.marknkamau.justjava.utils.NotificationHelper
import com.marknkamau.justjava.utils.NotificationHelperImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PreferencesRepository> {
        PreferencesRepositoryImpl(
            PreferenceManager.getDefaultSharedPreferences(
                androidContext()
            )
        )
    }

    single<NotificationHelper> { NotificationHelperImpl(androidContext()) }
}

val dbModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "justjava-db")
            .fallbackToDestructiveMigrationFrom(1)
            .build()
    }
    single { get<AppDatabase>().cartDao() }
    single<DbRepository> { DbRepositoryImpl(get()) }
}

val presentersModule = module {
    factory { (view: LogInView) -> LogInPresenter(view, get(), get(), get(), Dispatchers.Main) }
    factory { (view: SignUpView) -> SignUpPresenter(view, get(), get(), get(), Dispatchers.Main) }
    factory { (view: ViewOrderView) -> ViewOrderPresenter(view, get(), get(), get(), Dispatchers.Main) }
    factory { (view: ProfileView) -> ProfilePresenter(view, get(), get(), get(), get(), Dispatchers.Main) }
    factory { (view: PreviousOrdersView) -> PreviousOrdersPresenter(view, get(), get(), Dispatchers.Main) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ProductDetailsViewModel(get()) }
}