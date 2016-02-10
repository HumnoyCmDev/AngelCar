package com.beta.cls.angelcar.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.beta.cls.angelcar.manager.*;
import com.beta.cls.angelcar.util.PostBlogArrayMessage;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.google.gson.Gson;
import com.hndev.library.api.ConnectAPi;
import com.hndev.library.api.MessageAPi;
import com.hndev.library.manager.Callback;
import com.squareup.otto.Produce;

/**
 * Created by humnoy on 27/1/59.
 */
public class RealTimeBroadCast extends BroadcastReceiver {

    private static final String TAG = "RealTimeBroadCast";
    private PostBlogArrayMessage blogArrayMessage;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        loadData();
    }

    private void loadData() {

        Boolean isInternetPresent = false;
        isInternetPresent = ConnectionDetector.getInstance().isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            MessageAPi aPi = new MessageAPi.ViewMessageInBuilder()
                    .setMessage("26").build();
            new ConnectAPi(aPi, new Callback() {
                @Override
                public void onSucceed(String s, boolean isSucceed) {
                    Gson gson = new Gson();
                    blogArrayMessage = gson.fromJson(s, PostBlogArrayMessage.class);
                    BusProvider.getInstance().post(getMessageFromBroadCast());
                    startAlarm(3000);
                    /*Toast.makeText(context, "Active BroadCast",
                            Toast.LENGTH_LONG).show();*/

                }
            });
        } else {
            /*Toast.makeText(context, "ไม่มีการเชื่อมต่ออินเทอร์เน็ต",
            Toast.LENGTH_LONG).show();*/
        }


    }

    @Produce // ChatBuyFragment
    public PostBlogArrayMessage getMessageFromBroadCast() {
        return blogArrayMessage;
    }

    private void startAlarm(long scTime) {
        // start Broadcast;
        Intent new_intent = new Intent(context, RealTimeBroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new_intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);
    }

}
