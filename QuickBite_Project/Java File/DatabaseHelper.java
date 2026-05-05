package com.example.quickbite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuickBite.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    // User Table
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PASSWORD = "password";

    // Product Table
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_PROD_ID = "id";
    public static final String COL_PROD_USER_ID = "user_id"; // Associate product with user
    public static final String COL_PROD_NAME = "name";
    public static final String COL_PROD_QUANTITY = "quantity";
    public static final String COL_PROD_MFG_DATE = "mfg_date";
    public static final String COL_PROD_EXP_DATE = "exp_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_PASSWORD + " TEXT)";

        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROD_USER_ID + " INTEGER, " +
                COL_PROD_NAME + " TEXT, " +
                COL_PROD_QUANTITY + " INTEGER, " +
                COL_PROD_MFG_DATE + " TEXT, " +
                COL_PROD_EXP_DATE + " TEXT, " +
                "FOREIGN KEY(" + COL_PROD_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createProductsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_PRODUCTS + " ADD COLUMN " + COL_PROD_USER_ID + " INTEGER");
        }
    }

    // User Registration
    public boolean registerUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // User Login Check
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_USER_ID};
        String selection = COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public Cursor getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + "=?", new String[]{email});
    }

    public boolean updateUser(int id, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        return db.update(TABLE_USERS, values, COL_USER_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Add Product - Now requires userId
    public long addProduct(int userId, String name, int quantity, String mfgDate, String expDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROD_USER_ID, userId);
        values.put(COL_PROD_NAME, name);
        values.put(COL_PROD_QUANTITY, quantity);
        values.put(COL_PROD_MFG_DATE, mfgDate);
        values.put(COL_PROD_EXP_DATE, expDate);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public boolean updateProduct(int id, String name, int quantity, String mfgDate, String expDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROD_NAME, name);
        values.put(COL_PROD_QUANTITY, quantity);
        values.put(COL_PROD_MFG_DATE, mfgDate);
        values.put(COL_PROD_EXP_DATE, expDate);
        return db.update(TABLE_PRODUCTS, values, COL_PROD_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCTS, COL_PROD_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Get All Products for a specific user
    public Cursor getAllProducts(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PROD_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }
}
