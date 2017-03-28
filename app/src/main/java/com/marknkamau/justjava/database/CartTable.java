package com.marknkamau.justjava.database;

public class CartTable {
    public static final String TABLE_NAME = "cart_table";
    public static final String COLUMN_ID = "itemID";
    public static final String COLUMN_NAME = "itemName";
    public static final String COLUMN_QTY = "itemQty";
    public static final String COLUMN_CINNAMON = "itemCinnamon";
    public static final String COLUMN_CHOCOLATE = "itemChoc";
    public static final String COLUMN_MARSHMALLOW = "itemMarshmallow";
    public static final String COLUMN_PRICE = "itemPrice";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID, COLUMN_NAME, COLUMN_QTY, COLUMN_CHOCOLATE, COLUMN_CINNAMON, COLUMN_MARSHMALLOW, COLUMN_PRICE
    };

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " VARCHAR(255),"
            + COLUMN_NAME + " VARCHAR(255),"
            + COLUMN_QTY + " VARCHAR(255),"
            + COLUMN_CINNAMON + " VARCHAR(255),"
            + COLUMN_CHOCOLATE + " VARCHAR(255),"
            + COLUMN_MARSHMALLOW + " VARCHAR(255),"
            + COLUMN_PRICE + " VARCHAR(255)" + ");";

    public static final String CLEAR_TABLE = "DELETE FROM " + TABLE_NAME + ";";

    public static final String DELETE_TABLE = "DROP TABLE " + TABLE_NAME + ";";

}
