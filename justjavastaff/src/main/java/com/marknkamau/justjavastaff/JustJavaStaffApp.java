package com.marknkamau.justjavastaff;

import android.app.Application;

import com.marknkamau.justjavastaff.dagger.AppComponent;
//import com.marknkamau.justjavastaff.dagger.DaggerAppComponent;
import com.marknkamau.justjavastaff.dagger.SharedPreferencesModule;

public class JustJavaStaffApp extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

//        appComponent = DaggerAppComponent.builder()
//                .sharedPreferencesModule(new SharedPreferencesModule(this))
//                .build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
