package com.ae.diyabetik;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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


public class StepCounterActivity extends AppCompatActivity {

    private TextView stepCountTextView;
    private Button startButton;
    private Button stopButton;
    private boolean isRunning;
    private StepCountReceiver receiver;
    private SimpleDateFormat simpleDateFormat;
    private Date currentDate;
    private TextView textViewDate;
    StepCounterDAO stepCounterDAO = new StepCounterDAO();
    List<StepCounter> stepCounterList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
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

}