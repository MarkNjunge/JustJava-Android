package com.marknkamau.justjava.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marknkamau.justjava.BuildConfig;
import com.marknkamau.justjava.JustJavaApp;
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.data.PreferencesRepository;
import com.marknkamau.justjava.ui.about.AboutActivity;
import com.marknkamau.justjava.ui.cart.CartActivity;
import com.marknkamau.justjava.ui.login.LogInActivity;
import com.marknkamau.justjava.ui.profile.ProfileActivity;
import com.marknkamau.justjava.models.CoffeeDrink;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, MainView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_catalog)
    RecyclerView recyclerView;
    @BindView(R.id.fab_cart)
    FloatingActionButton fabCart;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private MainPresenter presenter;

    @Inject
    PreferencesRepository preferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scroll Down
                    if (fabCart.isShown()) {
                        fabCart.hide();
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!fabCart.isShown()) {
                        fabCart.show();
                    }
                }
            }
        });

        ((JustJavaApp) getApplication()).getAppComponent().inject(this);
        presenter = new MainPresenter(this, preferencesRepository);
        presenter.getCatalogItems();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!fabCart.isShown()) {
            fabCart.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        user = firebaseAuth.getCurrentUser();
        MenuInflater inflater = getMenuInflater();
        if (user == null) {
            inflater.inflate(R.menu.toolbar_menu, menu);
        } else {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_log_in:
                startActivity(new Intent(this, LogInActivity.class));
                return true;
            case R.id.menu_log_out:
                presenter.logUserOut();
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
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        invalidateOptionsMenu();
    }

    @OnClick(R.id.fab_cart)
    public void onClick() {
        startActivity(new Intent(MainActivity.this, CartActivity.class));
    }

    @Override
    public void displayCatalog(List<CoffeeDrink> drinkList) {
        recyclerView.setAdapter(new CatalogAdapter(this, drinkList));
    }
}
