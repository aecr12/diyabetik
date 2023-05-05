package com.ae.diyabetik;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.InformationCallback;
import com.ae.DAO.TensionDAO;
import com.ae.Models.Tension;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TensionTracker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText editTextSystolic, editTextDiastolic, editTextDate;
    private ImageButton imageButtonCalendar;
    private Button buttonSave;
    private int year, month, day, hour, minute;
    private RecyclerView recyclerView;
    private TensionAdapter tensionAdapter;
    private ArrayList<Tension> tensionList;
    TensionDAO tensionDAO = new TensionDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tension_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextSystolic = findViewById(R.id.editTextSystolic);
        editTextDiastolic = findViewById(R.id.editTextDiastolic);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setEnabled(false);
        imageButtonCalendar = findViewById(R.id.imageButtonCalendar);
        buttonSave = findViewById(R.id.buttonSave);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tensionList = new ArrayList<>();
        tensionAdapter = new TensionAdapter(tensionList, this);
        recyclerView.setAdapter(tensionAdapter);

        loadTensionData();

        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTension();
            }
        });
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showDateTimePicker() {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        year = i;
        month = i1;
        day = i2;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hour = i;
        minute = i1;

        String selectedDateTime = day + "/" + (month + 1) + "/" + year + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute);
        editTextDate.setText(selectedDateTime);
    }

    private void saveTension() {
        String systolic = editTextSystolic.getText().toString();
        String diastolic = editTextDiastolic.getText().toString();
        String dateTime = editTextDate.getText().toString();

        if (TextUtils.isEmpty(systolic) || TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(diastolic)) {
            Toast.makeText(this, "Lütfen gerekli alanları doldurunuz", Toast.LENGTH_SHORT).show();
            return;
        }

        Tension tension = new Tension(null, Integer.parseInt(systolic), Integer.parseInt(diastolic),dateTime);
        tensionDAO.create(tension);

        tensionList.add(tension);
        tensionAdapter.notifyDataSetChanged();

        editTextSystolic.setText("");
        editTextDiastolic.setText("");
        editTextDate.setText("");
    }

    private void loadTensionData() {
        tensionDAO.read(tensionList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                tensionAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
