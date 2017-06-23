package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.inventoryapp.data.InventoryQuery;

import static com.example.android.inventoryapp.data.InventoryContract.*;


public class InventoryCursorAdapter extends CursorAdapter {


    private LayoutInflater mLayoutInflator;

    public InventoryCursorAdapter(Context context, Cursor c, int flag) {
        super(context, c, flag);
        this.mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflator.inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        LinearLayout listItemView = (LinearLayout) view.findViewById(R.id.listItem);
        TextView productName = (TextView) view.findViewById(R.id.name);
        TextView productQuantity = (TextView) view.findViewById(R.id.quantity);
        TextView price = (TextView) view.findViewById(R.id.summary);
        Button buyButton = (Button) view.findViewById(R.id.decButton);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        final int id = cursor.getInt(cursor.getColumnIndex(inventoryTable._ID));
        final String name = cursor.getString(cursor.getColumnIndex(inventoryTable.COLOUMN_PRODUCT_NAME));
        final int prodQuantity = cursor.getInt(cursor.getColumnIndex(inventoryTable.COLOUMN_QUANTITY));
        final double mPrice = cursor.getDouble(cursor.getColumnIndex(inventoryTable.COLOUMN_PRICE));


        final byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(inventoryTable.COLOUMN_IMAGE_URL));

        productName.setText(name);
        price.setText("Price: " + mPrice);
        productQuantity.setText("Quantity: " + prodQuantity);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = prodQuantity - 1;
                if (quantity < 0) {
                    quantity = 0;
                    Toast.makeText(context, "Item out of stock", Toast.LENGTH_SHORT).show();
                }
                ContentValues values = new ContentValues();
                values.put(inventoryTable.COLOUMN_QUANTITY, quantity);
                String selection = inventoryTable._ID + " = ? ";
                String[] selectionArgs = {String.valueOf(id)};
                InventoryQuery.getInstance(context).updateData(inventoryTable.TABLE_NAME, values, selection, selectionArgs);
                MainActivity.onCursorRefresh();
            }
        });

        Glide.with(context).load(imageBlob).error(R.mipmap.ic_launcher).placeholder(R.drawable.placeholder).into(imageView);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("productName", name);
                bundle.putInt("quantity", prodQuantity);
                bundle.putDouble("price", mPrice);
                bundle.putByteArray("image", imageBlob);
                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
    }
}
