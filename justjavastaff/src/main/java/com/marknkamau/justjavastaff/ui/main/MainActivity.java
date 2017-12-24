package com.marknkamau.justjavastaff.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.marknkamau.justjavastaff.JustJavaStaffApp;
import com.marknkamau.justjavastaff.R;
import com.marknkamau.justjavastaff.data.network.OrdersRepository;
import com.marknkamau.justjavastaff.models.Order;
import com.marknkamau.justjavastaff.ui.MenuBarActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends MenuBarActivity implements MainView {
    @BindView(R.id.rv_orders)
    RecyclerView rvOrders;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private MainActivityPresenter presenter;
    private OrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = ((JustJavaStaffApp) getApplication()).getPreferences();
        OrdersRepository ordersRepository = ((JustJavaStaffApp) getApplication()).getOrdersRepository();
        presenter = new MainActivityPresenter(this, sharedPreferences, ordersRepository);

        rvOrders.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOrders.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapter = new OrdersAdapter(this);
        adapter.setOnClickListener(new OrdersAdapter.OnClickListener() {
            @Override
            public void onClick(Order element) {
                Toast.makeText(MainActivity.this, element.getOrderId(), Toast.LENGTH_SHORT).show();
            }
        });

        rvOrders.setAdapter(adapter);

        presenter.getOrders();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayAvailableOrders(List<Order> orders) {
        pbLoading.setVisibility(View.GONE);
        adapter.setItems(orders);
    }

    @Override
    public void displayNoOrders() {
        Toast.makeText(this, "There are no orders", Toast.LENGTH_SHORT).show();
        pbLoading.setVisibility(View.GONE);
    }
}
