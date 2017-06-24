package com.marknkamau.justjavastaff.dagger;

import com.marknkamau.justjavastaff.ui.main.MainActivity;

import dagger.Component;

@Component(modules = {SharedPreferencesModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
