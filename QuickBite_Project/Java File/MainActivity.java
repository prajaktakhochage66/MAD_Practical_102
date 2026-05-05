package com.example.quickbite;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private List<Product> filteredList;
    private FloatingActionButton fabAdd;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentFilter = "All";
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createNotificationChannel();
        requestNotificationPermission();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddProductActivity.class));
            }
        });

        loadProductsFromFirebase();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Expiry Notifications";
            String description = "Channel for Product Expiry Reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("expiry_notifications", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductsFromFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter_all) {
            currentFilter = "All";
            applyFilter();
            return true;
        } else if (id == R.id.action_filter_near) {
            currentFilter = "Near Expiry";
            applyFilter();
            return true;
        } else if (id == R.id.action_filter_expired) {
            currentFilter = "Expired";
            applyFilter();
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProductsFromFirebase() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("products")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = new Product(
                                    document.getId(),
                                    document.getString("name"),
                                    document.getLong("quantity").intValue(),
                                    document.getString("mfgDate"),
                                    document.getString("expDate")
                            );
                            productList.add(product);
                        }
                        applyFilter();
                    } else {
                        Toast.makeText(MainActivity.this, "Error fetching products", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void applyFilter() {
        filteredList.clear();
        if (currentFilter.equals("All")) {
            filteredList.addAll(productList);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar calToday = Calendar.getInstance();
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            calToday.set(Calendar.MILLISECOND, 0);
            long todayMillis = calToday.getTimeInMillis();

            for (Product p : productList) {
                try {
                    Date expDate = sdf.parse(p.getExpDate());
                    Calendar calExp = Calendar.getInstance();
                    calExp.setTime(expDate);
                    calExp.set(Calendar.HOUR_OF_DAY, 0);
                    calExp.set(Calendar.MINUTE, 0);
                    calExp.set(Calendar.SECOND, 0);
                    calExp.set(Calendar.MILLISECOND, 0);

                    long diff = calExp.getTimeInMillis() - todayMillis;
                    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                    if (currentFilter.equals("Expired") && days < 0) {
                        filteredList.add(p);
                    } else if (currentFilter.equals("Near Expiry") && days >= 0 && days <= 4) {
                        filteredList.add(p);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        adapter = new ProductAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }
}
