package com.marknkamau.justjavastaff.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable {
    private String itemName;
    private String itemQty;
    private String itemPrice;
    private String itemCinnamon;
    private String itemChoc;
    private String itemMarshmallow;

    public OrderItem(String itemName, String itemQty, String itemCinnamon, String itemChoc, String itemMarshmallow, String itemPrice) {
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemCinnamon = itemCinnamon;
        this.itemChoc = itemChoc;
        this.itemMarshmallow = itemMarshmallow;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQty() {
        return itemQty;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemCinnamon() {
        return itemCinnamon;
    }

    public String getItemChoc() {
        return itemChoc;
    }

    public String getItemMarshmallow() {
        return itemMarshmallow;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "itemName='" + itemName + '\'' +
                ", itemQty='" + itemQty + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", itemCinnamon='" + itemCinnamon + '\'' +
                ", itemChoc='" + itemChoc + '\'' +
                ", itemMarshmallow='" + itemMarshmallow + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.itemQty);
        dest.writeString(this.itemPrice);
        dest.writeString(this.itemCinnamon);
        dest.writeString(this.itemChoc);
        dest.writeString(this.itemMarshmallow);
    }

    private OrderItem(Parcel in) {
        this.itemName = in.readString();
        this.itemQty = in.readString();
        this.itemPrice = in.readString();
        this.itemCinnamon = in.readString();
        this.itemChoc = in.readString();
        this.itemMarshmallow = in.readString();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel source) {
            return new OrderItem(source);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}
