package com.example.lab12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect activity with XML layout
        setContentView(R.layout.activity_main);

        b1=findViewById(R.id.btnBack);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClassName("com.example.labs",
                        "com.example.labs.MainActivity");
                startActivity(intent);

            }
        });
    }
}