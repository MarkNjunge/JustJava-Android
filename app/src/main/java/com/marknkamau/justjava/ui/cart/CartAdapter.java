package com.marknkamau.justjava.ui.cart;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marknkamau.justjava.R;
import com.marknkamau.justjava.models.CartItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

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
    @BindView(R.id.img_edit)
    ImageView imgEdit;

    private final Context mContext;
    private final List<CartItem> cartItemList;
    private CartAdapterListener CartAdapterListener;

    public CartAdapter(Context mContext, List<CartItem> cartItemList, CartAdapterListener CartAdapterListener) {
        this.mContext = mContext;
        this.cartItemList = cartItemList;
        this.CartAdapterListener = CartAdapterListener;
    }

    public interface CartAdapterListener {
        void updateList();
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
        final CartItem item = cartItemList.get(position);

        tvItemName.setText(item.getItemName());
        tvQuantity.setText(mContext.getString(R.string.quantity) + ": " + item.getItemQty());
        tvPrice.setText(mContext.getString(R.string.price) + " " + item.getItemPrice());
        if (TextUtils.equals(item.getItemCinnamon(), "false"))
            tvCinnamon.setVisibility(View.GONE);
        if (TextUtils.equals(item.getItemChoc(), "false"))
            tvChocolate.setVisibility(View.GONE);
        if (TextUtils.equals(item.getItemMarshmallow(), "false"))
            tvMarshmallow.setVisibility(View.GONE);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    private void showEditDialog(CartItem item) {
        Bundle args = new Bundle();
        args.putParcelable(EditCartDialog.CART_ITEM, item);

        EditCartDialog editCartDialog = new EditCartDialog();
        editCartDialog.setArguments(args);
        editCartDialog.show(((Activity) mContext).getFragmentManager(), "IMAGE_FRAGMENT");
//        editCartDialog.setContext(mContext);

        editCartDialog.setResponseListener(new CartAdapterListener() {
            @Override
            public void updateList() {
                CartAdapterListener.updateList();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
