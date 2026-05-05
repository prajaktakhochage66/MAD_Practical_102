package com.example.quickbite;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private EditText etProductName, etQuantity, etMfgDate, etExpDate;
    private Button btnSaveProduct;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etProductName = findViewById(R.id.etProductName);
        etQuantity = findViewById(R.id.etQuantity);
        etMfgDate = findViewById(R.id.etMfgDate);
        etExpDate = findViewById(R.id.etExpDate);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        etMfgDate.setOnClickListener(v -> showDatePicker(etMfgDate));
        etExpDate.setOnClickListener(v -> showDatePicker(etExpDate));

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String mfg = etMfgDate.getText().toString().trim();
        String exp = etExpDate.getText().toString().trim();

        if (name.isEmpty() || qtyStr.isEmpty() || mfg.isEmpty() || exp.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSaveProduct.setEnabled(false);
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> product = new HashMap<>();
        product.put("userId", userId);
        product.put("name", name);
        product.put("quantity", Integer.parseInt(qtyStr));
        product.put("mfgDate", mfg);
        product.put("expDate", exp);

        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    // 1. Show success message using application context (lives beyond activity finish)
                    Toast.makeText(getApplicationContext(), "Product Added Successfully", Toast.LENGTH_LONG).show();

                    // 2. Schedule notification for expiry
                    scheduleNotification(documentReference.getId(), name, exp);

                    // 3. Navigate back with a slight delay to ensure Toast and UI process correctly
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 300);
                })
                .addOnFailureListener(e -> {
                    btnSaveProduct.setEnabled(true);
                    Toast.makeText(AddProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showDatePicker(final EditText editText) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date = year1 + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    editText.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void scheduleNotification(String productId, String productName, String expDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date expDate = sdf.parse(expDateStr);
            if (expDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(expDate);
                calendar.add(Calendar.DAY_OF_YEAR, -4); // 4 days before expiry
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                long triggerTime = calendar.getTimeInMillis();
                if (triggerTime <= System.currentTimeMillis()) {
                    triggerTime = System.currentTimeMillis() + 10000;
                }

                Intent intent = new Intent(this, NotificationReceiver.class);
                intent.putExtra("productName", productName);

                int requestCode = productId.hashCode();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                        } else {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                        }
                    } catch (SecurityException e) {
                        Log.e("AddProduct", "Permission denied for exact alarm", e);
                        // Fallback to non-exact alarm if permission is missing
                        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                    }
                }
            }
        } catch (ParseException e) {
            Log.e("AddProduct", "Date parse failed", e);
        }
    }
}
