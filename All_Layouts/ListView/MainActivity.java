
package com.example.listview;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ListView l1;
    String[] items = {
            "Puran Poli",
            "Misal Pav",
            "Vada Pav",
            "Pithla Bhakri",
            "Kanda Poha",
            "Sabudana Khichdi",
            "Bharli Vangi",
            "Aamti",
            "Thalipeeth",
            "Sol Kadhi",
            "Modak",
            "Batata Vada",
            "Pav Bhaji",
            "Kothimbir Vadi",
            "Chakli",
            "Shrikhand",
            "Basundi",
            "Pandhra Rassa",
            "Tambada Rassa",
            "Bombil Fry"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        l1 = findViewById(R.id.l1);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        items);

        l1.setAdapter(adapter);
    }

}
