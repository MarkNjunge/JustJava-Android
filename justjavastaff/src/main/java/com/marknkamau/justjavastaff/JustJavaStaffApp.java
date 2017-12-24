package com.marknkamau.justjavastaff;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.marknkamau.justjavastaff.authentication.AuthServiceImpl;
import com.marknkamau.justjavastaff.authentication.AuthenticationService;
import com.marknkamau.justjavastaff.data.network.OrdersRepository;
import com.marknkamau.justjavastaff.data.network.OrdersRepositoryImpl;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class JustJavaStaffApp extends Application {
    private SharedPreferences preferences;
    private AuthenticationService auth;
    private OrdersRepository ordersRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(@NotNull StackTraceElement element) {
                    return "Timber" + super.createStackElementTag(element) + "." + element.getMethodName();
                }
            });
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        auth = new AuthServiceImpl();
        ordersRepository = new OrdersRepositoryImpl();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public AuthenticationService getAuth() {
        return auth;
    }

    public OrdersRepository getOrdersRepository() {
        return ordersRepository;
    }
}
