package com.marknkamau.justjava.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.marknkamau.justjava.database.CartTable;

public class CartItem implements Parcelable {
    private String itemID;
    private String itemName;
    private String itemQty;
    private String itemPrice;
    private String itemCinnamon;
    private String itemChoc;
    private String itemMarshmallow;

    public CartItem() {
    }

    public CartItem(String itemID, String itemName, String itemQty, String itemCinnamon, String itemChoc, String itemMarshmallow, String itemPrice) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemCinnamon = itemCinnamon;
        this.itemChoc = itemChoc;
        this.itemMarshmallow = itemMarshmallow;
        this.itemPrice = itemPrice;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemCinnamon(String itemCinnamon) {
        this.itemCinnamon = itemCinnamon;
    }

    public void setItemChoc(String itemChoc) {
        this.itemChoc = itemChoc;
    }

    public void setItemMarshmallow(String itemMarshmallow) {
        this.itemMarshmallow = itemMarshmallow;
    }

    public String getItemID() {
        return itemID;
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

    public ContentValues toValues() {
        ContentValues values = new ContentValues(5);

        values.put(CartTable.COLUMN_ID, itemID);
        values.put(CartTable.COLUMN_NAME, itemName);
        values.put(CartTable.COLUMN_QTY, itemQty);
        values.put(CartTable.COLUMN_CINNAMON, itemCinnamon);
        values.put(CartTable.COLUMN_CHOCOLATE, itemChoc);
        values.put(CartTable.COLUMN_MARSHMALLOW, itemMarshmallow);
        values.put(CartTable.COLUMN_PRICE, itemPrice);

        return values;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "itemID='" + itemID + '\'' +
                ", itemName='" + itemName + '\'' +
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
        dest.writeString(this.itemID);
        dest.writeString(this.itemName);
        dest.writeString(this.itemQty);
        dest.writeString(this.itemPrice);
        dest.writeString(this.itemCinnamon);
        dest.writeString(this.itemChoc);
        dest.writeString(this.itemMarshmallow);
    }

    private CartItem(Parcel in) {
        this.itemID = in.readString();
        this.itemName = in.readString();
        this.itemQty = in.readString();
        this.itemPrice = in.readString();
        this.itemCinnamon = in.readString();
        this.itemChoc = in.readString();
        this.itemMarshmallow = in.readString();
    }

    public static final Parcelable.Creator<CartItem> CREATOR = new Parcelable.Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel source) {
            return new CartItem(source);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
