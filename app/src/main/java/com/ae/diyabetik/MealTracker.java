package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ae.DAO.InformationCallback;
import com.ae.DAO.WaterDAO;
import com.ae.Models.StepCounter;
import com.ae.Models.Water;

import java.util.ArrayList;
import java.util.List;

public class MealTracker extends AppCompatActivity {

    private CardView kahvalti_karti;
    private CardView ogleYemegiKarti;
    private CardView aksamYemegiKarti;
    private CardView araOgunKarti;
    private ImageButton azalt_button;
    private ImageButton arttir_button;
    private TextView sayac_textview;
    private int suSayaci;
    WaterDAO waterDAO = new WaterDAO();
    List<Water> waterList = new ArrayList<>();
    Water water;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        kahvalti_karti = findViewById(R.id.kahvalti_karti);
        ogleYemegiKarti = findViewById(R.id.ogle_yemegi_karti);
        aksamYemegiKarti=findViewById(R.id.aksam_yemegi_karti);
        araOgunKarti=findViewById(R.id.ara_ogun_karti);
        azalt_button=findViewById(R.id.azalt_button);
        arttir_button=findViewById(R.id.arttir_button);
        sayac_textview=findViewById(R.id.sayac_textview);

        waterDAO.read(waterList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                suSayaci = waterList.get(0).getGlassesCount();
            }

            @Override
            public void onInformationNotLoaded() {
                suSayaci = 0;
                saveWaterCount(suSayaci);
            }
        });

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
        azalt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(suSayaci==0){
                    Toast.makeText(MealTracker.this,"Nasıl negatif sayıda bardak su içebilirsiniz? Bence bardağa tükürdünüz",Toast.LENGTH_SHORT).show();
                }else {
                    suSayaci--;
                    sayac_textview.setText(String.valueOf(suSayaci));
                    updateWaterCount(suSayaci);
                }

            }
        });

        arttir_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suSayaci++;
                sayac_textview.setText(String.valueOf(suSayaci));
                updateWaterCount(suSayaci);
            }
        });
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

    private void saveWaterCount(int waterCount){
        waterList = waterDAO.read(waterList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {

            }

            @Override
            public void onInformationNotLoaded() {
                water = new Water(null, waterCount);
                waterDAO.create(water);
            }
        });
    }
    private void updateWaterCount(int waterCount){
        waterDAO.read(waterList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                Water waterToUpdate = new Water(waterList.get(0).getId(), waterCount);
                waterDAO.update(waterToUpdate);
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });


    }
}
