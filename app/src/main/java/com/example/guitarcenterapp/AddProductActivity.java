package com.example.guitarcenterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.guitarcenterapp.Helpers.BitmapHelper;
import com.example.guitarcenterapp.Helpers.DBSQLiteHelper;
import com.example.guitarcenterapp.Models.Product;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddProductActivity extends AppCompatActivity {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GALLERY_INTENT = 2;

    Button btnTakePhoto;
    Button btnAdd;
    Button btnGallery;

    TextInputEditText tiName;
    TextInputEditText tiBrand;
    TextInputEditText tiPrice;
    TextInputEditText tiType;

    ImageView ivImage;

    DBSQLiteHelper dbsqLiteHelper = new DBSQLiteHelper(this);

    Bitmap photo;

    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnTakePhoto = (Button) findViewById(R.id.btnPhoto);
        btnGallery = (Button) findViewById(R.id.btnGallery);

        tiName = (TextInputEditText) findViewById(R.id.tiName);
        tiBrand = (TextInputEditText) findViewById(R.id.tiBrand);
        tiPrice = (TextInputEditText) findViewById(R.id.tiPrice);
        tiType = (TextInputEditText) findViewById(R.id.tiType);

        ivImage = (ImageView) findViewById(R.id.ivProduct);

        verifyStoragePermissions(AddProductActivity.this);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product = new Product();

                product.setName(tiName.getText().toString());
                product.setBrand(tiBrand.getText().toString());
                product.setPrice(Float.valueOf(tiPrice.getText().toString()));
                product.setType(tiType.getText().toString());

                Uri uri = BitmapHelper.getImageUri(getApplicationContext(), photo);

                String path = BitmapHelper.getRealPathFromURI(getApplicationContext(), uri);

                product.setImagePath(path);

                dbsqLiteHelper.addProduct(product);

                Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            ivImage.setImageBitmap(photo);
            try {
                createFile(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri pickedImage = data.getData();
            photo = BitmapHelper.getBitmap(pickedImage, this.getContentResolver());
            ivImage.setImageBitmap(photo);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_INTENT);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void showPhoto(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //pvImage.setImageBitmap(bitmap);
    }

    private void takePhoto(View v) {
        Intent takePictureIntent = new
                Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            showAlert(getString(R.string.error), e.getMessage());
        }
    }

    private void createFile(Bitmap photo) throws IOException {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        String timeStamp = new
                SimpleDateFormat("yyyyMMdd_Hhmmss").format(
                new Date());

        File folder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File file = new File(folder.getPath() + File.separator
                + "JPG_" + timeStamp + ".jpg");

        photo.compress(Bitmap.CompressFormat.PNG, 0, bytes);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes.toByteArray());
        fos.flush();
        fos.close();

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);

        this.sendBroadcast(mediaScanIntent);
    }

    private void showAlert(String title, String message) {
        AlertDialog alertDialog = new
                AlertDialog.Builder(AddProductActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}