package com.example.intents;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button web, call, sms, email, camera, map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.btnWeb);
        call = findViewById(R.id.btnCall);
        sms = findViewById(R.id.btnSms);
        email = findViewById(R.id.btnEmail);
        camera = findViewById(R.id.btnCamera);
        map = findViewById(R.id.btnMap);

        // 1. Open Website
        web.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com"));
            startActivity(i);
        });

        // 2. Make Call
        call.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:9975090434"));
            startActivity(i);
        });

        // 3. Send SMS
        sms.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("sms:9975190434"));
            i.putExtra("sms_body", "Hello!");
            startActivity(i);
        });

        // 4. Send Email
        email.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:pratiksha8767099934@gmail.com"));
            i.putExtra(Intent.EXTRA_SUBJECT, "Test");
            startActivity(i);
        });

        // 5. Open Camera
        camera.setOnClickListener(v -> {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(i);
        });

        // 6. Open Map
        map.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=Kolhapur"));
            startActivity(i);
        });

    }
}