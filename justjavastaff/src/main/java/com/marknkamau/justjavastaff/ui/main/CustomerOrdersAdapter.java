package com.marknkamau.justjavastaff.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marknkamau.justjavastaff.ui.orderdetails.OrderDetails;
import com.marknkamau.justjavastaff.R;
import com.marknkamau.justjavastaff.models.CustomerOrder;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerOrdersAdapter extends RecyclerView.Adapter<CustomerOrdersAdapter.ViewHolder> {

    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_items_count)
    TextView tvItemsCount;
    @BindView(R.id.tv_timestamp)
    TextView tvTimestamp;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.order_item)
    ConstraintLayout orderItemLayout;
    @BindView(R.id.view_status)
    View viewStatus;

    private Context mContext;
    private List<CustomerOrder> customerOrders;
    public static final String ORDER_TAG = "order_tag";
    private int colorPending, colorInProgress, colorCancelled, colorCompleted, colorDelivered;

    public CustomerOrdersAdapter(Context mContext, List<CustomerOrder> customerOrders) {
        this.mContext = mContext;
        this.customerOrders = customerOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_list_orders, parent, false);
        ButterKnife.bind(this, itemView);

        colorPending = ContextCompat.getColor(mContext, R.color.colorPending);
        colorInProgress = ContextCompat.getColor(mContext, R.color.colorInProgress);
        colorCancelled = ContextCompat.getColor(mContext, R.color.colorCancelled);
        colorCompleted = ContextCompat.getColor(mContext, R.color.colorCompleted);
        colorDelivered = ContextCompat.getColor(mContext, R.color.colorDelivered);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CustomerOrder customerOrder = customerOrders.get(position);

        try {
            tvOrderId.setText(customerOrder.getOrderID());
            tvTimestamp.setText(getFormattedDate(Long.valueOf(customerOrder.getTimestamp())));
            tvItemsCount.setText(customerOrder.getItemsCount());
            tvTotalPrice.setText(customerOrder.getTotalPrice());
            String orderStatus = customerOrder.getOrderStatus();
            if (TextUtils.equals(orderStatus, "Pending"))
                viewStatus.setBackgroundColor(colorPending);
            else if (TextUtils.equals(orderStatus, "In progress"))
                viewStatus.setBackgroundColor(colorInProgress);
            else if (TextUtils.equals(orderStatus, "Completed"))
                viewStatus.setBackgroundColor(colorCompleted);
            else if (TextUtils.equals(orderStatus, "Delivered"))
                viewStatus.setBackgroundColor(colorDelivered);
            else if (TextUtils.equals(orderStatus, "Cancelled"))
                viewStatus.setBackgroundColor(colorCancelled);
        } catch (Exception e) {
            // Some values may be null due to data being retrieved before the whole order details are uploaded
            return;
        }

        orderItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetails.class);
                intent.putExtra(ORDER_TAG, customerOrder);
                mContext.startActivity(intent);
            }
        });
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

    @Override
    public int getItemCount() {
        return customerOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
