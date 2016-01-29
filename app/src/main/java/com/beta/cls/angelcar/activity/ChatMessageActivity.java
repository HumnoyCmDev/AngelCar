package com.beta.cls.angelcar.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.beta.cls.angelcar.Adapter.MultipleListViewChatAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.api.MessageAPI;
import com.beta.cls.angelcar.api.SendMessageAPI;
import com.beta.cls.angelcar.api.model.BlogMessage;
import com.beta.cls.angelcar.api.model.LoadMessageAsync;
import com.beta.cls.angelcar.api.model.PostBlogMessage;
import com.beta.cls.angelcar.api.model.SendMessageAsync;
import com.beta.cls.angelcar.api.model.TypeChat;
import com.beta.cls.angelcar.manager.AsyncResultChat;
import com.beta.cls.angelcar.manager.AsyncSucceed;
import com.beta.cls.angelcar.service.AlarmChatList;
import com.beta.cls.angelcar.service.BusProvider;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by humnoy on 27/1/59.
 */
public class ChatMessageActivity extends AppCompatActivity{
    @Bind(R.id.list_view)
    ListView listView;

    @Bind(R.id.list_view_chat_layout_input_chat)
    EditText messageText;

    private MultipleListViewChatAdapter chatAdapter;

    private static final String TAG = "ChatMessageActivity";

    // getIntent
    private BlogMessage blogMessage;
    private String messageBy;

    private List<BlogMessage> listData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_chat_layout);
        ButterKnife.bind(this);

        Intent getIntent = getIntent();
        if (getIntent != null){
            messageBy = getIntent.getStringExtra("messageBy");
            blogMessage = Parcels.unwrap(getIntent.getParcelableExtra("BlogMessage"));
        }
        loadMessage();
    }

    private void loadMessage() {

        MessageAPI messageAPI = new MessageAPI();
        messageAPI.message(TypeChat.VIEW,
                blogMessage.getMessagecarid(),
                blogMessage.getMessagefromuser(),
                "1");//blogMessage.getMessageid());

        Log.i(TAG, "loadMessage: url "+messageAPI.getURL());
//
//        new LoadMessageAsync(new AsyncResultChat() {
//            @Override
//            public void onSucceed(PostBlogMessage messages) {
//                listData = messages.getMessage();
//                chatAdapter = new MultipleListViewChatAdapter(ChatMessageActivity.this,
//                        listData,messageBy);
//                listView.setAdapter(chatAdapter);
//            }
//
//            @Override
//            public void onFail() {
//
//            }
//        }).execute(messageAPI);


        /// test
        Intent new_intent = new Intent(ChatMessageActivity.this, AlarmChatList.class);
        new_intent.putExtra("BlogMessage",Parcels.wrap(blogMessage));
        long scTime = 1000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ChatMessageActivity.this, 0, new_intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);
    }

    @OnClick(R.id.list_view_chat_layout_button_send)
    public void sendMessage(){
        BlogMessage b = new BlogMessage();
        b.setMessageid(blogMessage.getMessagecarid());
        b.setMessagecarid(blogMessage.getMessagecarid());
        b.setMessagefromuser(blogMessage.getMessagefromuser());
        b.setMessagetext(messageText.getText().toString()); // edit text
        b.setDisplayname(blogMessage.getDisplayname());
        b.setMessageby(messageBy);
        b.setUserprofileimage(blogMessage.getUserprofileimage());
//      b.setMessagestamp(blogMessage.getMessagestamp()); // date time set**
        listData.add(b);
        chatAdapter.notifyDataSetChanged();

        // send Message to server
        SendMessageAPI api = new SendMessageAPI();
        api.message("26",
                blogMessage.getMessagefromuser(),
                messageText.getText().toString(),
                messageBy);

        new SendMessageAsync(new AsyncSucceed() {
            @Override
            public void onSucceed() {
                Toast.makeText(ChatMessageActivity.this,"Send Succed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {

            }
        }).execute(api);
        messageText.setText("");
    }


    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onEvent(PostBlogMessage blogMessage){

        listData = blogMessage.getMessage();
        chatAdapter = new MultipleListViewChatAdapter(
                ChatMessageActivity.this,
                listData,
                messageBy);
        listView.setAdapter(chatAdapter);
    }

}
