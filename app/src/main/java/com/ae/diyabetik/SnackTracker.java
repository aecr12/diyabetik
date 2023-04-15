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

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ae.DAO.SnackDAO;
import com.ae.Models.Snack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SnackTracker extends AppCompatActivity {

    ImageView imageViewSnack;
    RecyclerView recyclerView1;
    EditText editTextSnack;
    FloatingActionButton fab;
    private ArrayList<Snack> snackList;
    SnackAdapter snackAdapter;
    SnackDAO snackDAO = new SnackDAO();

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
        snackAdapter = new SnackAdapter(snackList, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(snackAdapter);

        loadSnackData();

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateTime = dateFormat.format(calendar.getTime());
        Snack snack = new Snack(null, snackItemName, currentDateTime);
        snackDAO.create(snack);
        snackList.add(snack);
        snackAdapter.notifyDataSetChanged();
        editTextSnack.setText("");
    }

    private void loadSnackData() {
        snackDAO.read(snackList, snackAdapter);
    }

}


