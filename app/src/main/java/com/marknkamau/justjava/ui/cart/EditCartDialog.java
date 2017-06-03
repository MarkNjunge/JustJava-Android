package com.marknkamau.justjava.ui.cart;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marknkamau.justjava.R;
import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.models.CoffeeDrink;
import com.marknkamau.justjava.models.DataProvider;
import com.marknkamau.justjava.utils.RealmUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCartDialog extends DialogFragment {

    public static final String CART_ITEM = "item_cart";
    @BindView(R.id.tv_drink_name)
    TextView tvDrinkName;
    @BindView(R.id.img_minus_qty)
    ImageView imgMinusQty;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.img_add_qty)
    ImageView imgAddQty;
    @BindView(R.id.tv_cinnamon)
    TextView tvCinnamon;
    @BindView(R.id.tv_chocolate)
    TextView tvChocolate;
    @BindView(R.id.tv_marshmallows)
    TextView tvMarshmallows;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.img_save)
    ImageView imgSave;

    private int quantity;
    private CartItem item;
    private RealmUtils realmUtils;
    private static final int PADDING = 24;
    private CartAdapter.CartAdapterListener cartResponse = null;
    private boolean withCinnamon = false, withChocolate = false, withMarshmallow = false;

    public void setResponseListener(CartAdapter.CartAdapterListener response) {
        cartResponse = response;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment, container, false);
        ButterKnife.bind(this, view);
        realmUtils = new RealmUtils();

        Bundle args = getArguments();

        item = null;
        if (args != null) {
            item = args.getParcelable(CART_ITEM);
        }

        tvCinnamon.setPadding(PADDING, PADDING, PADDING, PADDING);
        tvChocolate.setPadding(PADDING, PADDING, PADDING, PADDING);
        tvMarshmallows.setPadding(PADDING, PADDING, PADDING, PADDING);

        if (item != null) {
            tvDrinkName.setText(item.getItemName());
            quantity = Integer.parseInt(item.getItemQty());
            tvQuantity.setText(String.valueOf(quantity));
            if (TextUtils.equals(item.getItemCinnamon(), "true")) {
                setToppingOn(tvCinnamon);
                withCinnamon = true;
            }
            if (TextUtils.equals(item.getItemChoc(), "true")) {
                setToppingOn(tvChocolate);
                withChocolate = true;
            }
            if (TextUtils.equals(item.getItemMarshmallow(), "true")) {
                setToppingOn(tvMarshmallows);
                withMarshmallow = true;
            }
            tvTotal.setText(getString(R.string.ksh) + item.getItemPrice());
        } else {
            dismiss();
        }
        return view;
    }

    @OnClick({R.id.img_minus_qty, R.id.img_add_qty, R.id.tv_cinnamon, R.id.tv_chocolate, R.id.tv_marshmallows, R.id.img_delete, R.id.img_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_minus_qty:
                minusQty();
                break;
            case R.id.img_add_qty:
                addQty();
                break;
            case R.id.tv_cinnamon:
                switchCinnamon(withCinnamon);
                updateSubtotal();
                break;
            case R.id.tv_chocolate:
                switchChocolate(withChocolate);
                updateSubtotal();
                break;
            case R.id.tv_marshmallows:
                switchMarshmallow(withMarshmallow);
                updateSubtotal();
                break;
            case R.id.img_delete:
                realmUtils.deleteSingleItem(item);
                cartResponse.updateList();
                dismiss();
                break;
            case R.id.img_save:
                dismiss();
                saveChanges();
                break;
        }
    }

    private void saveChanges() {
        realmUtils.saveEdit(new CartItem(item.getItemID(),
                item.getItemName(),
                String.valueOf(quantity),
                String.valueOf(withCinnamon),
                String.valueOf(withChocolate),
                String.valueOf(withMarshmallow),
                updateSubtotal()));
        cartResponse.updateList();
    }

    private void setToppingOn(TextView textView) {
        textView.setBackgroundResource(R.drawable.topping_on);
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorToppingOnText));
    }

    private void setToppingOff(TextView textView) {
        textView.setBackgroundResource(R.drawable.topping_off);
        textView.setPadding(PADDING, PADDING, PADDING, PADDING);
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorToppingOffText));
    }

    private void minusQty() {
        if (quantity > 1) {
            quantity -= 1;
        }
        tvQuantity.setText(String.valueOf(quantity));
        updateSubtotal();
    }

    private void addQty() {
        quantity += 1;
        tvQuantity.setText(String.valueOf(quantity));
        updateSubtotal();
    }


    @SuppressLint("SetTextI18n")
    private int updateSubtotal() {
        List<CoffeeDrink> drinks = DataProvider.drinksList;

        int base = 0;
        for (CoffeeDrink drink : drinks) {
            if (TextUtils.equals(drink.getDrinkName(), item.getItemName())) {
                base = Integer.parseInt(drink.getDrinkPrice());
            }
        }

        base = base * quantity;
        if (withCinnamon) {
            base = base + (quantity * 100);
        }
        if (withChocolate) {
            base = base + (quantity * 100);
        }
        if (withMarshmallow) {
            base = base + (quantity * 100);
        }
        tvTotal.setText(getResources().getString(R.string.ksh) + base);
        return base;
    }

    private void switchCinnamon(Boolean selected) {
        if (selected) {
            setToppingOff(tvCinnamon);
            withCinnamon = false;
        } else {
            setToppingOn(tvCinnamon);
            withCinnamon = true;
        }
    }

    private void switchChocolate(Boolean selected) {
        if (selected) {
            setToppingOff(tvChocolate);
            withChocolate = false;
        } else {
            setToppingOn(tvChocolate);
            withChocolate = true;
        }
    }

    private void switchMarshmallow(Boolean selected) {
        if (selected) {
            setToppingOff(tvMarshmallows);
            withMarshmallow = false;
        } else {
            setToppingOn(tvMarshmallows);
            withMarshmallow = true;
        }
    }
}
