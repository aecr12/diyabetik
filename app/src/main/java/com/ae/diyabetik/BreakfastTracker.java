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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Adapters.BreakfastAdapter;
import com.ae.Adapters.BreakfastAdapterForPastData;
import com.ae.DAO.BreakfastDAO;
import com.ae.DAO.InformationCallback;
import com.ae.Models.Breakfast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BreakfastTracker extends AppCompatActivity {
    private ImageView imageViewBreakfast;
    private RecyclerView recyclerView1;
    private EditText editTextBreakfast;
    private FloatingActionButton fab;
    private ArrayList<Breakfast> breakfastList;
    private ArrayList<Breakfast> breakfastListFilteredByDate;
    private BreakfastAdapter breakfastAdapter;
    private BreakfastDAO breakfastDAO = new BreakfastDAO();
    private BreakfastAdapterForPastData breakfastAdapterForPastData;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String currentDateTime = dateFormat.format(calendar.getTime());

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
        breakfastListFilteredByDate = new ArrayList<>();
        breakfastAdapter = new BreakfastAdapter(breakfastListFilteredByDate, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(breakfastAdapter);
        fab.setVisibility(View.INVISIBLE);

        loadBreakfastDataFilteredByDate();
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

    private void saveBreakfast() {
        String breakfastItemName = editTextBreakfast.getText().toString();
        if (TextUtils.isEmpty(breakfastItemName)) {
            Toast.makeText(BreakfastTracker.this, "Lütfen bir değer giriniz", Toast.LENGTH_LONG);
        }

        Breakfast breakfast = new Breakfast(null, breakfastItemName, currentDateTime);
        breakfastDAO.create(breakfast);
        breakfastList.add(breakfast);
        breakfastListFilteredByDate.add(breakfast);
        breakfastAdapter.notifyDataSetChanged();
        editTextBreakfast.setText("");
    }

    private void loadBreakfastData() {
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

    private void  loadBreakfastDataFilteredByDate(){
        breakfastDAO.readByCurrentDate(breakfastListFilteredByDate, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                breakfastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }


    // geri butonu için menünün inflate edilmesi
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
        } else if (id == R.id.action_pastdata) {
            showBottomSheetDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recViewForPastData);
        breakfastAdapterForPastData = new BreakfastAdapterForPastData(breakfastList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(breakfastAdapterForPastData);
        breakfastAdapterForPastData.notifyDataSetChanged();
        bottomSheetDialog.show();

    }
}