package com.marknkamau.justjava.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marknkamau.justjava.JustJavaApp;
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.data.PreferencesRepository;
import com.marknkamau.justjava.network.AuthenticationService;
import com.marknkamau.justjava.ui.about.AboutActivity;
import com.marknkamau.justjava.ui.cart.CartActivity;
import com.marknkamau.justjava.ui.login.LogInActivity;
import com.marknkamau.justjava.ui.profile.ProfileActivity;
import com.marknkamau.justjava.models.CoffeeDrink;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_catalog)
    RecyclerView recyclerView;

    @Inject
    PreferencesRepository preferencesRepository;
    @Inject
    AuthenticationService authenticationService;

    private MainPresenter presenter;
    private boolean isSignedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ((JustJavaApp) getApplication()).getAppComponent().inject(this);
        presenter = new MainPresenter(this, preferencesRepository, authenticationService);
        presenter.getCatalogItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getSignInStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (isSignedIn) {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu);
        } else {
            inflater.inflate(R.menu.toolbar_menu, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            case R.id.menu_log_in:
                startActivity(new Intent(this, LogInActivity.class));
                return true;
            case R.id.menu_log_out:
                presenter.signOut();
                invalidateOptionsMenu();
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void displayCatalog(@NonNull List<CoffeeDrink> drinkList) {
        recyclerView.setAdapter(new CatalogAdapter(this, drinkList));
    }

    @Override
    public void setSignInStatus(boolean status) {
        isSignedIn = status;
        invalidateOptionsMenu();
    }
}
