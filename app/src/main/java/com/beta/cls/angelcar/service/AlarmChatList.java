package com.beta.cls.angelcar.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beta.cls.angelcar.Adapter.MultipleListViewChatAdapter;
import com.beta.cls.angelcar.api.MessageAPI;
import com.beta.cls.angelcar.api.model.BlogMessage;
import com.beta.cls.angelcar.api.model.LoadMessageAsync;
import com.beta.cls.angelcar.api.model.PostBlogMessage;
import com.beta.cls.angelcar.api.model.TypeChat;
import com.beta.cls.angelcar.manager.AsyncResultChat;
import com.squareup.otto.Produce;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by humnoy on 29/1/59.
 */
public class AlarmChatList extends BroadcastReceiver{

    private PostBlogMessage blogMessage;
    private Context mContext;
    private static final String TAG = "AlarmChatList";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;

        
        BlogMessage getIntent = Parcels.unwrap(intent.getParcelableExtra("BlogMessage"));
        if (getIntent !=null) {
            MessageAPI messageAPI = new MessageAPI();
            messageAPI.message(TypeChat.VIEW,
                    getIntent.getMessagecarid(),
                    getIntent.getMessagefromuser(),
                    "1");//blogMessage.getMessageid());

            Log.i(TAG, "onReceive: "+ messageAPI.getURL());

            new LoadMessageAsync(new AsyncResultChat() {
                @Override
                public void onSucceed(PostBlogMessage messages) {
                    blogMessage = messages;
                   BusProvider.getInstance().post(getBlogMessage());

                    Intent new_intent = new Intent(mContext, AlarmChatList.class);
                    long scTime = 2000;
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, new_intent, 0);
                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);
                }

                @Override
                public void onFail() {

                }
            }).execute(messageAPI);
        }

    }

    @Produce
    public PostBlogMessage getBlogMessage() {
        return blogMessage;
    }
}
