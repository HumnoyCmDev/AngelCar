package com.beta.cls.angelcar.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.beta.cls.angelcar.gao.MessageAdminCollectionGao;
import com.beta.cls.angelcar.manager.*;
import com.beta.cls.angelcar.manager.http.ApiService;
import com.beta.cls.angelcar.manager.http.HttpChatManager;
import com.beta.cls.angelcar.util.PostBlogArrayMessage;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.google.gson.Gson;
import com.hndev.library.api.ConnectAPi;
import com.hndev.library.api.MessageAPi;
import com.hndev.library.manager.Callback;
import com.squareup.otto.Produce;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by humnoy on 27/1/59.
 */
public class RealTimeBroadCast extends BroadcastReceiver {

    private static final String TAG = "RealTimeBroadCast";
    private PostBlogArrayMessage blogArrayMessage;
    private Context context;

    private MessageAdminCollectionGao gao;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        loadData();
    }

    private void loadData() {

        Boolean isInternetPresent = ConnectionDetector.getInstance().isConnectingToInternet();

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

        // TODO Retrofit 2.0 ReQuest Message #Code 001
        ApiService server = HttpChatManager.getInstance().getService();
        Call<MessageAdminCollectionGao> call = server.messageAdmin("26");
        call.enqueue(new retrofit2.Callback<MessageAdminCollectionGao>() {
            @Override
            public void onResponse(Call<MessageAdminCollectionGao> call, Response<MessageAdminCollectionGao> response) {
                if (response.isSuccess()){
                    gao = response.body();
                    BusProvider.getInstance().post(productDataMessageAdminGao());
                }else {
                    try {
                        Log.i(TAG, "onResponse: error"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageAdminCollectionGao> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });


    }

    @Produce // ChatBuyFragment
    public PostBlogArrayMessage getMessageFromBroadCast() {
        return blogArrayMessage;
    }

    @Produce
    public MessageAdminCollectionGao productDataMessageAdminGao(){
        return gao;
    }

    private void startAlarm(long scTime) {
        // start Broadcast;
        Intent new_intent = new Intent(context, RealTimeBroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new_intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);
    }

}
