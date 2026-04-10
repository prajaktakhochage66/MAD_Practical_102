package com.example.prac_7;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ProgressBar horizontalProgress, circularProgress;
    TextView txtProgress, txtDone;
    RatingBar ratingBar;

    int progressStatus = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horizontalProgress = findViewById(R.id.horizontalProgress);
        circularProgress = findViewById(R.id.circularProgress);
        txtProgress = findViewById(R.id.txtProgress);
        txtDone = findViewById(R.id.txtDone);
        ratingBar = findViewById(R.id.ratingBar);

        // ⭐ Rating popup
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            Toast.makeText(MainActivity.this, "Rating: " + rating, Toast.LENGTH_SHORT).show();
        });

        startDownloading();
    }

    private void startDownloading() {
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 2;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(() -> {
                    horizontalProgress.setProgress(progressStatus);
                    txtProgress.setText(progressStatus + "%");
                });
            }

            handler.post(() -> {
                circularProgress.setVisibility(View.GONE);
                txtDone.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
            });

        }).start();
    }
}