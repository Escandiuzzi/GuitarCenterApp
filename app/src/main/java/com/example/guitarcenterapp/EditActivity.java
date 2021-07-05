package com.example.guitarcenterapp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.example.guitarcenterapp.Helpers.BitmapHelper;
import com.example.guitarcenterapp.Helpers.DBSQLiteHelper;
import com.example.guitarcenterapp.Models.Product;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.guitarcenterapp.databinding.ActivityEditBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int GALLERY_INTENT = 2;

    Button btnTakePhoto;
    Button btnGallery;
    Button btnSell;
    Button btnEdit;
    Button btnDelete;

    TextInputEditText tiName;
    TextInputEditText tiBrand;
    TextInputEditText tiPrice;

    Spinner editType;

    ImageView pvImage;

    DBSQLiteHelper dbsqLiteHelper;

    Bitmap photo;

    Product product;

    Boolean isSold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        isSold = false;

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnSell = (Button) findViewById(R.id.btnSell);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnTakePhoto = (Button) findViewById(R.id.btnEditPhoto);
        btnGallery = (Button) findViewById(R.id.btnEditGallery);

        tiName = (TextInputEditText) findViewById(R.id.editName);
        tiBrand = (TextInputEditText) findViewById(R.id.editBrand);
        tiPrice = (TextInputEditText) findViewById(R.id.editPrice);

        editType = (Spinner) findViewById(R.id.editType);

        pvImage = (ImageView) findViewById(R.id.editProductImage);

        dbsqLiteHelper = new DBSQLiteHelper(this.getBaseContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.products_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editType.setAdapter(adapter);

        int id = getIntent().getIntExtra("ID", -1);

        if(id > 0) {
            product = dbsqLiteHelper.getProduct(id);

            tiName.setText(product.getName());
            tiBrand.setText(product.getBrand());
            tiPrice.setText(String.valueOf(product.getPrice()));

            int spinnerPosition = adapter.getPosition(product.getType());

            editType.setSelection(spinnerPosition);

            pvImage.setImageBitmap(BitmapHelper.getBitmapFromPath(product.getImagePath()));
        }

        dbsqLiteHelper = new DBSQLiteHelper(this);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSold = true;
                update();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext()).setMessage(R.string.confirmDelete)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbsqLiteHelper.deleteProduct(product.getId());

                                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();

            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { takePhoto(v); }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void update() {
        product.setName(tiName.getText().toString());
        product.setBrand(tiBrand.getText().toString());
        product.setPrice(Float.valueOf(tiPrice.getText().toString()));
        product.setType(editType.getSelectedItem().toString());
        product.setSold(isSold);

        if(photo != null) {
            Uri uri = BitmapHelper.getImageUri(getApplicationContext(), photo);

            String path = BitmapHelper.getRealPathFromURI(getApplicationContext(), uri);

            product.setImagePath(path);
        }

        dbsqLiteHelper.updateProduct(product);

        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            pvImage.setImageBitmap(photo);
            try {
                createFile(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri pickedImage = data.getData();
            photo = getBitmap(pickedImage, this.getContentResolver());
            pvImage.setImageBitmap(Bitmap.createScaledBitmap(photo, 200, 250, false));
        }

    }

    private Bitmap getBitmap(Uri file, ContentResolver cr) {

        Bitmap bitmap = null;

        try {
            InputStream inputStream = cr.openInputStream(file);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException ex){}

        return bitmap;
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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_INTENT);
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
                AlertDialog.Builder(EditActivity.this).create();
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