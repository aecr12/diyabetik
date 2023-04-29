package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.InformationCallback;
import com.ae.DAO.LunchDAO;
import com.ae.Models.Lunch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LunchTracker extends AppCompatActivity {
    ImageView imageViewLunch;
    RecyclerView recyclerView1;
    EditText editTextLunch;
    FloatingActionButton fab;
    private ArrayList<Lunch> lunchList;
    LunchAdapter lunchAdapter;
    LunchDAO lunchDAO = new LunchDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunch_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewLunch = findViewById(R.id.imageViewLunch);
        recyclerView1 = findViewById(R.id.recyclerView1);
        editTextLunch = findViewById(R.id.editTextLunch);
        fab = findViewById(R.id.fab);
        lunchList = new ArrayList<>();
        lunchAdapter = new LunchAdapter(lunchList, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(lunchAdapter);

        loadLunchData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLunch();
            }
        });
    }

    private void saveLunch() {
        String lunchItemName = editTextLunch.getText().toString();
        if (TextUtils.isEmpty(lunchItemName)) {
            Toast.makeText(LunchTracker.this, "Lütfen bir değer giriniz", Toast.LENGTH_LONG);
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateTime = dateFormat.format(calendar.getTime());
        Lunch lunch = new Lunch(null, lunchItemName, currentDateTime);
        lunchDAO.create(lunch);
        lunchList.add(lunch);
        lunchAdapter.notifyDataSetChanged();
        editTextLunch.setText("");
    }

    private void loadLunchData() {
        lunchDAO.read(lunchList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                lunchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }

}
