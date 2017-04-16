package com.marknkamau.justjavastaff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marknkamau.justjavastaff.adapters.CustomerOrdersAdapter;
import com.marknkamau.justjavastaff.models.CustomerOrder;
import com.marknkamau.justjavastaff.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_order_list)
    RecyclerView recyclerView;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private Query query;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;
    private ValueEventListener eventListener;
    private List<CustomerOrder> customerOrders;
    private Boolean showPendingOrders, showInProgressOrders, showCompletedOrders, showDeliveredOrders, showCancelledOrders;

    private static final String PENDING = "Pending";
    private static final String IN_PROGRESS = "In progress";
    private static final String COMPLETED = "Completed";
    private static final String DELIVERED = "Delivered";
    private static final String CANCELLED = "Cancelled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
        }

        getPreferences();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        getOrders();
    }

    private void getOrders() {
        DatabaseReference databaseReference = FirebaseUtil.getDatabase().getReference();
        query = databaseReference.child("allOrders").orderByKey();

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pbLoading.setVisibility(View.GONE);
                customerOrders = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String orderStatus = String.valueOf(snapshot.child("orderStatus").getValue());

                    boolean isPending = TextUtils.equals(orderStatus, PENDING);
                    boolean isInProgress = TextUtils.equals(orderStatus, IN_PROGRESS);
                    boolean isCompleted = TextUtils.equals(orderStatus, COMPLETED);
                    boolean isDeliverd = TextUtils.equals(orderStatus, DELIVERED);
                    boolean isCancelled = TextUtils.equals(orderStatus, CANCELLED);

                    CustomerOrder item = new CustomerOrder(
                            (String) snapshot.child("orderID").getValue()
                            , String.valueOf(snapshot.child("customerName").getValue())
                            , String.valueOf(snapshot.child("customerPhone").getValue())
                            , String.valueOf(snapshot.child("deliveryAddress").getValue())
                            , String.valueOf(snapshot.child("additionalComments").getValue())
                            , orderStatus
                            , String.valueOf(snapshot.child("timestamp").getValue())
                            , String.valueOf(snapshot.child("totalPrice").getValue())
                            , String.valueOf(snapshot.child("itemsCount").getValue()));

                    if (isPending && showPendingOrders) {
                        customerOrders.add(item);
                    } else if (isInProgress && showInProgressOrders) {
                        customerOrders.add(item);
                    } else if (isCompleted && showCompletedOrders) {
                        customerOrders.add(item);
                    } else if (isDeliverd && showDeliveredOrders) {
                        customerOrders.add(item);
                    } else if (isCancelled && showCancelledOrders) {
                        customerOrders.add(item);
                    }

                }

                recyclerView.setAdapter(new CustomerOrdersAdapter(MainActivity.this, customerOrders));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void getPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        showPendingOrders = preferences.getBoolean(getString(R.string.PENDING_ORDERS_KEY), true);
        showInProgressOrders = preferences.getBoolean(getString(R.string.IN_PROGRESS_ORDERS_KEY), false);
        showCompletedOrders = preferences.getBoolean(getString(R.string.COMPLETED_ORDERS_KEY), false);
        showDeliveredOrders = preferences.getBoolean(getString(R.string.DELIVERED_ORDERS_KEY), false);
        showCancelledOrders = preferences.getBoolean(getString(R.string.CANCELLED_ORDERS_KEY), false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        query.addValueEventListener(eventListener);
        getPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        query.removeEventListener(eventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_log_out:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                finish();
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
                return true;
            case R.id.menu_help:
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
