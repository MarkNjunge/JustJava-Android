package com.marknkamau.justjava;

import android.app.Application;

import com.marknkamau.justjava.dagger.AppComponent;
import com.marknkamau.justjava.dagger.DaggerAppComponent;
import com.marknkamau.justjava.dagger.SharedPreferencesModule;

import io.realm.Realm;
import timber.log.Timber;

public class JustJavaApp extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return "Timber " + super.createStackElementTag(element) + "." + element.getMethodName();
            }
        });

        appComponent = DaggerAppComponent.builder().sharedPreferencesModule(new SharedPreferencesModule(this)).build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }

}
