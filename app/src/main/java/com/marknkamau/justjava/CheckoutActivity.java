package com.marknkamau.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.marknkamau.justjava.authentication.login.LogInActivity;
import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.utils.FirebaseDBUtil;
import com.marknkamau.justjava.utils.MenuActions;
import com.marknkamau.justjava.utils.PreferencesInteraction;
import com.marknkamau.justjava.utils.RealmUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_log_in)
    Button btnLogIn;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_delivery_address)
    EditText etDeliveryAddress;
    @BindView(R.id.et_comments)
    MultiAutoCompleteTextView etComments;
    @BindView(R.id.btn_place_order)
    Button btnPlaceOrder;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.tv_or)
    TextView tvOr;

    private String name, phone, address, comments;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private RealmUtils realmUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        realmUtils = new RealmUtils();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
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
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvOr.setText(getString(R.string.logged_in_as) + " " + user.getDisplayName());
            btnLogIn.setVisibility(View.GONE);

            Map<String, String> defaults = PreferencesInteraction.getDefaults(this);
            etName.setText(defaults.get(PreferencesInteraction.DEF_NAME));
            etPhoneNumber.setText(defaults.get(PreferencesInteraction.DEF_PHONE));
            etDeliveryAddress.setText(defaults.get(PreferencesInteraction.DEF_ADDRESS));
        } else {
            tvOr.setText(getString(R.string.or));
            btnLogIn.setVisibility(View.VISIBLE);
        }
        invalidateOptionsMenu();
    }

    @OnClick({R.id.btn_log_in, R.id.btn_place_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log_in:
                startActivity(new Intent(CheckoutActivity.this, LogInActivity.class));
                break;
            case R.id.btn_place_order:
                if (canConnectToInternet()) {
                    placeOder();
                } else {
                    Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void placeOder() {
        if (validateInput()) {
            pbProgress.setVisibility(View.VISIBLE);
            btnPlaceOrder.setBackgroundResource(R.drawable.large_button_disabled);
            btnPlaceOrder.setEnabled(false);

            final List<CartItem> cartItems = realmUtils.getAllCartItems();
            int items = cartItems.size();
            int totalCost = realmUtils.getTotalCost();

            final DatabaseReference database = FirebaseDBUtil.getDatabase().getReference();

            DatabaseReference orderRef = database.child("allOrders").push();
            final String key = orderRef.getKey();

            orderRef.child("orderID").setValue(key);
            orderRef.child("customerName").setValue(name);
            orderRef.child("customerPhone").setValue(phone);
            orderRef.child("itemsCount").setValue(items);
            orderRef.child("totalPrice").setValue(totalCost);
            orderRef.child("orderStatus").setValue("Pending");
            orderRef.child("deliveryAddress").setValue(address);
            orderRef.child("additionalComments").setValue(comments);
            orderRef.child("deviceToken").setValue(FirebaseInstanceId.getInstance().getToken());
            orderRef.child("timestamp").setValue(ServerValue.TIMESTAMP);
            if (user != null) {
                orderRef.child("user").setValue(user.getUid());
                DatabaseReference userOrdersRef = database.child("userOrders/" + user.getUid());
                userOrdersRef.push().setValue(key);
            } else {
                orderRef.child("user").setValue("null");
            }

            DatabaseReference orderItemsRef = database.child("orderItems").child(key);
            orderItemsRef.setValue(cartItems).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CheckoutActivity.this, getString(R.string.order_placed), Toast.LENGTH_SHORT).show();

                        realmUtils.deleteAllItems();
                    } else {
                        pbProgress.setVisibility(View.INVISIBLE);
                        btnPlaceOrder.setBackgroundResource(R.drawable.large_button);
                        btnPlaceOrder.setEnabled(true);

                        Toast.makeText(CheckoutActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private boolean validateInput() {
        name = etName.getText().toString().trim();
        phone = etPhoneNumber.getText().toString().trim();
        address = etDeliveryAddress.getText().toString().trim();
        comments = etComments.getText().toString().trim();

        Boolean returnValue = true;
        if (TextUtils.isEmpty(name)) {
            etName.setError(getString(R.string.required));
            returnValue = false;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhoneNumber.setError(getString(R.string.required));
            returnValue = false;
        }
        if (TextUtils.isEmpty(address)) {
            etDeliveryAddress.setError(getString(R.string.required));
            returnValue = false;
        }
        return returnValue;
    }

    private boolean canConnectToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
