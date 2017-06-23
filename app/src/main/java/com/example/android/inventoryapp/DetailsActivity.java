package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import static com.example.android.inventoryapp.data.InventoryContract.*;
import static com.example.android.inventoryapp.data.InventoryQuery.*;


public class DetailsActivity extends AppCompatActivity {

    final Context context = this;
    TextView name;
    TextView price;
    TextView quantity;
    ImageView image;
    // Buttons
    Button incButton;
    Button decButton;
    Button order_item;
    // variables required for intent data
    int mProductId;
    String mProductName;
    int mProductQuantity;
    byte[] mImageUrl;
    double mProductPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = (TextView) findViewById(R.id.name_text_view);
        price = (TextView) findViewById(R.id.price_text_view);
        quantity = (TextView) findViewById(R.id.quantity_text);
        image = (ImageView) findViewById(R.id.product_photo);
        incButton = (Button) findViewById(R.id.inc_button);
        decButton = (Button) findViewById(R.id.dec_button);
        order_item = (Button) findViewById(R.id.order_now_button);

        Intent i = getIntent();
        if (i != null) {
            Bundle bundle = i.getExtras();
            mProductId = bundle.getInt("id");
            mProductName = bundle.getString("productName");
            mProductQuantity = bundle.getInt("quantity");
            mProductPrice = bundle.getDouble("price");
            mImageUrl = bundle.getByteArray("image");
        }

        name.setText(mProductName);
        quantity.setText(" " + mProductQuantity + " ");
        price.setText(" ₹ " + mProductPrice);
        Glide.with(context)
                .load(mImageUrl)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.placeholder)
                .into(image);

        buttonOnClickListener();

        orderItem();

    }

    public void orderItem() {
        order_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Please Ship 50 products of " + mProductName);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
            }
        });
    }

    public void buttonOnClickListener() {

        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductQuantity++;
                quantity.setText(" " + mProductQuantity + " ");
                ContentValues values = new ContentValues();
                values.put(inventoryTable.COLOUMN_QUANTITY, mProductQuantity);
                String selection = inventoryTable._ID + " =  ? ";
                String[] selectionArgs = {String.valueOf(mProductId)};
                getInstance(context).updateData(inventoryTable.TABLE_NAME, values, selection, selectionArgs);
                MainActivity.onCursorRefresh();
            }
        });

        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductQuantity--;
                if (mProductQuantity <= 0) {
                    mProductQuantity = 0;
                    Toast.makeText(getApplicationContext(), "item out of stock", Toast.LENGTH_SHORT).show();
                }
                quantity.setText(" " + mProductQuantity + " ");
                ContentValues values = new ContentValues();
                values.put(inventoryTable.COLOUMN_QUANTITY, mProductQuantity);
                String selection = inventoryTable._ID + " = ? ";
                String[] selectionArgs = {String.valueOf(mProductId)};
                getInstance(context).updateData(inventoryTable.TABLE_NAME, values, selection, selectionArgs);
                MainActivity.onCursorRefresh();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void deleteItem() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete_dialog_msg)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selection = inventoryTable._ID + " = ? ";
                        String[] selectionArgs = {String.valueOf(mProductId)};
                        getInstance(context).deleteEntry(inventoryTable.TABLE_NAME, selection, selectionArgs);
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        MainActivity.onCursorRefresh();
                        finish();
                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
