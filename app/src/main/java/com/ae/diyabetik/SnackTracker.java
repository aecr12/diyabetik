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

public class SnackTracker extends AppCompatActivity {

    private EditText editTextSnack;
    private ImageView imageViewSnack;
    private RecyclerView foodListSnack;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Food> snackItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snack_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        snackItemList = new ArrayList<>();
        editTextSnack = findViewById(R.id.editTextSnack);
        imageViewSnack = findViewById(R.id.imageViewSnack);
        foodListSnack = findViewById(R.id.foodListSnack);
        floatingActionButton = findViewById(R.id.fab);
        // RecyclerView'ı ayarla
        foodListSnack.setLayoutManager(new LinearLayoutManager(this));
        foodListSnack.setHasFixedSize(true);
        FoodListAdapter adapter1 = new FoodListAdapter(snackItemList, this);
        foodListSnack.setAdapter(adapter1);
        // Kullanıcı girdi yapınca FAB görünür olacak, kullanıcı girsini takip etmek için textwatcherlar ekleniyor
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editTextBreakfast ve editTextPortion doluysa FAB'ı göster
                String foodName = editTextSnack.getText().toString().trim();
                if (!foodName.isEmpty()) {
                    floatingActionButton.show();
                } else {
                    floatingActionButton.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editTextSnack.addTextChangedListener(textWatcher);
        // FABa tıklanınca yapılacaklar
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = editTextSnack.getText().toString();

                if (!foodName.isEmpty()) {
                    Food food = new Food(foodName);
                    snackItemList.add(food);
                    adapter1.notifyDataSetChanged();
                    editTextSnack.setText("");
                    floatingActionButton.hide();
                }
            }
        });
    }
}

