package com.marknkamau.justjava.activities.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marknkamau.justjava.R;
import com.marknkamau.justjava.models.PreviousOrder;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviousOrderAdapter extends RecyclerView.Adapter<PreviousOrderAdapter.ViewHolder> {

    @BindView(R.id.tv_timestamp)
    TextView tvTimestamp;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_delivery_address)
    TextView tvDeliveryAddress;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;

    private Context mContext;
    private List<PreviousOrder> previousOrders;

    PreviousOrderAdapter(Context mContext, List<PreviousOrder> previousOrders) {
        this.mContext = mContext;
        this.previousOrders = previousOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_previous_order, parent, false);
        ButterKnife.bind(this, itemView);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PreviousOrder order = previousOrders.get(position);

        tvTimestamp.setText(getFormattedDate(Long.valueOf(order.getTimestamp())));
        tvTotalPrice.setText(String.format("%s%S", mContext.getString(R.string.ksh), order.getTotalPrice()));
        tvOrderStatus.setText(order.getOrderStatus());
        tvDeliveryAddress.setText(order.getDeliveryAddress());
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
        return previousOrders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
