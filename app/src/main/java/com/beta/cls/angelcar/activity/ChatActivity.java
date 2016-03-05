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
import com.beta.cls.angelcar.gao.MessageCollectionGao;
import com.beta.cls.angelcar.gao.MessageGao;
import com.beta.cls.angelcar.interfaces.WaitMessageOnBackground;
import com.beta.cls.angelcar.manager.MessageManager;
import com.beta.cls.angelcar.manager.WaitMessageSynchronous;
import com.beta.cls.angelcar.manager.http.HttpChatManager;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.manager.http.OkHttpManager;
import com.beta.cls.angelcar.interfaces.CallBackMainThread;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 27/1/59. เวลา 13:42
 ***************************************/
public class ChatActivity extends AppCompatActivity {
    @Bind(R.id.list_view) ListView listView;

    @Bind(R.id.list_view_chat_layout_input_chat) EditText messageText;

    private MultipleListViewChatAdapter chatAdapter;

    private static final String TAG = "ChatActivity";

    MessageGao messageGao;
    private String messageBy;

    private List<MessageGao> listData;

    MessageManager messageManager;
    WaitMessageSynchronous synchronous;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_chat_layout);
        ButterKnife.bind(this);

        Intent getIntent = getIntent();
        if (getIntent != null){
            messageBy = getIntent.getStringExtra("messageBy");
            messageGao = Parcels.unwrap(getIntent.getParcelableExtra("MessageGao"));
            Toast.makeText(ChatActivity.this,"-"+messageBy,Toast.LENGTH_SHORT).show();
        }

        chatAdapter = new MultipleListViewChatAdapter(messageBy);
        listView.setAdapter(chatAdapter);

        messageManager = new MessageManager();

        loadMessage();
    }

    private void loadMessage() {
        Call<MessageCollectionGao> call =
                HttpChatManager.getInstance().getService().viewMessage(
                messageGao.getMessageCarId() + "||" +
                        messageGao.getMessageFromUser() + "||" +
                        "1");

        call.enqueue(new retrofit2.Callback<MessageCollectionGao>() {
            @Override
            public void onResponse(Call<MessageCollectionGao> call, Response<MessageCollectionGao> response) {
                    if (response.isSuccess()){

                        messageManager.setMessageGao(response.body());
                        chatAdapter.setMessages(messageManager.getMessageGao().getMessage());
                        chatAdapter.notifyDataSetChanged();

                        synchronous
                                = new WaitMessageSynchronous(waitMessageOnBackground);
                        synchronous.execute();

                    }else {
                        try {
                            Toast.makeText(ChatActivity.this,response.errorBody().string(),Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }

            @Override
            public void onFailure(Call<MessageCollectionGao> call, Throwable t) {
                Toast.makeText(ChatActivity.this,"Failure LogCat!!",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    @OnClick(R.id.list_view_chat_layout_button_send)
    public void buttonSendMessage(){
        MessageGao b = new MessageGao();
        b.setMessageCarId(messageGao.getMessageCarId());
        b.setMessageFromUser(messageGao.getMessageFromUser());
        b.setMessageText(messageText.getText().toString()); // edit text
        b.setDisplayName(messageGao.getDisplayName());
        b.setMessageBy(messageBy);
        b.setUserProfileImage(messageGao.getUserProfileImage());
//      b.setMessagestamp(blogMessage.getMessagestamp()); // date time set**
        listData.add(b);
        chatAdapter.notifyDataSetChanged();

        sendMessage(messageText.getText().toString().trim());

    }

    private void sendMessage(String message){
        if (message.equals("")) {
            OkHttpManager aPi = new OkHttpManager.SendMessageBuilder()
                    .setMessage("26||"+messageGao.getMessageFromUser()+"||"+message+"||"+messageBy).build();
            aPi.callEnqueue(new CallBackMainThread() {
                @Override
                public void onResponse(okhttp3.Response response) {
                    Toast.makeText(ChatActivity.this,"success",Toast.LENGTH_SHORT).show();
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
    public void produceMessage(MessageManager message){
        chatAdapter.setMessages(messageManager.getMessageGao().getMessage());
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(synchronous != null)
        synchronous.killTask();
    }


    /*************
     *Inner Class
     **************/
    WaitMessageOnBackground waitMessageOnBackground = new WaitMessageOnBackground() {
        Response<MessageCollectionGao> response;
        @Override
        public void onBackground() {
            int maxId = messageManager.getMaximumId();
            Call<MessageCollectionGao> cell = HttpChatManager.getInstance()
                    .getService(60 * 1000).waitMessage(messageGao.getMessageCarId() + "||" +
                            messageGao.getMessageFromUser() + "||" +maxId);
            try {
                response = cell.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMainThread() {
            if (response.isSuccess()) {
                messageManager.updateDataToLastPosition(response.body());
                BusProvider.getInstance().post(messageManager);
                for (MessageGao g : response.body().getMessage()) {
                    Log.i(TAG, "doInBackground: " + g.getMessageId());
                    Log.i(TAG, "doInBackground: " + g.getMessageText());
                }
            }
        }
    };

}
