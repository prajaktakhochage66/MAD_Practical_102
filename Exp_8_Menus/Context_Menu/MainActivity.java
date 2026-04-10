package com.example.context_menu;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        // Register for Context Menu (IMPORTANT)
        registerForContextMenu(textView);
    }

    // Create Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context, menu);
    }

    // Handle Item Click
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.edit) {
            Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Delete clicked", Toast.LENGTH_SHORT).show();

            return true;

        } else if (item.getItemId() == R.id.share) {
            Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show();
            return true;

            } else if (item.getItemId() == R.id.settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;

        }

        return super.onContextItemSelected(item);
    }
}