package com.example.android.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class InventoryQuery {

    private InventoryDbHelper mDbHelper;
    private static InventoryQuery INSTANCE;
    private SQLiteDatabase db;
    private InventoryQuery(Context context) {
        mDbHelper = new InventoryDbHelper(context);
    }

    @org.jetbrains.annotations.Contract("null -> null")
    public static InventoryQuery getInstance(Context context) {
        if (context == null) {
            return null;
        }

        if (INSTANCE == null) {
            INSTANCE = new InventoryQuery(context);
        }
        return INSTANCE;
    }

    public void insertIntoTable(String tableName, ContentValues values) {

        db = mDbHelper.getWritableDatabase();
        db.insert(tableName, null, values);

    }

    public Cursor readFromTable(String tableName, String[] projections) {

        db = mDbHelper.getReadableDatabase();
        return db.query(tableName, projections, null, null, null, null, null);

    }

    public Cursor readFromTable(String tableName, String[] projection, String selection,
                                String[] selectionArgs) {
        if (tableName != null) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            return db.query(tableName, projection, selection, selectionArgs, null, null, null);
        }
        return null;
    }

    public void deleteEntry (String tableName,String selection,String[] selectionArgs) {

        db = mDbHelper.getReadableDatabase();
        db.delete(tableName, selection, selectionArgs);

    }

    public void updateData (String tableName, ContentValues values, String selection, String[] selectionArgs) {

        db = mDbHelper.getReadableDatabase();
        db.update(tableName, values, selection, selectionArgs);

    }
}
