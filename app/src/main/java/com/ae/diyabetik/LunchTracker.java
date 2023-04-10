package com.ae.diyabetik;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class LunchTracker extends AppCompatActivity {
    private ImageView imageViewLunch;
    private RecyclerView foodListLunch;
    private EditText editTextLunch;
    private FloatingActionButton fab;
    private ArrayList<Food> lunchItemArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunch_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // tarasımdaki bileşenlerin tanımlanması
        imageViewLunch = findViewById(R.id.imageViewLunch);
        foodListLunch = findViewById(R.id.foodListLunch);
        editTextLunch = findViewById(R.id.editTextLunch);
        lunchItemArrayList = new ArrayList<>();
        fab = findViewById(R.id.fab);

        // Recycler view ayarlanması
        foodListLunch.setLayoutManager(new LinearLayoutManager(this));
        foodListLunch.setHasFixedSize(true);
        FoodListAdapter adapter = new FoodListAdapter(lunchItemArrayList,this);
        foodListLunch.setAdapter(adapter);

        // Kullanıcı girdi yapınca FAB görünür olacak, kullanıcı girsini takip etmek için textwatcherlar ekleniyor
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editTextBreakfast ve editTextPortion doluysa FAB'ı göster
                String foodName = editTextLunch.getText().toString().trim();
                if (!foodName.isEmpty()) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editTextLunch.addTextChangedListener(textWatcher);

        // FABa tıklanınca yapılacaklar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = editTextLunch.getText().toString();
                if (!foodName.isEmpty()) {
                    Food food = new Food(foodName);
                    lunchItemArrayList.add(food);
                    adapter.notifyDataSetChanged();
                    editTextLunch.setText("");
                    fab.hide();
                }
            }
        });
    }
}
