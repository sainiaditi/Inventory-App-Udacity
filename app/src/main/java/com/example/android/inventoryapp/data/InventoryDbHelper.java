package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class InventoryDbHelper extends SQLiteOpenHelper {

    private Context context;

    public InventoryDbHelper(Context context) {
        super(context, InventoryContract.PATH_INVENTORY, null, InventoryContract.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(InventoryContract.inventoryTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(InventoryContract.inventoryTable.DELETE_TABLE);
        onCreate(database);
    }
}
