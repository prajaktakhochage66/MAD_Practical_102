package com.example.quickbite;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    private EditText etName, etQty, etMfg, etExp;
    private Button btnDelete;
    private FirebaseFirestore db;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        db = FirebaseFirestore.getInstance();
        etName = findViewById(R.id.etEditProductName);
        etQty = findViewById(R.id.etEditQuantity);
        etMfg = findViewById(R.id.etEditMfgDate);
        etExp = findViewById(R.id.etEditExpDate);
        btnDelete = findViewById(R.id.btnDeleteProduct);

        productId = getIntent().getStringExtra("id");
        etName.setText(getIntent().getStringExtra("name"));
        etQty.setText(String.valueOf(getIntent().getIntExtra("quantity", 0)));
        etMfg.setText(getIntent().getStringExtra("mfgDate"));
        etExp.setText(getIntent().getStringExtra("expDate"));

        etMfg.setOnClickListener(v -> showDatePicker(etMfg));
        etExp.setOnClickListener(v -> showDatePicker(etExp));



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProductFromFirebase();
            }
        });
    }

    private void updateProductInFirebase(String name, int quantity, String mfg, String exp) {
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("quantity", quantity);
        product.put("mfgDate", mfg);
        product.put("expDate", exp);

        db.collection("products").document(productId)
                .update(product)
                .addOnSuccessListener(aVoid -> {
                    scheduleNotification(productId, name, exp);
                    Toast.makeText(getApplicationContext(), "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteProductFromFirebase() {
        db.collection("products").document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
                calendar.add(Calendar.DAY_OF_YEAR, -4);
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);

                if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                    Intent intent = new Intent(this, NotificationReceiver.class);
                    intent.putExtra("productName", productName);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, productId.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
