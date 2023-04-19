package com.ae.diyabetik;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class StepCounter extends AppCompatActivity implements SensorEventListener {

    private TextView textViewDate;
    private Button buttonStart;
    private Button buttonStop;
    private ImageButton buttonReset;
    private SimpleDateFormat simpleDateFormat;
    private Date currentDate;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private TextView textViewStepCount;
    private TextView textViewCalorie;
    private TextView textViewDistance;
    private TextView textViewTime;
    private static final int REQUEST_CODE = 1;
    private int stepCount = 0;
    private final static float burntCaloriesPerSecond = 0.04f;
    private final static float meanStepLengthM = 0.8f;


    private long startTime;
    private long elapsedTime;
    private Timer timer;
    private String time;
    private long millis;
    private int seconds;
    private int minutes;
    private int hours;

    // float verilerde işlem yaparken virgülden sonra kaç hane kullanılacak
    DecimalFormat df = new DecimalFormat("#,##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewDate = findViewById(R.id.textViewDate);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        buttonStop.setEnabled(false);
        buttonReset = findViewById(R.id.buttonReset);
        textViewStepCount = findViewById(R.id.textViewStepCount);
        textViewCalorie = findViewById(R.id.textViewCalorie);
        textViewDistance = findViewById(R.id.textViewDistance);
        textViewTime = findViewById(R.id.textViewTime);

        // Tarihin gösterilmesi
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = new Date();
        String formattedDate = simpleDateFormat.format(currentDate);
        textViewDate.setText(formattedDate);

        // sensöre erişilmesi ve cihazda gerekli sensör var mı kontrol edilmesi
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (isStepDetectorSensorAvailable()) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            if (stepSensor == null) {
                Toast.makeText(this, "Adım tespit sensörü bulunamadı.", Toast.LENGTH_SHORT).show();
            } else {
                requestStepDetectorSensorPermission();
            }
        } else {
            Toast.makeText(this, "Cihazınız adım tespit sensörü desteklemiyor.", Toast.LENGTH_SHORT).show();
        }


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setEnabled(false);
                buttonReset.setEnabled(false);
                buttonStop.setEnabled(true);
                textViewStepCount.setText(String.valueOf(stepCount));
                textViewCalorie.setText(String.valueOf("Yakılan Kalori: 0"));
                if (stepSensor != null) {
                    sensorManager.registerListener(StepCounter.this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
                startTimer();

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonReset.setEnabled(true);
                stop();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSteps();
                resetCalories();
                resetDistance();
                resetTime();
            }
        });
    }

    // sensör verisi değiştikçe textviewlerin güncellenmesi
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
            textViewStepCount.setText(String.valueOf(stepCount));
            textViewCalorie.setText(String.valueOf("Yakılan Kalori (kcal): " + calculateBurntCalories()));
            textViewDistance.setText(String.valueOf("Yürünen Mesafe (m): ") + calculateDistanceM());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // Adım tespit sensörünün kullanılabilirliğini kontrol etmek için bu metod çağrılır
    private boolean isStepDetectorSensorAvailable() {
        PackageManager packageManager = getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
    }

    // Adım tespit sensörü iznini istemek için bu metod çağrılır
    private void requestStepDetectorSensorPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_CODE);
    }

    // Adım tespit sensörü izni verilip verilmediğini kontrol etmek için bu metod çağrılır
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Adım tespit sensörü izni verildi.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Adım tespit sensörü izni verilmedi.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // yakılan kaloriyi hesaplayacak metot
    private float calculateBurntCalories() {

        return Float.parseFloat(df.format(burntCaloriesPerSecond * stepCount));
    }

    //yürünen mesafeyi hesaplayacak metot
    private float calculateDistanceM() {
        return Float.parseFloat(df.format(meanStepLengthM * stepCount));
    }

    // geçen süreyi hesaplayacak metot
    private void startTimer() {
        if (elapsedTime == 0) {
            startTime = System.currentTimeMillis();
        } else {
            startTime = System.currentTimeMillis() - elapsedTime;
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                millis = System.currentTimeMillis() - startTime;
                seconds = (int) (millis / 1000) % 60;
                minutes = (int) ((millis / (1000 * 60)) % 60);
                hours = (int) ((millis / (1000 * 60 * 60)) % 24);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                        textViewTime.setText("Geçen Süre: " + time);
                    }
                });
            }
        }, 0, 1000);
    }

    private void stopTimer(){
        timer.cancel();
        elapsedTime = System.currentTimeMillis() - startTime;
    }


    // durdur butonuna basılınca tetiklenecek fonksiyon
    private void stop(){
        sensorManager.unregisterListener(this,stepSensor);
        stopTimer();
    }

    private void resetSteps(){
        stepCount=0;
        textViewStepCount.setText(String.valueOf(stepCount));
    }

    private void resetCalories(){
        textViewCalorie.setText(String.valueOf("Yakılan Kalori (kcal): " + calculateBurntCalories()));
    }

    private void resetDistance(){
        textViewDistance.setText(String.valueOf("Yürünen Mesafe (m): ") + calculateDistanceM());
    }

    private void resetTime(){
        elapsedTime = 0;
        millis=0;
        seconds=0;
        minutes=0;
        hours=0;
        time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        textViewTime.setText("Geçen Süre: " + time);
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
