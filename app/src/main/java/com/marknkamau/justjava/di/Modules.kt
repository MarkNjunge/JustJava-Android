package com.marknkamau.justjava.di

import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.network.service.FirebaseService
import com.marknjunge.core.data.network.GoogleSignInService
import com.marknkamau.justjava.BuildConfig
import com.marknkamau.justjava.data.db.AppDatabase
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.db.DbRepositoryImpl
import com.marknkamau.justjava.data.network.AppFirebaseService
import com.marknkamau.justjava.data.network.GoogleSignInServiceImpl
import com.marknkamau.justjava.data.preferences.PreferencesRepositoryImpl
import com.marknkamau.justjava.ui.addressBook.AddressBookViewModel
import com.marknkamau.justjava.ui.cart.CartViewModel
import com.marknkamau.justjava.ui.checkout.CheckoutViewModel
import com.marknkamau.justjava.ui.login.SignInViewModel
import com.marknkamau.justjava.ui.main.MainViewModel
import com.marknkamau.justjava.ui.orderDetail.OrderDetailViewModel
import com.marknkamau.justjava.ui.orders.OrdersViewModel
import com.marknkamau.justjava.ui.payCard.PayCardViewModel
import com.marknkamau.justjava.ui.payMpesa.PayMpesaViewModel
import com.marknkamau.justjava.ui.productDetails.ProductDetailsViewModel
import com.marknkamau.justjava.ui.profile.ProfileViewModel
import com.marknkamau.justjava.ui.signup.SignUpViewModel
import com.marknkamau.justjava.utils.NotificationHelper
import com.marknkamau.justjava.utils.NotificationHelperImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(androidContext()) }

    single<NotificationHelper> { NotificationHelperImpl(androidContext()) }

    single<GoogleSignInClient> {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.googleClientId)
            .build()
        GoogleSignIn.getClient(androidContext(), gso)
    }

    single<GoogleSignInService> { GoogleSignInServiceImpl(get()) }

    single<FirebaseService> { AppFirebaseService() }
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

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ProductDetailsViewModel(get()) }
    viewModel { CartViewModel(get(), get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { AddressBookViewModel(get()) }
    viewModel { CheckoutViewModel(get(), get(), get(), get()) }
    viewModel { OrdersViewModel(get()) }
    viewModel { OrderDetailViewModel(get(), get()) }
    viewModel { PayMpesaViewModel(get(), get()) }
    viewModel { PayCardViewModel(get()) }
}
