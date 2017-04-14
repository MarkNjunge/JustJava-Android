package com.marknkamau.justjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marknkamau.justjava.adapters.CatalogAdapter;
import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.models.CoffeeDrink;
import com.marknkamau.justjava.utils.MenuActions;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class DrinkDetailsActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

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
    @BindView(R.id.tv_cinnamon)
    TextView tvCinnamon;
    @BindView(R.id.tv_marshmallows)
    TextView tvMarshmallows;
    @BindView(R.id.tv_chocolate)
    TextView tvChocolate;
    @BindView(R.id.btn_add_to_cart)
    Button btnAddToCart;

    private CoffeeDrink drink;
    private int quantity;
    private static final int PADDING = 24;
    private boolean withCinnamon = false, withChocolate = false, withMarshmallow = false;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_details);
        ButterKnife.bind(this);
        Realm.init(this);

        realm = Realm.getDefaultInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        invalidateOptionsMenu();
    }

    @OnClick({R.id.img_minus_qty, R.id.img_add_qty, R.id.tv_cinnamon, R.id.tv_marshmallows, R.id.tv_chocolate, R.id.btn_add_to_cart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_minus_qty:
                minusQty();
                break;
            case R.id.img_add_qty:
                addQty();
                break;
            case R.id.tv_cinnamon:
                switchCinnamon(withCinnamon);
                updateSubtotal();
                break;
            case R.id.tv_chocolate:
                switchChocolate(withChocolate);
                updateSubtotal();
                break;
            case R.id.tv_marshmallows:
                switchMarshmallow(withMarshmallow);
                updateSubtotal();
                break;
            case R.id.btn_add_to_cart:
                addToCart();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void initializeViews() {
        tvCinnamon.setPadding(PADDING, PADDING, PADDING, PADDING);
        tvChocolate.setPadding(PADDING, PADDING, PADDING, PADDING);
        tvMarshmallows.setPadding(PADDING, PADDING, PADDING, PADDING);


        if (drink != null) {
            tvDrinkName.setText(drink.getDrinkName());
            tvDrinkContents.setText(drink.getDrinkContents());
            tvDrinkDescription.setText(drink.getDrinkDescription());
            tvDrinkPrice.setText(getResources().getString(R.string.ksh) + drink.getDrinkPrice());
            tvSubtotal.setText(getResources().getString(R.string.ksh) + drink.getDrinkPrice());

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

    private void setToppingOn(TextView textView) {
        textView.setBackgroundResource(R.drawable.topping_on);
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorToppingOnText));
    }

    private void setToppingOff(TextView textView) {
        textView.setBackgroundResource(R.drawable.topping_off);
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorToppingOffText));
    }

    @SuppressLint("SetTextI18n")
    private int updateSubtotal() {
        int base = Integer.parseInt(drink.getDrinkPrice());
        base = base * quantity;
        if (withCinnamon) {
            base = base + (quantity * 100);
        }
        if (withChocolate) {
            base = base + (quantity * 100);
        }
        if (withMarshmallow) {
            base = base + (quantity * 100);
        }
        tvSubtotal.setText(getResources().getString(R.string.ksh) + base);
        return base;
    }

    private void addToCart() {
        final String cinnamon = (withCinnamon) ? "true" : "false";
        final String choc = (withChocolate) ? "true" : "false";
        final String marshmallow = (withMarshmallow) ? "true" : "false";
        final int total = updateSubtotal();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number current = realm.where(CartItem.class).max("itemID");
                int nextID;
                if (current == null) {
                    nextID = 1;
                }else {
                    nextID = current.intValue() + 1;
                }
                CartItem item = new CartItem(
                        nextID, drink.getDrinkName(), String.valueOf(quantity), cinnamon, choc, marshmallow, total
                );

                realm.copyToRealm(item);
                Toast.makeText(DrinkDetailsActivity.this, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void switchCinnamon(Boolean selected) {
        if (selected) {
            setToppingOff(tvCinnamon);
            withCinnamon = false;
        } else {
            setToppingOn(tvCinnamon);
            withCinnamon = true;
        }
    }

    private void switchChocolate(Boolean selected) {
        if (selected) {
            setToppingOff(tvChocolate);
            withChocolate = false;
        } else {
            setToppingOn(tvChocolate);
            withChocolate = true;
        }
    }

    private void switchMarshmallow(Boolean selected) {
        if (selected) {
            setToppingOff(tvMarshmallows);
            withMarshmallow = false;
        } else {
            setToppingOn(tvMarshmallows);
            withMarshmallow = true;
        }
    }
}
