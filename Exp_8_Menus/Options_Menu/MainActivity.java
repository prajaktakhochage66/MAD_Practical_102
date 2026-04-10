package com.example.options_menu;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_pizza) {
            Toast.makeText(this, "Pizza selected", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.coffe) {
            Toast.makeText(this, "Coffe selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.pasta) {
            Toast.makeText(this, "Pasta selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.maggie) {
            Toast.makeText(this, "Maggie selected", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}