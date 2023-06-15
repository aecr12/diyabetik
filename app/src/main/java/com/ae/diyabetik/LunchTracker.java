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

import com.ae.Adapters.LunchAdapter;
import com.ae.Adapters.LunchAdapterForPastData;
import com.ae.DAO.InformationCallback;
import com.ae.DAO.LunchDAO;
import com.ae.Models.Lunch;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LunchTracker extends AppCompatActivity {

    // kullanılacak bileşenlerin initlaize edilmesi
    private ImageView imageViewLunch;
    private RecyclerView recyclerView1;
    private EditText editTextLunch;
    private FloatingActionButton fab;
    private ArrayList<Lunch> lunchList;
    private ArrayList<Lunch> lunchListFilteredByDate;
    private LunchAdapter lunchAdapter;
    private LunchDAO lunchDAO = new LunchDAO();
    private LunchAdapterForPastData lunchAdapterForPastData;

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
        lunchListFilteredByDate = new ArrayList<>();
        lunchAdapter = new LunchAdapter(lunchListFilteredByDate, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(lunchAdapter);
        fab.setVisibility(View.INVISIBLE);

        // mevcut datanın yüklenmesi
        loadLunchDataFilteredByDate();
        loadLunchData();

        // kullanıcı bilgi girişi yaparsa fab aktif olacak
        editTextLunch.addTextChangedListener(new TextWatcher() {
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
                saveLunch();
            }
        });
    }

    // girilen verilerin listeye ve dbye aktarılması
    private void saveLunch() {
        String lunchItemName = editTextLunch.getText().toString();
        if (TextUtils.isEmpty(lunchItemName)) {
            Toast.makeText(LunchTracker.this, "Lütfen bir değer giriniz", Toast.LENGTH_LONG);
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateTime = dateFormat.format(calendar.getTime());
        Lunch lunch = new Lunch(null, lunchItemName, currentDateTime);
        lunchDAO.create(lunch);
        lunchList.add(lunch);
        lunchListFilteredByDate.add(lunch);
        lunchAdapter.notifyDataSetChanged();
        editTextLunch.setText("");
    }


    // sayfa create olurken sonra verileri yükleyecek fonksiyon
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

    private void  loadLunchDataFilteredByDate(){
        lunchDAO.readByCurrentDate(lunchListFilteredByDate, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                lunchAdapter.notifyDataSetChanged();
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

    // geri butonuna basınca önceki sayfaya gidiliyor
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
        lunchAdapterForPastData = new LunchAdapterForPastData(lunchList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(lunchAdapterForPastData);
        lunchAdapterForPastData.notifyDataSetChanged();
        bottomSheetDialog.show();

    }

}
