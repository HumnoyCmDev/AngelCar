package com.beta.cls.angelcar.activity;

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
import com.beta.cls.angelcar.interfaces.AsyncResultChat;
import com.beta.cls.angelcar.interfaces.CallBackResult;
import com.beta.cls.angelcar.interfaces.Callback;
import com.beta.cls.angelcar.manager.BusProvider;
import com.beta.cls.angelcar.util.ConnectAPi;
import com.beta.cls.angelcar.util.MessageAPi;
import com.google.gson.Gson;
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
            Toast.makeText(ChatMessageActivity.this,"-"+messageBy,Toast.LENGTH_SHORT).show();
        }
        loadMessage();

        // test up mac 2
    }

    private void loadMessage() {
//        MessageAPI messageAPI = new MessageAPI();
//        messageAPI.message(TypeChat.VIEW,
//                blogMessage.getMessagecarid(),
//                blogMessage.getMessagefromuser(),
//                "1");//blogMessage.getMessageid());
//
//        Log.i(TAG, "loadMessage: url "+messageAPI.getURL());
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
        MessageAPi aPi = new MessageAPi.ViewMessageBuilder()
                .setId(blogMessage.getMessagecarid())
                .setIdUser(blogMessage.getMessagefromuser())
                .setIdMessage("1")
                .build();
        Log.i(TAG, "loadMessage: url "+aPi.getUrl());

        new ConnectAPi(aPi, new Callback() {
            @Override
            public void onSucceed(String s, boolean isSucceed) {
                Gson gson = new Gson();
                PostBlogMessage postBlogMessage = gson.fromJson(
                        s, PostBlogMessage.class);
                listData = postBlogMessage.getMessage();
                chatAdapter = new MultipleListViewChatAdapter(
                        ChatMessageActivity.this,
                        listData, messageBy);
                listView.setAdapter(chatAdapter);
            }
        });


//        /// test
//        Intent new_intent = new Intent(ChatMessageActivity.this, AlarmChatList.class);
//        new_intent.putExtra("BlogMessage",Parcels.wrap(blogMessage));
//        long scTime = 1000;
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(ChatMessageActivity.this, 0, new_intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);
    }

    @OnClick(R.id.list_view_chat_layout_button_send)
    public void buttonSendMessage(){
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
//        SendMessageAPI api = new SendMessageAPI();
//        api.message("26",
//                blogMessage.getMessagefromuser(),
//                messageText.getText().toString(),
//                messageBy);
//
//        new SendMessageAsync(new CallBackResult() {
//            @Override
//            public void onSucceed() {
//                Toast.makeText(ChatMessageActivity.this,"Send Succed",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFail() {
//
//            }
//        }).execute(api);
        sendMessage(messageText.getText().toString().trim());

    }

    private void sendMessage(String message){
        if (!message.equals("")) {
            MessageAPi aPi = new MessageAPi.SendMessageBuilder()
                    .setId("26")
                    .setIdUser(blogMessage.getMessagefromuser())
                    .setMessage(message)
                    .setTypeFrom(messageBy)
                    .build();
            new ConnectAPi(aPi, new Callback() {
                @Override
                public void onSucceed(String s, boolean isSucceed) {

                }
            });
            messageText.setText("");
        }
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
    public void onUpdateChatList(BlogMessage message){
//        BlogMessage b = new BlogMessage();
//        b.setMessageid(message.getMessagecarid());
//        b.setMessagecarid(message.getMessagecarid());
//        b.setMessagefromuser(message.getMessagefromuser());
//        b.setMessagetext(message.getMessagetext()); // edit text
//        b.setDisplayname(message.getDisplayname());
//        b.setMessageby(message.getMessageby());
//        b.setUserprofileimage(message.getUserprofileimage());
////      b.setMessagestamp(blogMessage.getMessagestamp()); // date time set**
//        listData.add(b);
//        chatAdapter.notifyDataSetChanged();
        Toast.makeText(ChatMessageActivity.this,"onUpdateChatList",Toast.LENGTH_SHORT).show();
    }

}
