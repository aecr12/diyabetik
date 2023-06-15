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
import com.ae.Adapters.SnackAdapter;
import com.ae.Adapters.SnackAdapterForPastData;
import com.ae.DAO.InformationCallback;
import com.ae.DAO.SnackDAO;
import com.ae.Models.Snack;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SnackTracker extends AppCompatActivity {

    private ImageView imageViewSnack;
    private RecyclerView recyclerView1;
    private EditText editTextSnack;
    private FloatingActionButton fab;
    private ArrayList<Snack> snackList;
    private ArrayList<Snack> snackListFilteredByDate;
    private SnackAdapter snackAdapter;
    private SnackDAO snackDAO = new SnackDAO();
    private SnackAdapterForPastData snackAdapterForPastData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snack_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewSnack = findViewById(R.id.imageViewSnack);
        recyclerView1 = findViewById(R.id.recyclerView1);
        editTextSnack = findViewById(R.id.editTextSnack);
        fab = findViewById(R.id.fab);
        snackList = new ArrayList<>();
        snackListFilteredByDate = new ArrayList<>();
        snackAdapter = new SnackAdapter(snackListFilteredByDate, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(snackAdapter);
        fab.setVisibility(View.INVISIBLE);
        loadSnackDataFilteredByDate();
        loadSnackData();

        editTextSnack.addTextChangedListener(new TextWatcher() {
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
                saveSnack();
            }
        });
    }

    private void saveSnack() {
        String snackItemName = editTextSnack.getText().toString();
        if (TextUtils.isEmpty(snackItemName)) {
            Toast.makeText(SnackTracker.this, "Lütfen bir değer giriniz", Toast.LENGTH_LONG);
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateTime = dateFormat.format(calendar.getTime());
        Snack snack = new Snack(null, snackItemName, currentDateTime);
        snackDAO.create(snack);
        snackList.add(snack);
        snackListFilteredByDate.add(snack);
        snackAdapter.notifyDataSetChanged();
        editTextSnack.setText("");
    }

    private void loadSnackData() {
        snackDAO.read(snackList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                snackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }

    private void  loadSnackDataFilteredByDate(){
        snackDAO.readByCurrentDate(snackListFilteredByDate, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                snackAdapter.notifyDataSetChanged();
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
        }else if(id == R.id.action_pastdata){
            showBottomSheetDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recViewForPastData);
        snackAdapterForPastData = new SnackAdapterForPastData(snackList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(snackAdapterForPastData);
        snackAdapterForPastData.notifyDataSetChanged();
        bottomSheetDialog.show();

    }
}


