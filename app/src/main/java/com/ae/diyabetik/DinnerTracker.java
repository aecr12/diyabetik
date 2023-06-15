package com.ae.diyabetik;

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

import com.ae.Adapters.BreakfastAdapterForPastData;
import com.ae.Adapters.DinnerAdapter;
import com.ae.Adapters.DinnerAdapterForPastData;
import com.ae.DAO.DinnerDAO;
import com.ae.DAO.InformationCallback;
import com.ae.Models.Dinner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DinnerTracker extends AppCompatActivity {

    private  ImageView imageViewDinner;
    private  RecyclerView recyclerView1;
    private EditText editTextDinner;
    private FloatingActionButton fab;
    private ArrayList<Dinner> dinnerList;
    private ArrayList<Dinner> dinnerListFilteredByDate;
    private DinnerAdapter dinnerAdapter;
    private DinnerDAO dinnerDAO = new DinnerDAO();
    private DinnerAdapterForPastData dinnerAdapterForPastData;

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
        dinnerListFilteredByDate = new ArrayList<>();
        dinnerAdapter = new DinnerAdapter(dinnerListFilteredByDate, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(dinnerAdapter);
        fab.setVisibility(View.INVISIBLE);
        loadDinnerData();

        editTextDinner.addTextChangedListener(new TextWatcher() {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateTime = dateFormat.format(calendar.getTime());
        Dinner dinner = new Dinner(null, dinnerItemName, currentDateTime);
        dinnerDAO.create(dinner);
        dinnerList.add(dinner);
        dinnerListFilteredByDate.add(dinner);
        dinnerAdapter.notifyDataSetChanged();
        editTextDinner.setText("");
    }

    private void loadDinnerData() {
        dinnerDAO.read(dinnerList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                dinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }

    private void  loadBreakfastDataFilteredByDate(){
        dinnerDAO.readByCurrentDate(dinnerListFilteredByDate, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                dinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // geri butonuna asınca önceki sayfaya gidiliyor
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }else if(id == R.id.action_pastdata){
            showBottomSheetDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recViewForPastData);
        dinnerAdapterForPastData = new DinnerAdapterForPastData(dinnerList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dinnerAdapterForPastData);
        dinnerAdapterForPastData.notifyDataSetChanged();
        bottomSheetDialog.show();

    }
}

