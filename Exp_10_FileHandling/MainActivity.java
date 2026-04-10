package com.example.prac_10;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button saveBtn, loadBtn;

    String fileName = "notes.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        saveBtn = findViewById(R.id.saveBtn);
        loadBtn = findViewById(R.id.loadBtn);

        saveBtn.setOnClickListener(v -> {
            try {
                String data = editText.getText().toString();

                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
                fos.write(data.getBytes());
                fos.close();

                Toast.makeText(this, "File Saved Successfully", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(this, "Error Saving File", Toast.LENGTH_SHORT).show();
            }
        });


        loadBtn.setOnClickListener(v -> {
            try {
                FileInputStream fis = openFileInput(fileName);

                int c;
                StringBuilder temp = new StringBuilder();

                while ((c = fis.read()) != -1) {
                    temp.append((char) c);
                }

                fis.close();
                editText.setText(temp.toString());

                Toast.makeText(this, "File Loaded", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(this, "Error Loading File", Toast.LENGTH_SHORT).show();
            }
        });
    }
}