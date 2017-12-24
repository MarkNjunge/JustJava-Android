package com.marknkamau.justjavastaff.ui.main;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marknkamau.justjavastaff.R;
import com.marknkamau.justjavastaff.models.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    @BindView(R.id.view_status)
    View viewStatus;
    @BindView(R.id.tv_timestamp)
    TextView tvTimestamp;
    @BindView(R.id.tv_items_count)
    TextView tvItemsCount;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.order_item)
    ConstraintLayout orderItem;

    private List<Order> items = new ArrayList<>();
    private OnClickListener onClickListener;
    private Context context;

    public OrdersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orders, parent, false);
        ButterKnife.bind(this, view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Order order = items.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, d MMM");

        tvItemsCount.setText(String.valueOf(order.getItemsCount()));
        tvOrderId.setText(order.getOrderId());
        tvTotalPrice.setText(String.valueOf(order.getTotalPrice()));
        tvTimestamp.setText(dateFormat.format(order.getTimestamp()));

        orderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setItems(List<Order> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public void addItem(Order element) {
        items.add(element);
        this.notifyDataSetChanged();
    }

    public void removeItem(Order element) {
        items.remove(element);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    interface OnClickListener {
        void onClick(Order element);
    }
}
