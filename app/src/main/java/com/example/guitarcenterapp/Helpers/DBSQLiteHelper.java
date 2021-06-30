package com.example.guitarcenterapp.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.guitarcenterapp.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class DBSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ProductsDB";
    private static final String PRODUCTS_TABLE = "products";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String BRAND = "BRAND";
    private static final String PRICE = "PRICE";
    private static final String IMAGE = "IMAGE_PATH";
    private static final String TYPE = "TYPE";
    private static final String SOLD = "SOLD";


    private final String[] COLUMNS = { ID, NAME, BRAND, PRICE, TYPE, SOLD, IMAGE };

    private Context context;

    public DBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + PRODUCTS_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT," +
                BRAND + " TEXT," +
                PRICE + " DECIMAL," +
                TYPE + " TEXT," +
                SOLD + " INTEGER," +
                IMAGE + " TEXT )";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS products");
        this.onCreate(db);
    }

    public void addProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, product.getName());
        contentValues.put(BRAND, product.getBrand());
        contentValues.put(PRICE, product.getPrice());
        contentValues.put(TYPE, product.getType());
        contentValues.put(SOLD, product.isSold());
        contentValues.put(IMAGE, product.getImagePath());

        db.insertOrThrow(PRODUCTS_TABLE, null, contentValues);

        db.close();
    }

    public Product getProduct(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(PRODUCTS_TABLE, COLUMNS, "id = ?", new String[] { String.valueOf(id) } , null, null, null);

        if(cursor == null)
            return null;
        else {
            cursor.moveToFirst();

            Product product = cursorToProduct(cursor);

            return product;
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + PRODUCTS_TABLE + " ORDER BY " + NAME;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {

            do {
                Product product = cursorToProduct(cursor);
                products.add(product);

            } while(cursor.moveToNext());

        }

        return products;
    }

    public List<Product> getAllProducts(String filter) {
        List<Product> products = new ArrayList<Product>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query;

        if(filter.equals(""))
            query = "SELECT * FROM " + PRODUCTS_TABLE + " ORDER BY " + NAME;

        else
            query = "SELECT * FROM " + PRODUCTS_TABLE + " WHERE "+ TYPE + " = " + "'" + filter + "'" + " ORDER BY " + NAME;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {

            do {
                Product product = cursorToProduct(cursor);
                products.add(product);

            } while(cursor.moveToNext());

        }

        return products;
    }


    public int updateProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, product.getId());
        contentValues.put(NAME, product.getName());
        contentValues.put(BRAND, product.getBrand());
        contentValues.put(PRICE, product.getPrice());
        contentValues.put(TYPE, product.getType());
        contentValues.put(SOLD, product.isSold());
        contentValues.put(IMAGE, product.getImagePath());

        int i = db.update(PRODUCTS_TABLE,
                contentValues, ID + " = ?",
                new String[]{ String.valueOf(product.getId()) });

        db.close();

        return i;
    }

    public int deleteProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(PRODUCTS_TABLE, ID + " = ?", new String[]{ String.valueOf(product.getId()) } );

        db.close();

        return i;
    }

    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();

        product.setId(cursor.getInt(0));
        product.setName(cursor.getString(1));
        product.setBrand(cursor.getString(2));
        product.setPrice(cursor.getFloat(3));
        product.setType(cursor.getString(4));
        product.setSold(cursor.getInt(5) == 1);
        product.setImagePath(cursor.getString(6));

        return product;
    }


}