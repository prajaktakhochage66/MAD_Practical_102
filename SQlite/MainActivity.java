package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText etRoll, etName, etMarks;
    Button btnInsert, btnUpdate, btnDelete, btnView;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etRoll = findViewById(R.id.etRoll);
        etName = findViewById(R.id.etName);
        etMarks = findViewById(R.id.etMarks);

        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        db = new DBHelper(this);

        // INSERT
        btnInsert.setOnClickListener(v -> {
            boolean result = db.insertData(
                    Integer.parseInt(etRoll.getText().toString()),
                    etName.getText().toString(),
                    Double.parseDouble(etMarks.getText().toString())
            );

            Toast.makeText(this, result ? "Inserted" : "Failed", Toast.LENGTH_SHORT).show();
        });

        // UPDATE
        btnUpdate.setOnClickListener(v -> {
            boolean result = db.updateData(
                    Integer.parseInt(etRoll.getText().toString()),
                    etName.getText().toString(),
                    Double.parseDouble(etMarks.getText().toString())
            );

            Toast.makeText(this, result ? "Updated" : "Failed", Toast.LENGTH_SHORT).show();
        });

        // DELETE
        btnDelete.setOnClickListener(v -> {
            boolean result = db.deleteData(
                    Integer.parseInt(etRoll.getText().toString())
            );

            Toast.makeText(this, result ? "Deleted" : "Failed", Toast.LENGTH_SHORT).show();
        });

        // SELECT (VIEW ALL)
        btnView.setOnClickListener(v -> {
            Cursor cursor = db.getAllData();

            if (cursor.getCount() == 0) {
                showMessage("Error", "No Data Found");
                return;
            }

            StringBuilder sb = new StringBuilder();
            while (cursor.moveToNext()) {
                sb.append("Roll: ").append(cursor.getInt(0)).append("\n");
                sb.append("Name: ").append(cursor.getString(1)).append("\n");
                sb.append("Marks: ").append(cursor.getDouble(2)).append("\n\n");
            }

            showMessage("Student Data", sb.toString());
        });
    }

    // Alert Dialog
    public void showMessage(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}