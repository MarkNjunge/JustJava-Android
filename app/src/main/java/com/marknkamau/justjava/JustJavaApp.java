package com.marknkamau.justjava;

import android.app.Application;

import io.realm.Realm;
import timber.log.Timber;

public class JustJavaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        Timber.plant(new Timber.DebugTree(){
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return "Timber " + super.createStackElementTag(element) + "." + element.getMethodName();
            }
        });
    }

}
