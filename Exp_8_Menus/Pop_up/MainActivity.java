package com.example.pop_up;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Link the button from XML to Java
        Button btnOpenMenu = findViewById(R.id.btn_open_menu);

        // 2. Set what happens when the button is clicked
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the PopupMenu
                // 'v' is the button itself, so the menu appears next to it
                PopupMenu popup = new PopupMenu(MainActivity.this, v);

                // 3. Add the items from your sketch
                popup.getMenu().add("Reply all");
                popup.getMenu().add("Forward");

                // 4. Set what happens when a menu item is clicked
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Reply all")) {
                            Toast.makeText(MainActivity.this, "Selected: Reply all", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (item.getTitle().equals("Forward")) {
                            Toast.makeText(MainActivity.this, "Selected: Forward", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        return false;
                    }
                });

                // 5. Actually show the menu on the screen
                popup.show();
            }
        });
    }
}