package com.ae.diyabetik;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class LunchTracker extends AppCompatActivity {
    private EditText searchBarLunch;
    private ImageView lunchImage;
    private ListView foodListLunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunch_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchBarLunch = findViewById(R.id.search_bar_lunch);
        lunchImage = findViewById(R.id.lunch_image);
        foodListLunch = findViewById(R.id.food_list_lunch);

        // ListView'i doldurmak için bir örnek öğeler listesi oluşturun.
        List<LunchTracker.FoodItem> items = new ArrayList<>();
        items.add(new LunchTracker.FoodItem("Egg", "1"));
        items.add(new LunchTracker.FoodItem("Bread", "2 slices"));
        items.add(new LunchTracker.FoodItem("Butter", "1 tablespoon"));
        items.add(new LunchTracker.FoodItem("Cheese", "30 grams"));

        // Liste öğelerini göstermek için ArrayAdapter kullanın.
        ArrayAdapter<LunchTracker.FoodItem> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        foodListLunch.setAdapter(adapter);
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

    // geri butonu için menünün inflate edilmesi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
