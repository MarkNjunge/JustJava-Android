package com.marknkamau.justjavastaff.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marknkamau.justjavastaff.R;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@SuppressLint("Registered")
public class MenuBarActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_log_out:

                return true;
            case R.id.menu_settings:

                return true;
            case R.id.menu_help:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
