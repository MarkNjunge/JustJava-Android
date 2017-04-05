package com.marknkamau.justjavastaff;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marknkamau.justjavastaff.adapters.OrderItemAdapter;
import com.marknkamau.justjavastaff.adapters.CustomerOrdersAdapter;
import com.marknkamau.justjavastaff.models.CustomerOrder;
import com.marknkamau.justjavastaff.models.OrderItem;
import com.marknkamau.justjavastaff.util.FirebaseUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetails extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_timestamp)
    TextView tvTimestamp;
    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;
    @BindView(R.id.tv_customer_phone)
    TextView tvCustomerPhone;
    @BindView(R.id.tv_delivery_address)
    TextView tvDeliveryAddress;
    @BindView(R.id.tv_additional_comments)
    TextView tvAdditionalComments;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.rv_order_items)
    RecyclerView rvOrderItems;
    @BindView(R.id.btn_advance_order)
    Button btnAdvanceOrder;
    @BindView(R.id.btn_cancel_order)
    Button btnCancelOrder;

    private CustomerOrder customerOrder;
    private String currentStatus;
    private String previousStatus;
    private String nextStatus;
    private String orderID;
    private static final String PENDING = "Pending";
    private static final String IN_PROGRESS = "In progress";
    private static final String COMPLETED = "Completed";
    private static final String DELIVERED = "Delivered";
    private static final String CANCELLED = "Cancelled";
    private int colorPending, colorInProgress, colorCancelled, colorCompleted, colorDelivered;
    private Drawable drawableInProgress, drawableCompleted, drawableDisabled, drawableDelivered;
    private DatabaseReference databaseReference;
    private List<OrderItem> orderItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        colorPending = ContextCompat.getColor(this, R.color.colorPending);
        colorInProgress = ContextCompat.getColor(this, R.color.colorInProgress);
        colorCancelled = ContextCompat.getColor(this, R.color.colorCancelled);
        colorCompleted = ContextCompat.getColor(this, R.color.colorCompleted);
        colorDelivered = ContextCompat.getColor(this, R.color.colorDelivered);

        drawableInProgress = ContextCompat.getDrawable(this, R.drawable.button_in_progress);
        drawableCompleted = ContextCompat.getDrawable(this, R.drawable.button_completed);
        drawableDisabled = ContextCompat.getDrawable(this, R.drawable.button_disabled);
        drawableDelivered = ContextCompat.getDrawable(this, R.drawable.button_delivered);

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOrderItems.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        setOrderDisplay();
        databaseReference = FirebaseUtil.getDatabase().getReference();
        Query query = databaseReference.child("orderItems/" + orderID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderItemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    orderItemList.add(new OrderItem(
                            String.valueOf(snapshot.child("itemName").getValue())
                            , String.valueOf(snapshot.child("itemQty").getValue())
                            , String.valueOf(snapshot.child("itemCinnamon").getValue())
                            , String.valueOf(snapshot.child("itemChoc").getValue())
                            , String.valueOf(snapshot.child("itemMarshmallow").getValue())
                            , String.valueOf(snapshot.child("itemPrice").getValue())
                    ));
                }
                rvOrderItems.setAdapter(new OrderItemAdapter(OrderDetails.this, orderItemList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @OnClick({R.id.btn_advance_order, R.id.btn_cancel_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_advance_order:
                confirmAdvanceOrder();
                break;
            case R.id.btn_cancel_order:
                confirmCancel();
                break;
        }
    }

    public void confirmAdvanceOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to set the order to " + nextStatus + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        advanceOrder();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    public void confirmCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel the order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentStatus = CANCELLED;
                        tvOrderStatus.setText(CANCELLED);
                        updateDatabaseStatus(CANCELLED);
                        refreshOrderStatus();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    private void advanceOrder() {
        previousStatus = currentStatus;
        currentStatus = nextStatus;
        tvOrderStatus.setText(currentStatus);
        updateDatabaseStatus(currentStatus);
        refreshOrderStatus();
    }

    private void setOrderDisplay() {
        customerOrder = getIntent().getExtras().getParcelable(CustomerOrdersAdapter.ORDER_TAG);

        orderID = customerOrder.getOrderID();
        currentStatus = customerOrder.getOrderStatus();
        String additionalComments = customerOrder.getAdditionalComments();

        tvOrderId.setText(orderID);
        tvTimestamp.setText(getFormattedDate(Long.valueOf(customerOrder.getTimestamp())));
        tvCustomerName.setText(customerOrder.getCustomerName());
        tvCustomerPhone.setText(customerOrder.getCustomerPhone());
        tvDeliveryAddress.setText(customerOrder.getDeliveryAddress());
        tvTotalPrice.setText(customerOrder.getTotalPrice());
        tvOrderStatus.setText(currentStatus);
        if (TextUtils.isEmpty(additionalComments))
            tvAdditionalComments.setVisibility(View.GONE);
        else
            tvAdditionalComments.setText(additionalComments);

        refreshOrderStatus();
    }

    private void refreshOrderStatus() {
        if (currentStatusIs(PENDING)) {
            tvOrderStatus.setBackgroundColor(colorPending);
            nextStatus = IN_PROGRESS;
            btnAdvanceOrder.setText("Set order to " + nextStatus);
            btnAdvanceOrder.setBackground(drawableInProgress);

        } else if (currentStatusIs(IN_PROGRESS)) {
            tvOrderStatus.setBackgroundColor(colorInProgress);
            nextStatus = COMPLETED;
            btnAdvanceOrder.setText("Set order to " + nextStatus);
            btnAdvanceOrder.setBackground(drawableCompleted);

        } else if (currentStatusIs(COMPLETED)) {
            tvOrderStatus.setBackgroundColor(colorCompleted);
            nextStatus = DELIVERED;
            btnAdvanceOrder.setText("Set order to " + nextStatus);
            btnAdvanceOrder.setBackground(drawableDelivered);
            btnCancelOrder.setVisibility(View.GONE);

        } else if (currentStatusIs(DELIVERED)) {
            tvOrderStatus.setBackgroundColor(colorDelivered);
            nextStatus = null;

        } else if (currentStatusIs(CANCELLED)) {
            tvOrderStatus.setBackgroundColor(colorCancelled);
            nextStatus = null;
        }

        if (nextStatus == null) {
            btnAdvanceOrder.setVisibility(View.GONE);
            btnCancelOrder.setVisibility(View.GONE);
        }
    }

    private boolean currentStatusIs(String s) {
        return TextUtils.equals(currentStatus, s);
    }

    private String getFormattedDate(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            Date currentTime = calendar.getTime();
            return DateFormat.getDateTimeInstance().format(currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(timestamp);
    }

    private void updateDatabaseStatus(String status){
        DatabaseReference orderStatusRef = databaseReference.child("allOrders/" + orderID + "/orderStatus");
        orderStatusRef.setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(OrderDetails.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    currentStatus = previousStatus;
                    refreshOrderStatus();
                }
            }
        });

        if (TextUtils.equals(status, COMPLETED)){
            DatabaseReference completedOrders = databaseReference.child("completedOrders");
            completedOrders.push().setValue(orderID);
        }
        if (TextUtils.equals(status, CANCELLED)){
            DatabaseReference cancelledOrders = databaseReference.child("cancelledOrders");
            cancelledOrders.push().setValue(orderID);
        }
    }

}
