package com.ae.Notifications;

import android.app.Application;

import com.onesignal.OneSignal;

public class ApplicationClass extends Application {
    // Bildirimlerimn alınması için onesignal implementasyonu
    private static final String ONESIGNAL_APP_ID = "6ff7609c-241f-4518-aed1-84be6cde7710";
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.promptForPushNotifications();
    }
}
