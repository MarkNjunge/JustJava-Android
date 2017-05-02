package com.marknkamau.justjava.activities.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marknkamau.justjava.activities.drinkdetails.DrinkDetailsActivity;
import com.marknkamau.justjava.R;
import com.marknkamau.justjava.models.CoffeeDrink;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    @BindView(R.id.img_drink_image)
    ImageView imgDrinkImage;
    @BindView(R.id.tv_item_name)
    TextView tvDrinkName;
    @BindView(R.id.tv_short_desc)
    TextView tvDrinkDetails;
    @BindView(R.id.tv_drink_price)
    TextView tvDrinkPrice;
    @BindView(R.id.catalog_item)
    ConstraintLayout catalogItem;

    private final Context mContext;
    private final List<CoffeeDrink> drinkList;
    public static final String DRINK_KEY = "drink_key";

    public CatalogAdapter(Context mContext, List<CoffeeDrink> drinkList) {
        this.mContext = mContext;
        this.drinkList = drinkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_catalog, parent, false);
        ButterKnife.bind(this, itemView);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CoffeeDrink drink = drinkList.get(position);

        tvDrinkName.setText(drink.getDrinkName());
        tvDrinkDetails.setText(drink.getDrinkContents());
        tvDrinkPrice.setText(mContext.getResources().getString(R.string.ksh) + drink.getDrinkPrice());

        String drinkImage = "file:///android_asset/" + drink.getDrinkImage();
        Picasso.with(mContext).load(drinkImage).placeholder(R.drawable.plain_brown).into(imgDrinkImage);

        catalogItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DrinkDetailsActivity.class);
                i.putExtra(DRINK_KEY, drink);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
