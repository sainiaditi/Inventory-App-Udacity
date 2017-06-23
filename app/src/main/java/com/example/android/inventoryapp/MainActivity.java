package com.example.android.inventoryapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryQuery;


public class MainActivity extends AppCompatActivity {

    private static Context mContext;
    private static InventoryCursorAdapter adapter;
    Cursor cursor;

    public static void onCursorRefresh() {
        Cursor cursor = InventoryQuery.getInstance(mContext).readFromTable(InventoryContract.inventoryTable.TABLE_NAME, null);
        adapter.swapCursor(cursor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        utility();
        addProduct();
    }

    public void addProduct() {
        Button addProductButton = (Button) findViewById(R.id.button);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, EditorActivity.class);
                startActivity(i);
            }
        });
    }

    public void utility() {
        final ListView listView = (ListView) findViewById(R.id.list_view_items);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        cursor = InventoryQuery.getInstance(this).readFromTable(InventoryContract.inventoryTable.TABLE_NAME, null);

        if (InventoryQuery.getInstance(this).readFromTable(InventoryContract.inventoryTable.TABLE_NAME, null) != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    adapter = new InventoryCursorAdapter(MainActivity.this, cursor, 0);
                    listView.setAdapter(adapter);
                }
            });
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }


}

    