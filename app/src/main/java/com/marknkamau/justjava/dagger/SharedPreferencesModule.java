package com.marknkamau.justjava.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {
    private Application application;

    public SharedPreferencesModule(Application application) {
        this.application = application;
    }

    @Provides
    public SharedPreferences provideSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
