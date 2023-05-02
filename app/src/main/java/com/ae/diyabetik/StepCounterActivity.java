package com.ae.diyabetik;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ae.DAO.InformationCallback;
import com.ae.DAO.StepCounterDAO;
import com.ae.Models.StepCounter;
import com.ae.Receivers.StepCountReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class StepCounterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView stepCountTextView;
    private Button startButton;
    private Button stopButton;
    private boolean isRunning;
    private StepCountReceiver receiver;
    private SimpleDateFormat simpleDateFormat;
    private Date currentDate;
    private TextView textViewDate;
    private StepCounterDAO stepCounterDAO = new StepCounterDAO();
    private List<StepCounter> stepCounterList = new ArrayList<>();
    private List<StepCounter> stepCounterList2 = new ArrayList<>();
    private ImageButton imageButtonCalendar;
    private int year, month, day;
    private String selectedDate;
    private String monthString;
    private String dayString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageButtonCalendar = findViewById(R.id.imageButtonCalendar);
        textViewDate = findViewById(R.id.textViewDate);
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = new Date();
        String formattedDate = simpleDateFormat.format(currentDate);
        textViewDate.setText(formattedDate);

        stepCountTextView = findViewById(R.id.textViewStepCount);
        startButton = findViewById(R.id.buttonStart);
        stopButton = findViewById(R.id.buttonStop);
        stepCounterDAO.read(stepCounterList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                stepCountTextView.setText(String.valueOf(stepCounterList.get(0).getStepCount()));
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });

        receiver = new StepCountReceiver(stepCount -> {
            stepCountTextView.setText(String.valueOf(stepCount));
        });

        registerReceiver(receiver, new IntentFilter("step_count_changed"));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = true;
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                startStepDetectorService();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                stopStepDetectorService();
            }
        });

        // Check if the service is running, and update the UI accordingly
        if (isMyServiceRunning(StepCounterService.class)) {
            isRunning = true;
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            isRunning = false;
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        }
        imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
    }

    // Start the foreground service that detects steps
    private void startStepDetectorService() {
        Intent intent = new Intent(this, StepCounterService.class);
        startForegroundService(intent);
    }

    // Stop the foreground service
    private void stopStepDetectorService() {
        Intent intent = new Intent(this, StepCounterService.class);
        stopService(intent);
    }

    // Check if the specified service is running or not
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isRunning", isRunning);
    }

    private void showDateTimePicker(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    private void showByDate(){
        stepCounterDAO.getByDate(stepCounterList2, selectedDate, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                Toast.makeText(StepCounterActivity.this,"Bulundu: "+ stepCounterList2.get(0).getStepCount(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInformationNotLoaded() {
                Toast.makeText(StepCounterActivity.this,"Seçtiğiniz tarihe uygun kayıt bulunamadı" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        month = month +1;
        this.day = dayOfMonth;
        if (month<=9){
            monthString = "0"+month;
        }else {
            monthString = String.valueOf(month);
        }
        if (dayOfMonth<=9){
            dayString = "0"+dayOfMonth;
        }else {
            dayString = String.valueOf(dayOfMonth);
        }
        selectedDate = dayString+"-"+monthString+"-"+year;
        Toast.makeText(StepCounterActivity.this,selectedDate,Toast.LENGTH_LONG).show();
        showByDate();
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}