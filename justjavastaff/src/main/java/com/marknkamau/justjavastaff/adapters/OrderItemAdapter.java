package com.marknkamau.justjavastaff.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marknkamau.justjavastaff.R;
import com.marknkamau.justjavastaff.models.OrderItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    @BindView(R.id.tv_item_name)
    TextView tvItemName;
    @BindView(R.id.tv_item_price)
    TextView tvPrice;
    @BindView(R.id.tv_item_qty)
    TextView tvQuantity;
    @BindView(R.id.tv_cinnamon)
    TextView tvCinnamon;
    @BindView(R.id.tv_chocolate)
    TextView tvChocolate;
    @BindView(R.id.tv_marshmallows)
    TextView tvMarshmallow;

    private final Context mContext;
    private final List<OrderItem> orderItemList;

    public OrderItemAdapter(Context mContext, List<OrderItem> orderItemList) {
        this.mContext = mContext;
        this.orderItemList = orderItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_cart, parent, false);
        ButterKnife.bind(this, itemView);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OrderItem item = orderItemList.get(position);

        tvItemName.setText(item.getItemName());
        tvQuantity.setText("Quantity: " + item.getItemQty());
        tvPrice.setText("Price " + item.getItemPrice());
        if (TextUtils.equals(item.getItemCinnamon(), "false"))
            tvCinnamon.setVisibility(View.GONE);
        if (TextUtils.equals(item.getItemChoc(), "false"))
            tvChocolate.setVisibility(View.GONE);
        if (TextUtils.equals(item.getItemMarshmallow(), "false"))
            tvMarshmallow.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
