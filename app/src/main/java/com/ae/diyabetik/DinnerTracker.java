package com.ae.diyabetik;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DinnerTracker extends AppCompatActivity {
    private EditText searchBarDinner;
    private ImageView dinnerImage;
    private ListView foodListDinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dinner_tracker);

        searchBarDinner = findViewById(R.id.search_bar_dinner);
        dinnerImage = findViewById(R.id.dinner_image);
        foodListDinner = findViewById(R.id.food_list_dinner);

        // ListView'i doldurmak için bir örnek öğeler listesi oluşturun.
        List<DinnerTracker.FoodItem> items = new ArrayList<>();
        items.add(new DinnerTracker.FoodItem("Egg", "1"));
        items.add(new DinnerTracker.FoodItem("Bread", "2 slices"));
        items.add(new DinnerTracker.FoodItem("Butter", "1 tablespoon"));
        items.add(new DinnerTracker.FoodItem("Cheese", "30 grams"));

        // Liste öğelerini göstermek için ArrayAdapter kullanın.
        ArrayAdapter<DinnerTracker.FoodItem> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, R.id.food_name, items);
        foodListDinner.setAdapter(adapter);
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
