package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public final class InventoryContract {

    public static final String PATH_INVENTORY = "inventory.db";

    public InventoryContract(){
    }

    //Database NAme and Version
    public static final int DATABASE_VERSION = 1;

    public static abstract class inventoryTable implements BaseColumns{

        public static final String TABLE_NAME = "Inventory";
        public static final String COLOUMN_PRODUCT_NAME = "name";
        public static final String COLOUMN_QUANTITY = "quantity";
        public static final String COLOUMN_PRICE = "price";
        public static final String COLOUMN_IMAGE_URL = "imageUrl";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLOUMN_PRODUCT_NAME +
                " TEXT NOT NULL," + COLOUMN_QUANTITY + " INT NOT NULL," + COLOUMN_PRICE +
                " DOUBLE NOT NULL,"  + COLOUMN_IMAGE_URL + " BLOB NOT NULL);";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
