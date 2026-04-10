package com.example.camera;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    Button cameraBtn, submitBtn;
    ImageView photo;
    RatingBar ratingBar;
    ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {

                            Intent data = result.getData();

                            Bundle extras = data.getExtras();
                            Bitmap image = (Bitmap) extras.get("data");

                            photo.setImageBitmap(image);
                        }
                    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = findViewById(R.id.cameraBtn);
        submitBtn = findViewById(R.id.submitBtn);
        photo = findViewById(R.id.photo);
        ratingBar = findViewById(R.id.ratingBar);

        cameraBtn.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(cameraIntent);
        });
        submitBtn.setOnClickListener(v ->{
            float rating = ratingBar.getRating();

            Toast.makeText(this,
                    "Score: " + rating + " out of 5",
                    Toast.LENGTH_LONG).show();
        });
    }
}