package com.ae.diyabetik;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SnackTracker extends AppCompatActivity {
    private EditText editTextSearchBar;
    private ImageView imageViewSnack;
    private ListView listViewSnacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snack_tracker);

        editTextSearchBar = findViewById(R.id.editTextSearchBar);
        imageViewSnack = findViewById(R.id.imageViewSnack);
        listViewSnacks = findViewById(R.id.listViewSnacks);

        // ListView'i doldurmak için bir örnek öğeler listesi oluşturun.
        List<SnackTracker.FoodItem> items = new ArrayList<>();
        items.add(new SnackTracker.FoodItem("Egg", "1"));
        items.add(new SnackTracker.FoodItem("Bread", "2 slices"));
        items.add(new SnackTracker.FoodItem("Butter", "1 tablespoon"));
        items.add(new SnackTracker.FoodItem("Cheese", "30 grams"));

        // Liste öğelerini göstermek için ArrayAdapter kullanın.
        ArrayAdapter<SnackTracker.FoodItem> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, R.id.food_name, items);
        listViewSnacks.setAdapter(adapter);
    }

    // Özel bir FoodItem sınıfı oluşturun, böylece besin adı ve porsiyon miktarını birlikte tutabilirsiniz.
    private static class FoodItem {
        private String name;
        private String portion;

        public FoodItem(String name, String portion) {
            this.name = name;
            this.portion = portion;
        }

        @Override
        public String toString() {
            return name + " (" + portion + ")";
        }
    }
}
