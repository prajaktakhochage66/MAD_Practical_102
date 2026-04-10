package com.example.calci;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button b1,b2,b3,b4;
    TextView tv;
    EditText e1,e2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);

        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);
        b3=findViewById(R.id.b3);
        b4=findViewById(R.id.b4);

        tv=findViewById(R.id.tv1);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int res = Integer.parseInt(e1.getText().toString()) +
                        Integer.parseInt(e2.getText().toString());

                tv.setText("Addition :"+res);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int res = Integer.parseInt(e1.getText().toString()) -
                        Integer.parseInt(e2.getText().toString());

                tv.setText("Subtraction : " + res);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int res = Integer.parseInt(e1.getText().toString()) *
                        Integer.parseInt(e2.getText().toString());

                tv.setText("Multiplication :"+res);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int res = Integer.parseInt(e1.getText().toString()) /
                        Integer.parseInt(e2.getText().toString());

                tv.setText("Division : " + res);
            }
        });

    }
}