package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.BreakfastDAO;
import com.ae.Models.Breakfast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BreakfastTracker extends AppCompatActivity {
    ImageView imageViewBreakfast;
    RecyclerView recyclerView1;
    EditText editTextBreakfast;
    FloatingActionButton fab;
    private ArrayList<Breakfast> breakfastList;
    BreakfastAdapter breakfastAdapter;
    BreakfastDAO breakfastDAO = new BreakfastDAO();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakfast_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageViewBreakfast = findViewById(R.id.imageViewBreakfast);
        recyclerView1 = findViewById(R.id.recyclerView1);
        editTextBreakfast = findViewById(R.id.editTextBreakfast);
        fab = findViewById(R.id.fab);
        breakfastList = new ArrayList<>();
        breakfastAdapter = new BreakfastAdapter(breakfastList,this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(breakfastAdapter);

        loadBreakfastData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBreakfast();
            }
        });
    }
    private void saveBreakfast(){
        String breakfastItemName = editTextBreakfast.getText().toString();
        if (TextUtils.isEmpty(breakfastItemName)){
            Toast.makeText(BreakfastTracker.this,"Lütfen bir değer giriniz",Toast.LENGTH_LONG);
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateTime = dateFormat.format(calendar.getTime());
        Breakfast breakfast = new Breakfast(null,breakfastItemName,currentDateTime);
        breakfastDAO.create(breakfast);
        breakfastList.add(breakfast);
        breakfastAdapter.notifyDataSetChanged();
        editTextBreakfast.setText("");
    }
    private void loadBreakfastData(){
        breakfastDAO.read(breakfastList,breakfastAdapter);
    }

}
