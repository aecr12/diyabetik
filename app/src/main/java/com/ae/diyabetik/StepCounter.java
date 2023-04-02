package com.ae.diyabetik;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StepCounter extends AppCompatActivity implements SensorEventListener {

    private TextView textViewDate;
    private Button baslaDurdurButton;
    private SimpleDateFormat simpleDateFormat;
    private Date currentDate;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private boolean isRunning;
    private double totalSteps = 0;
    private double previousTotalSteps = 0;
    private TextView textViewStepCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewDate = findViewById(R.id.textViewDate);
        baslaDurdurButton = findViewById(R.id.baslaDurdurButton);

        // Tarihin gösterilmesi
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = new Date();
        String formattedDate = simpleDateFormat.format(currentDate);
        textViewDate.setText(formattedDate);

    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        textViewStepCount=findViewById(R.id.textViewStepCount);
        if (isRunning) {
            totalSteps = event.values[0];

            // Current steps are calculated by taking the difference of total steps
            // and previous steps
            Double currentSteps = totalSteps - previousTotalSteps;

            // It will show the current steps to the user
            textViewStepCount.setText(currentSteps.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Bu metodun kullanımı burada gerekli değil
    }

    @Override
    public void onResume(){
        super.onResume();
        isRunning = true;
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepSensor==null){
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        }else{
            sensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}