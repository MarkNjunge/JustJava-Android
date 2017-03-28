package com.marknkamau.justjava.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.marknkamau.justjava.models.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSource {
    private final DBHelper dbHelper;
    private SQLiteDatabase database;

    public DataSource(Context mContext) {
        dbHelper = new DBHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    private void open() {
        if (!database.isOpen())
            database = dbHelper.getWritableDatabase();
    }

    private void close() {
        database.close();
    }

    public void addToDatabase(CartItem cartItem) {
        open();
        try {
            createItem(cartItem);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        close();
    }

    private void createItem(CartItem cartItem) {
        ContentValues contentValues = cartItem.toValues();
        database.insert(CartTable.TABLE_NAME, null, contentValues);
    }

    public List<CartItem> getDatabaseItems(String sort) {
        open();
        List<CartItem> cartItems = new ArrayList<>();
        Cursor cursor;
        try {
            cursor = database.query(CartTable.TABLE_NAME, CartTable.ALL_COLUMNS, null, null, null, null, sort);
            while (cursor.moveToNext()) {
                CartItem item = new CartItem();
                item.setItemID(cursor.getString(cursor.getColumnIndex(CartTable.COLUMN_ID)));
                item.setItemName(cursor.getString(cursor.getColumnIndex(CartTable.COLUMN_NAME)));
                item.setItemQty(cursor.getString(cursor.getColumnIndex(CartTable.COLUMN_QTY)));
                item.setItemCinnamon(cursor.getString(cursor.getColumnIndex(CartTable.COLUMN_CINNAMON)));
                item.setItemChoc(cursor.getString(cursor.getColumnIndex(CartTable.COLUMN_CHOCOLATE)));
                item.setItemMarshmallow(cursor.getString(cursor.getColumnIndex(CartTable.COLUMN_MARSHMALLOW)));
                item.setItemPrice(cursor.getString(cursor.getColumnIndex(CartTable.COLUMN_PRICE)));

                cartItems.add(item);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return cartItems;
    }

    public void clearTable() {
        open();
        database.execSQL(CartTable.CLEAR_TABLE);
        close();
    }

    public void deleteEntry(String entryID) {
        open();
        String query = "DELETE FROM " + CartTable.TABLE_NAME + " WHERE " + CartTable.COLUMN_ID + "='" + entryID + "';";
        database.execSQL(query);
        close();
    }

    public int getTotalPrice() {
        open();
        String query = "SELECT sum(" + CartTable.COLUMN_PRICE + ") FROM " + CartTable.TABLE_NAME + ";";
        Cursor cursor = database.rawQuery(query, null);
        int total = 0;
        while (cursor.moveToNext()) {
            total = total + cursor.getInt(0);
        }
        cursor.close();
        close();
        return total;
    }

    public void updateCartItem(Map<String, String> data) {
        open();
        String query = "UPDATE " + CartTable.TABLE_NAME +
                " SET " + CartTable.COLUMN_QTY + " = '" + data.get(CartTable.COLUMN_QTY) + "', "
                + CartTable.COLUMN_CINNAMON + " = '" + data.get(CartTable.COLUMN_CINNAMON) + "', "
                + CartTable.COLUMN_CHOCOLATE + " = '" + data.get(CartTable.COLUMN_CHOCOLATE) + "', "
                + CartTable.COLUMN_MARSHMALLOW + " = '" + data.get(CartTable.COLUMN_MARSHMALLOW) + "', "
                + CartTable.COLUMN_PRICE + " = '" + data.get(CartTable.COLUMN_PRICE) +
                "' WHERE " + CartTable.COLUMN_ID + " = '" + data.get(CartTable.COLUMN_ID) + "';";
        database.execSQL(query);
        close();
    }

    public long getItemCount() {
        open();
        long l = DatabaseUtils.queryNumEntries(database, CartTable.TABLE_NAME);
        database.close();
        return l;
    }
}
