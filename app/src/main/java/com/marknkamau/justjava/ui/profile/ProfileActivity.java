package com.marknkamau.justjava.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.marknkamau.justjava.JustJavaApp;
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.ui.about.AboutActivity;
import com.marknkamau.justjava.ui.login.LogInActivity;
import com.marknkamau.justjava.models.PreviousOrder;
import com.marknkamau.justjava.utils.Constants;
import com.marknkamau.justjava.utils.FirebaseAuthUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements ProfileActivityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_delivery_address)
    EditText etDeliveryAddress;
    @BindView(R.id.pb_saving)
    ProgressBar pbSaving;
    @BindView(R.id.rv_previous_orders)
    RecyclerView rvPreviousOrders;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.pb_loading_orders)
    ProgressBar pbLoadingOrders;

    private FirebaseUser user;
    private String name, phone, address;
    private ProfileActivityPresenter presenter;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        user = FirebaseAuthUtils.getCurrentUser();

        if (user == null) {
            finish();
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rvPreviousOrders.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        rvPreviousOrders.addItemDecoration(itemDecoration);

        ((JustJavaApp) getApplication()).getAppComponent().inject(this);
        presenter = new ProfileActivityPresenter(this, sharedPreferences);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        user = FirebaseAuthUtils.getCurrentUser();
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
                finish();
                return true;
            case R.id.menu_profile:
                // Do nothing
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                saveChanges();
                break;
        }
    }


    @Override
    public void displayUserDefaults(Map<String, String> defaults) {
        etName.setText(defaults.get(Constants.DEF_NAME));
        etPhoneNumber.setText(defaults.get(Constants.DEF_PHONE));
        etDeliveryAddress.setText(defaults.get(Constants.DEF_ADDRESS));
    }

    @Override
    public void showProgressBar() {
        pbSaving.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pbSaving.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayNoPreviousOrders() {
        pbLoadingOrders.setVisibility(View.GONE);
        Toast.makeText(this, "No previous orders", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayPreviousOrders(List<PreviousOrder> orderList) {
        pbLoadingOrders.setVisibility(View.GONE);
        rvPreviousOrders.setAdapter(new PreviousOrderAdapter(this, orderList));
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveChanges() {
        if (fieldsOk()) {
            presenter.updateUserDefaults(name, phone, address);
        }
    }

    private boolean fieldsOk() {
        name = etName.getText().toString().trim();
        phone = etPhoneNumber.getText().toString().trim();
        address = etDeliveryAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}