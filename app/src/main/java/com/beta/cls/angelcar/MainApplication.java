package com.beta.cls.angelcar;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import com.beta.cls.angelcar.manager.Contextor;
import com.beta.cls.angelcar.service.RealTimeBroadCast;

/**
 * Created by humnoy on 27/1/59.
 */
public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
        //TODO ปิดไว้ ก่อน อย่าลืมมาแก้
//        Intent intent = new Intent(this, RealTimeBroadCast.class);
//        long scTime = 1000;
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
