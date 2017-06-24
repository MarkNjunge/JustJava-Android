package com.marknkamau.justjava.ui.checkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseUser;
import com.marknkamau.justjava.JustJavaApp;
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.models.Order;
import com.marknkamau.justjava.ui.about.AboutActivity;
import com.marknkamau.justjava.ui.login.LogInActivity;
import com.marknkamau.justjava.ui.main.MainActivity;
import com.marknkamau.justjava.ui.profile.ProfileActivity;
import com.marknkamau.justjava.utils.Constants;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutActivity extends AppCompatActivity implements CheckoutActivityView {

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
    private CheckoutActivityPresenter presenter;
    private boolean userIsLoggedIn;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((JustJavaApp) getApplication()).getAppComponent().inject(this);
        presenter = new CheckoutActivityPresenter(this,sharedPreferences);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (userIsLoggedIn) {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu);
        } else {
            inflater.inflate(R.menu.toolbar_menu, menu);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.updateLoggedInStatus();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_log_in:
                startActivity(new Intent(this, LogInActivity.class));
                return true;
            case R.id.menu_log_out:
                presenter.logOut();
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
            presenter.placeOrder(new Order(name, phone, 0, 0, address, comments));
        }
    }

    @Override
    public void setLoggedInStatus(Boolean status) {
        userIsLoggedIn  = status;
    }

    @Override
    public void invalidateMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void setDisplayToLoggedIn(FirebaseUser user, Map<String, String> defaults) {
        tvOr.setText(getString(R.string.logged_in_as) + " " + user.getDisplayName());
        btnLogIn.setVisibility(View.GONE);

        etName.setText(defaults.get(Constants.INSTANCE.getDEF_NAME()));
        etPhoneNumber.setText(defaults.get(Constants.INSTANCE.getDEF_PHONE()));
        etDeliveryAddress.setText(defaults.get(Constants.INSTANCE.getDEF_ADDRESS()));
    }

    @Override
    public void setDisplayToLoggedOut() {
        tvOr.setText(getString(R.string.or));
        btnLogIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUploadBar() {
        pbProgress.setVisibility(View.VISIBLE);
        btnPlaceOrder.setBackgroundResource(R.drawable.large_button_disabled);
        btnPlaceOrder.setEnabled(false);
    }

    @Override
    public void hideUploadBar() {
        pbProgress.setVisibility(View.INVISIBLE);
        btnPlaceOrder.setBackgroundResource(R.drawable.large_button);
        btnPlaceOrder.setEnabled(true);
    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
