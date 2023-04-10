package com.ae.diyabetik;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class BreakfastTracker extends AppCompatActivity {
    private EditText editTextBreakfast;
    private ImageView breakfastImage;
    private RecyclerView foodListBreakfast;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Food> breakfastItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakfast_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        breakfastItemList = new ArrayList<>();
        editTextBreakfast = findViewById(R.id.editTextBreakfast);
        breakfastImage = findViewById(R.id.breakfast_image);
        foodListBreakfast = findViewById(R.id.foodListBreakfast);
        floatingActionButton = findViewById(R.id.fab);
        // RecyclerView'ı ayarla
        foodListBreakfast.setLayoutManager(new LinearLayoutManager(this));
        foodListBreakfast.setHasFixedSize(true);
        FoodListAdapter adapter1 = new FoodListAdapter(breakfastItemList,this);
        foodListBreakfast.setAdapter(adapter1);
        // Kullanıcı girdi yapınca FAB görünür olacak, kullanıcı girsini takip etmek için textwatcherlar ekleniyor
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editTextBreakfast ve editTextPortion doluysa FAB'ı göster
                String foodName = editTextBreakfast.getText().toString().trim();
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
        editTextBreakfast.addTextChangedListener(textWatcher);
        // FABa tıklanınca yapılacaklar
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = editTextBreakfast.getText().toString();

                if (!foodName.isEmpty()) {
                    Food food = new Food(foodName);
                    breakfastItemList.add(food);
                    adapter1.notifyDataSetChanged();
                    editTextBreakfast.setText("");
                    floatingActionButton.hide();
                }
            }
        });
    }
}


