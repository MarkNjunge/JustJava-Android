package com.marknkamau.justjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marknkamau.justjava.adapters.PreviousOrderAdapter;
import com.marknkamau.justjava.activities.main.MainActivity;
import com.marknkamau.justjava.models.PreviousOrder;
import com.marknkamau.justjava.utils.FirebaseDBUtil;
import com.marknkamau.justjava.utils.MenuActions;
import com.marknkamau.justjava.utils.PreferencesInteraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

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

    private Query userOrders;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String name, phone, address;
    private ValueEventListener eventListener;
    private List<PreviousOrder> previousOrders;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (user != null) {
            Map<String, String> defaults = PreferencesInteraction.getDefaults(this);
            etName.setText(defaults.get(PreferencesInteraction.DEF_NAME));
            etPhoneNumber.setText(defaults.get(PreferencesInteraction.DEF_PHONE));
            etDeliveryAddress.setText(defaults.get(PreferencesInteraction.DEF_ADDRESS));
        } else {
            finish();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rvPreviousOrders.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        rvPreviousOrders.addItemDecoration(itemDecoration);

        databaseReference = FirebaseDBUtil.getDatabase().getReference();
        userOrders = databaseReference.child("userOrders/" + user.getUid());

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pbLoadingOrders.setVisibility(View.GONE);
                previousOrders = new ArrayList<>();
                if (dataSnapshot == null)
                    return;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Query orderItem = databaseReference.child("allOrders/" + snapshot.getValue());
                    orderItem.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            try {
                                previousOrders.add(new PreviousOrder(
                                        snapshot.child("deliveryAddress").getValue().toString()
                                        , snapshot.child("timestamp").getValue().toString()
                                        , snapshot.child("totalPrice").getValue().toString()
                                        , snapshot.child("orderStatus").getValue().toString())
                                );
                            } catch (Exception e) {
                                // null object reference if orders have been deleted
                            }
                            setAdapter(previousOrders);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        userOrders.addValueEventListener(eventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userOrders.removeEventListener(eventListener);
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
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                return true;
            case R.id.menu_profile:
                // Do nothing
                return true;
            case R.id.menu_about:
                MenuActions.ActionAbout(this);
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

    private void saveChanges() {
        if (fieldsOk()) {
            pbSaving.setVisibility(View.VISIBLE);

            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    pbSaving.setVisibility(View.INVISIBLE);

                    if (task.isSuccessful()) {
                        DatabaseReference userReference = databaseReference.child("users").child(user.getUid());
                        userReference.child("name").setValue(name);
                        userReference.child("phone").setValue(phone);
                        userReference.child("defaultAddress").setValue(address);

                        PreferencesInteraction.setDefaults(getApplicationContext(), name, phone, address);

                        Toast.makeText(ProfileActivity.this, "Values updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setAdapter(List<PreviousOrder> orderList) {
        rvPreviousOrders.setAdapter(new PreviousOrderAdapter(this, orderList));
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