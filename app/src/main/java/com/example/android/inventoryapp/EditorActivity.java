package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;

import java.io.ByteArrayOutputStream;

import static com.example.android.inventoryapp.data.InventoryQuery.*;

public class EditorActivity extends AppCompatActivity {
    static final int REQUEST_CAMERA = 1;
    ImageView productImage;
    byte[] imageBlob = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        productImage = (ImageView) findViewById(R.id.product_photo);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                productImage.setImageBitmap(image);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBlob = stream.toByteArray();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveItem() {
        final EditText productPrice = (EditText) findViewById(R.id.edit_item_price);
        final EditText productName = (EditText) findViewById(R.id.edit_item_name);
        final EditText productQuantity = (EditText) findViewById(R.id.edit_item_quantity);

        String mProductName = productName.getText().toString();
        if (productName.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Name can\'t be empty", Toast.LENGTH_LONG).show();
            return;
        }

        double mProductPrice = 0.0;
        if (productPrice.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please Enter the price", Toast.LENGTH_LONG).show();
            return;
        } else {
            mProductPrice = Double.parseDouble(productPrice.getText().toString());
        }

        if (imageBlob == null) {
            Toast.makeText(getApplicationContext(), "Product Image required", Toast.LENGTH_LONG).show();
        }

        int mProductQuantity;
        if (productQuantity.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Quantity needed", Toast.LENGTH_LONG).show();
            return;
        } else {
            mProductQuantity = Integer.parseInt(productQuantity.getText().toString());
        }


        ContentValues values = new ContentValues();
        values.put(InventoryContract.inventoryTable.COLOUMN_PRODUCT_NAME, mProductName);
        values.put(InventoryContract.inventoryTable.COLOUMN_QUANTITY, mProductQuantity);
        values.put(InventoryContract.inventoryTable.COLOUMN_PRICE, mProductPrice);
        values.put(InventoryContract.inventoryTable.COLOUMN_IMAGE_URL, imageBlob);
        getInstance(getBaseContext()).insertIntoTable(InventoryContract.inventoryTable.TABLE_NAME, values);
        MainActivity.onCursorRefresh();
        finish();


    }


}
