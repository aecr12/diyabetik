package com.ae.diyabetik;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
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
import java.util.ArrayList;
import java.util.Calendar;

public class BloodSugarTracker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText editTextBloodSugarValue;
    private EditText editTextDate;
    private ImageButton imageButtonCalendar;
    private Button buttonSave;
    private int year, month, day, hour, minute;
    private RecyclerView recyclerView;
    private BloodSugarAdapter bloodSugarAdapter;
    private ArrayList<BloodSugar> bloodSugarList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_sugar_tracker);

        editTextBloodSugarValue = findViewById(R.id.editTextBloodSugarValue);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setEnabled(false);
        imageButtonCalendar = findViewById(R.id.imageButtonCalendar);
        buttonSave = findViewById(R.id.buttonSave);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodSugarList = new ArrayList<>();
        bloodSugarAdapter = new BloodSugarAdapter(bloodSugarList,this);
        recyclerView.setAdapter(bloodSugarAdapter);

        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBloodSugar();
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

    private void saveBloodSugar() {
        String bloodSugarValue = editTextBloodSugarValue.getText().toString();
        String dateTime = editTextDate.getText().toString();
        /*SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        if (TextUtils.isEmpty(bloodSugarValue) || TextUtils.isEmpty(dateTime)) {
            Toast.makeText(this, "Lütfen kan şekeri değerini ve tarihi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        BloodSugar bloodSugar = new BloodSugar(Integer.parseInt(bloodSugarValue), dateTime);
        bloodSugarList.add(bloodSugar);
        bloodSugarAdapter.notifyDataSetChanged();

        editTextBloodSugarValue.setText("");
        editTextDate.setText("");
    }

}