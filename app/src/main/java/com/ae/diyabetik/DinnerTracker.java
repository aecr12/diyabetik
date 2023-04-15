package com.ae.diyabetik;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.DinnerDAO;
import com.ae.DAO.LunchDAO;
import com.ae.Models.Dinner;
import com.ae.Models.Lunch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DinnerTracker extends AppCompatActivity {

    ImageView imageViewDinner;
    RecyclerView recyclerView1;
    EditText editTextDinner;
    FloatingActionButton fab;
    private ArrayList<Dinner> dinnerList;
    DinnerAdapter dinnerAdapter;
    DinnerDAO dinnerDAO = new DinnerDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dinner_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewDinner = findViewById(R.id.imageViewDinner);
        recyclerView1 = findViewById(R.id.recyclerView1);
        editTextDinner = findViewById(R.id.editTextDinner);
        fab = findViewById(R.id.fab);
        dinnerList = new ArrayList<>();
        dinnerAdapter = new DinnerAdapter(dinnerList, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(dinnerAdapter);

        loadDinnerData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDinner();
            }
        });
    }

    private void saveDinner() {
        String dinnerItemName = editTextDinner.getText().toString();
        if (TextUtils.isEmpty(dinnerItemName)) {
            Toast.makeText(DinnerTracker.this, "Lütfen bir değer giriniz", Toast.LENGTH_LONG);
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateTime = dateFormat.format(calendar.getTime());
        Dinner dinner = new Dinner(null, dinnerItemName, currentDateTime);
        dinnerDAO.create(dinner);
        dinnerList.add(dinner);
        dinnerAdapter.notifyDataSetChanged();
        editTextDinner.setText("");
    }

    private void loadDinnerData() {
        dinnerDAO.read(dinnerList, dinnerAdapter);
    }

}

