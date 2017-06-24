package com.marknkamau.justjavastaff.ui.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.marknkamau.justjavastaff.R;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MyPreferencesFragment preferencesFragment = new MyPreferencesFragment();
        getFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, preferencesFragment)
                .commit();

    }

    public static class MyPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
