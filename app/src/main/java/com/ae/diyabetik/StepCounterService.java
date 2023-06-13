package com.ae.diyabetik;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.ae.DAO.InformationCallback;
import com.ae.DAO.StepCounterDAO;
import com.ae.Models.StepCounter;
import java.util.ArrayList;

import java.util.List;

public class StepCounterService extends Service implements SensorEventListener {

    // bu sayfada step detector sensör kullanıldı. alınan adım sayısı broadcast receiverle, adım sayısını kullancak activitye gönderildi

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int stepCount;
    private boolean isRunning;
    private final static String CHANNEL_ID = "1";
    private StepCounterDAO stepCounterDAO = new StepCounterDAO();
    private StepCounter stepCounter;
    private List<StepCounter> stepCounterList = new ArrayList<>();
    private List<StepCounter> updatedStepCounterList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        stepCounterDAO.read(stepCounterList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                stepCount = stepCounterList.get(0).getStepCount();
            }

            @Override
            public void onInformationNotLoaded() {
                stepCount = 0;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Adımsayar çalışıyor...", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Adımsayar çalışıyor...")
                    .setContentText(String.valueOf(stepCount))
                    .setSmallIcon(R.mipmap.app_logo)
                    .build();
            startForeground(1, notification);
        } else {
            startForeground(1, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
        isRunning = true;

        saveStepCounter();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopStepDetector();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void stopStepDetector() {
        sensorManager.unregisterListener(this);
        isRunning = false;
        stopForeground(true);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
            sendBroadcastStepCount(stepCount);
            updateStepCount(stepCount);
        }

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (notificationManager != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Adımsayar çalışıyor")
                    .setContentText(String.valueOf(stepCount))
                    .setSmallIcon(R.mipmap.app_logo);

            notificationManager.notify(1, builder.build());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void sendBroadcastStepCount(int stepCount) {
        Intent intent = new Intent("step_count_changed");
        intent.putExtra("step_count", stepCount);
        sendBroadcast(intent);
    }

    private void saveStepCounter() {
        stepCounterList = stepCounterDAO.read(stepCounterList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {

            }

            @Override
            public void onInformationNotLoaded() {
                stepCounter = new StepCounter(null, stepCount);
                stepCounterDAO.create(stepCounter);
            }
        });
    }

    private void updateStepCount(int stepCount) {
        updatedStepCounterList = stepCounterDAO.read(stepCounterList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                StepCounter stepCounterToUpdate = new StepCounter(stepCounterList.get(0).getId(), stepCount);
                stepCounterDAO.update(stepCounterToUpdate);
            }

            @Override
            public void onInformationNotLoaded() {

            }
        });
    }
}