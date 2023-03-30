package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MealTracker extends AppCompatActivity {

    CardView kahvalti_karti;
    CardView ogleYemegiKarti;
    CardView aksamYemegiKarti;
    CardView araOgunKarti;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_tracker);
        kahvalti_karti = findViewById(R.id.kahvalti_karti);
        ogleYemegiKarti = findViewById(R.id.ogle_yemegi_karti);
        aksamYemegiKarti=findViewById(R.id.aksam_yemegi_karti);
        araOgunKarti=findViewById(R.id.ara_ogun_karti);
        kahvalti_karti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealTracker.this,BreakfastTracker.class);
                startActivity(intent);
            }
        });

        ogleYemegiKarti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealTracker.this,LunchTracker.class);
                startActivity(intent);
            }
        });
        aksamYemegiKarti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealTracker.this,DinnerTracker.class);
                startActivity(intent);
            }
        });
        araOgunKarti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealTracker.this,SnackTracker.class);
                startActivity(intent);
            }
        });
    }
}
