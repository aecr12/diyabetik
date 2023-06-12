package com.ae.diyabetik;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Adapters.BreakfastAdapter;
import com.ae.DAO.BreakfastDAO;
import com.ae.DAO.InformationCallback;
import com.ae.Models.Breakfast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        fab.setVisibility(View.INVISIBLE);

        loadBreakfastData();

        editTextBreakfast.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
        breakfastDAO.read(breakfastList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                breakfastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }

}