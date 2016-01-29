package com.beta.cls.angelcar.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.beta.cls.angelcar.Adapter.MessageItemAdapter;
import com.beta.cls.angelcar.api.MessageAPI;
import com.beta.cls.angelcar.api.model.BlogMessage;
import com.beta.cls.angelcar.api.model.LoadMessageAsync;
import com.beta.cls.angelcar.api.model.PostBlogArrayMessage;
import com.beta.cls.angelcar.manager.AsyncResult;
import com.google.gson.Gson;
import com.squareup.otto.Produce;

import java.util.List;

/**
 * Created by humnoy on 27/1/59.
 */
public class RealTimeBroadCast extends BroadcastReceiver{

    private static final String TAG = "RealTimeBroadCast";
    private PostBlogArrayMessage blogArrayMessage;
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i(TAG, "onReceive: ");

        this.context = context;

        loadData();
    }

    private void loadData() {
//        new
        MessageAPI api = new MessageAPI();
        api.putMessage("26");
//        Log.i(TAG, "BroadCast Start");
        new LoadMessageAsync(new AsyncResult() {
            @Override
            public void onSucceed(String s) {
                Gson gson = new Gson();
                blogArrayMessage = gson.fromJson(s, PostBlogArrayMessage.class);
                BusProvider.getInstance().post(getMessageFromBroadCast());

                // start Broadcast;
                Intent new_intent = new Intent(context, RealTimeBroadCast.class);
                long scTime = 5000;
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new_intent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);
//                Log.i(TAG, "BroadCast Succeed");

            }

            @Override
            public void onFail() {
                Log.i(TAG, "onFail: ");
            }
        }).execute(api);
    }

    @Produce
    public PostBlogArrayMessage getMessageFromBroadCast(){
      return blogArrayMessage;
    }

}
