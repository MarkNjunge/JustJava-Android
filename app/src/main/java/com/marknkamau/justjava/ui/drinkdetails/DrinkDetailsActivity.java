package com.marknkamau.justjava.ui.drinkdetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marknkamau.justjava.JustJavaApp;
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.data.PreferencesRepository;
import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.models.CoffeeDrink;
import com.marknkamau.justjava.ui.about.AboutActivity;
import com.marknkamau.justjava.ui.cart.CartActivity;
import com.marknkamau.justjava.ui.login.LogInActivity;
import com.marknkamau.justjava.ui.main.CatalogAdapter;
import com.marknkamau.justjava.ui.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class DrinkDetailsActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, DrinkDetailsView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_drink_image)
    ImageView imgDrinkImage;
    @BindView(R.id.tv_drink_name)
    TextView tvDrinkName;
    @BindView(R.id.tv_drink_description)
    TextView tvDrinkDescription;
    @BindView(R.id.tv_drink_contents)
    TextView tvDrinkContents;
    @BindView(R.id.tv_drink_price)
    TextView tvDrinkPrice;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.tv_subtotal)
    TextView tvSubtotal;
    @BindView(R.id.img_minus_qty)
    ImageView imgMinusQty;
    @BindView(R.id.img_add_qty)
    ImageView imgAddQty;
    @BindView(R.id.btn_add_to_cart)
    Button btnAddToCart;
    @BindView(R.id.cb_cinnamon)
    CheckBox cbCinnamon;
    @BindView(R.id.cb_chocolate)
    CheckBox cbChocolate;
    @BindView(R.id.cb_marshmallow)
    CheckBox cbMarshmallow;

    private CoffeeDrink drink;
    private int quantity;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DrinkDetailsPresenter presenter;

    @Inject
    PreferencesRepository preferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((JustJavaApp) getApplication()).getAppComponent().inject(this);
        presenter = new DrinkDetailsPresenter(this, preferencesRepository);

        firebaseAuth = FirebaseAuth.getInstance();

        drink = getIntent().getExtras().getParcelable(CatalogAdapter.DRINK_KEY);

        initializeViews();
        quantity = 1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
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
            case R.id.menu_cart:
                startActivity(new Intent(DrinkDetailsActivity.this, CartActivity.class));
                return true;
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

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @OnClick({R.id.img_minus_qty, R.id.img_add_qty, R.id.btn_add_to_cart, R.id.cb_cinnamon, R.id.cb_chocolate, R.id.cb_marshmallow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_minus_qty:
                minusQty();
                break;
            case R.id.img_add_qty:
                addQty();
                break;
            case R.id.btn_add_to_cart:
                addToCart();
                break;
            case R.id.cb_cinnamon:
                updateSubtotal();
                break;
            case R.id.cb_chocolate:
                updateSubtotal();
                break;
            case R.id.cb_marshmallow:
                updateSubtotal();
                break;
        }
    }

    private void addToCart() {
        final String cinnamon = cbCinnamon.isChecked() ? "true" : "false";
        final String choc = cbChocolate.isChecked() ? "true" : "false";
        final String marshmallow = cbMarshmallow.isChecked() ? "true" : "false";
        final int total = updateSubtotal();

        CartItem item = new CartItem(
                0, drink.getDrinkName(), String.valueOf(quantity), cinnamon, choc, marshmallow, total
        );
        presenter.addToCart(item);
    }

    private void initializeViews() {
        if (drink != null) {
            tvDrinkName.setText(drink.getDrinkName());
            tvDrinkContents.setText(drink.getDrinkContents());
            tvDrinkDescription.setText(drink.getDrinkDescription());
            tvDrinkPrice.setText(String.format("%s%s", getResources().getString(R.string.ksh), drink.getDrinkPrice()));
            tvSubtotal.setText(String.format("%s%s", getResources().getString(R.string.ksh), drink.getDrinkPrice()));

            String drinkImage = "file:///android_asset/" + drink.getDrinkImage();
            Picasso picasso = Picasso.with(this);
            picasso.load(drinkImage).noFade().into(imgDrinkImage);
        }
    }

    private void minusQty() {
        if (quantity > 1) {
            quantity -= 1;
        }
        tvQuantity.setText(String.valueOf(quantity));
        updateSubtotal();
    }

    private void addQty() {
        quantity += 1;
        tvQuantity.setText(String.valueOf(quantity));
        updateSubtotal();
    }

    @SuppressLint("SetTextI18n")
    private int updateSubtotal() {
        int base = Integer.parseInt(drink.getDrinkPrice());
        base = base * quantity;
        if (cbCinnamon.isChecked()) {
            base = base + (quantity * 100);
        }
        if (cbChocolate.isChecked()) {
            base = base + (quantity * 100);
        }
        if (cbMarshmallow.isChecked()) {
            base = base + (quantity * 100);
        }
        tvSubtotal.setText(getResources().getString(R.string.ksh) + base);
        return base;
    }
}
