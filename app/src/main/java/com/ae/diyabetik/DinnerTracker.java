package com.ae.diyabetik;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DinnerTracker extends AppCompatActivity {
    private ImageView imageViewDinner;
    private RecyclerView foodListDinner;
    private EditText editTextDinner;
    private FloatingActionButton fab;
    private ArrayList<Food> dinnerItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dinner_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // tarasımdaki bileşenlerin tanımlanması
        imageViewDinner = findViewById(R.id.imageViewDinner);
        foodListDinner = findViewById(R.id.foodListDinner);
        editTextDinner = findViewById(R.id.editTextDinner);
        dinnerItemArrayList = new ArrayList<>();
        fab = findViewById(R.id.fab);

        // Recycler view ayarlanması
        foodListDinner.setLayoutManager(new LinearLayoutManager(this));
        foodListDinner.setHasFixedSize(true);
        FoodListAdapter adapter = new FoodListAdapter(dinnerItemArrayList, this);
        foodListDinner.setAdapter(adapter);

        // Kullanıcı girdi yapınca FAB görünür olacak, kullanıcı girsini takip etmek için textwatcherlar ekleniyor
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editTextBreakfast ve editTextPortion doluysa FAB'ı göster
                String foodName = editTextDinner.getText().toString().trim();
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
        editTextDinner.addTextChangedListener(textWatcher);

        // FABa tıklanınca yapılacaklar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = editTextDinner.getText().toString();
                if (!foodName.isEmpty()) {
                    Food food = new Food(foodName);
                    dinnerItemArrayList.add(food);
                    adapter.notifyDataSetChanged();
                    editTextDinner.setText("");
                    fab.hide();
                }
            }
        });
    }
}

