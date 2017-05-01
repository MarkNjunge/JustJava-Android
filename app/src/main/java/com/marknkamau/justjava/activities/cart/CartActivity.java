package com.marknkamau.justjava.activities.cart;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marknkamau.justjava.CheckoutActivity;
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.MenuActions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, CartActivityView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_clear_cart)
    Button btnClearCart;
    @BindView(R.id.rv_cart)
    RecyclerView recyclerView;
    @BindView(R.id.tv_cart_total)
    TextView tvCartTotal;
    @BindView(R.id.btn_checkout)
    Button btnCheckout;
    @BindView(R.id.tv_no_items)
    TextView tvNoItems;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private CartActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        presenter = new CartActivityPresenter(this);
        presenter.loadItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        user = firebaseAuth.getCurrentUser();
        MenuInflater inflater = getMenuInflater();
        if (user != null) {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu);
        } else {
            inflater.inflate(R.menu.toolbar_menu, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_log_in:
                MenuActions.ActionLogIn(this);
                return true;
            case R.id.menu_log_out:
                MenuActions.ActionLogOut(this);
                return true;
            case R.id.menu_profile:
                MenuActions.ActionProfile(this);
                return true;
            case R.id.menu_about:
                MenuActions.ActionAbout(this);
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

    @OnClick({R.id.btn_clear_cart, R.id.btn_checkout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear_cart:
                presenter.clearCart();
                break;
            case R.id.btn_checkout:
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
                break;
        }
    }

    @Override
    public void displayCart(List<CartItem> cartItems) {
        CartAdapter adapter = new CartAdapter(this, cartItems, new CartAdapter.CartAdapterListener() {
            @Override
            public void updateList() {
                presenter.loadItems();
            }
        });

        recyclerView.setAdapter(adapter);
        btnCheckout.setEnabled(true);
    }

    @Override
    public void displayCartTotal(int totalCost) {
        tvCartTotal.setText(getString(R.string.total) + ": " + getString(R.string.ksh) + totalCost);
    }

    @Override
    public void displayEmptyCart() {
        tvNoItems.setVisibility(View.VISIBLE);
        btnClearCart.setEnabled(false);
        btnClearCart.setAlpha(.54f);
        tvCartTotal.setAlpha(.54f);
        tvCartTotal.setText(getString(R.string.total) + ": " + getString(R.string.ksh));
        btnCheckout.setBackgroundResource(R.drawable.large_button_disabled);
        btnCheckout.setEnabled(false);
    }



}
