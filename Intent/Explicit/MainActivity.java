package com.example.resume;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnBack;

        btnBack = findViewById(R.id.button);

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // Explicit Intent — going back to Login page
                Intent intent = new Intent();
                intent.setClassName(
                        "com.example.my_profile",          // package of login app
                        "com.example.my_profile.MainActivity"  // full class name
                );
                startActivity(intent);
                finish(); // closes profile page
            }
        });
    }
}