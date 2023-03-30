package com.ae.diyabetik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BreakfastTracker extends AppCompatActivity {
    private EditText searchBarBreakfast;
    private ImageView breakfastImage;
    private ListView foodListBreakfast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakfast_tracker);

        searchBarBreakfast = findViewById(R.id.search_bar_breakfast);
        breakfastImage = findViewById(R.id.breakfast_image);
        foodListBreakfast = findViewById(R.id.food_list_breakfast);

        // ListView'i doldurmak için bir örnek öğeler listesi oluşturun.
        List<FoodItem> items = new ArrayList<>();
        items.add(new FoodItem("Egg", "1"));
        items.add(new FoodItem("Bread", "2 slices"));
        items.add(new FoodItem("Butter", "1 tablespoon"));
        items.add(new FoodItem("Cheese", "30 grams"));

        // Liste öğelerini göstermek için ArrayAdapter kullanın.
        ArrayAdapter<FoodItem> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, R.id.food_name, items);
        foodListBreakfast.setAdapter(adapter);
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
