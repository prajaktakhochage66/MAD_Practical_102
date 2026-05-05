package com.example.quickbite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private Button btnUpdate, btnLogout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        userId = currentUser.getUid();
        etName = findViewById(R.id.etProfileName);
        etEmail = findViewById(R.id.etProfileEmail);
        btnUpdate = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);

        loadUserData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(name);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAndRedirect(LoginActivity.class);
            }
        });
    }

    private void loadUserData() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        etName.setText(documentSnapshot.getString("name"));
                        etEmail.setText(documentSnapshot.getString("email"));
                    }
                })
                .addOnFailureListener(e -> Log.e("ProfileActivity", "Error loading data", e));
    }

    private void updateProfile(String newName) {
        // Change button state to show progress
        btnUpdate.setEnabled(false);
        btnUpdate.setText("Updated");

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", newName);

        // Use set with merge to update Firestore
        db.collection("users").document(userId)
                .set(userMap, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Profile Updated! Redirecting...", Toast.LENGTH_SHORT).show();
                            // Redirect to RegisterActivity as requested
                            logoutAndRedirect(RegisterActivity.class);
                        } else {
                            // On failure, reset the button so the user can try again
                            btnUpdate.setEnabled(true);
                            btnUpdate.setText("Update Profile");
                            
                            String errorMsg = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(ProfileActivity.this, "Update Failed: " + errorMsg, Toast.LENGTH_LONG).show();
                            Log.e("ProfileActivity", "Firestore Update Error", task.getException());
                        }
                    }
                });
    }

    private void logoutAndRedirect(Class<?> destinationActivity) {
        // Sign out from Firebase
        if (mAuth != null) {
            mAuth.signOut();
        }

        // Clear user session from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        // Navigate to the target page and clear the activity history
        Intent intent = new Intent(ProfileActivity.this, destinationActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
