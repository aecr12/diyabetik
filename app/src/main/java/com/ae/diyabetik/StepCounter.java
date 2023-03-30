package com.ae.diyabetik;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StepCounter extends AppCompatActivity implements SensorEventListener {

    private Sensor adimSaymaSensoru;
    private SensorManager sensorManager;
    private boolean adimSaymaSensoruDestekleniyor;
    private int adimSayisi = 0;

    private TextView textViewDate;
    private TextView textViewStepCount;
    private TextView textViewCalorie;
    private TextView textViewVelocity;
    private TextView sureTextView;
    private TextView textViewDistance;
    private Button baslaDurdurButton;
    private SimpleDateFormat simpleDateFormat;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        textViewDate = findViewById(R.id.textViewDate);
        textViewStepCount = findViewById(R.id.textViewStepCount);
        textViewCalorie = findViewById(R.id.textViewCalorie);
        textViewVelocity = findViewById(R.id.textViewVelocity);
        sureTextView = findViewById(R.id.textViewTime);
        textViewDistance = findViewById(R.id.textViewDistance);
        baslaDurdurButton = findViewById(R.id.baslaDurdurButton);

        // Tarihin gösterilmesi
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = new Date();
        String formattedDate = simpleDateFormat.format(currentDate);
        textViewDate.setText(formattedDate);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            adimSaymaSensoru = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            adimSaymaSensoruDestekleniyor = true;
        } else {
            textViewStepCount.setText("Adım sayma sensörü cihazınızda desteklenmiyor");
        }

        baslaDurdurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adimSaymaSensoruDestekleniyor) {
                    if (baslaDurdurButton.getText().equals("Başlat")) {
                        // Sensör okumaları başlatılıyor
                        sensorManager.registerListener(StepCounter.this, adimSaymaSensoru, SensorManager.SENSOR_DELAY_NORMAL);
                        baslaDurdurButton.setText("Durdur");
                    } else {
                        // Sensör okumaları durduruluyor
                        sensorManager.unregisterListener(StepCounter.this);
                        baslaDurdurButton.setText("Başlat");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int adimSayisi = (int) sensorEvent.values[0];
            textViewStepCount.setText(String.valueOf(adimSayisi));
            Toast.makeText(this,adimSayisi,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}