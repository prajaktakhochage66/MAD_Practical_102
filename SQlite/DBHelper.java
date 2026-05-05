package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Student.db";
    public static final String TABLE_NAME = "student";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(roll INTEGER PRIMARY KEY, name TEXT, marks REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert
    public boolean insertData(int roll, String name, double marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("roll", roll);
        cv.put("name", name);
        cv.put("marks", marks);

        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    // Update
    public boolean updateData(int roll, String name, double marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("marks", marks);

        int result = db.update(TABLE_NAME, cv, "roll=?", new String[]{String.valueOf(roll)});
        return result > 0;
    }

    // Delete
    public boolean deleteData(int roll) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "roll=?", new String[]{String.valueOf(roll)});
        return result > 0;
    }

    // Select
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}