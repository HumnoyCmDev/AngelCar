package com.beta.cls.angelcar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beta.cls.angelcar.Adapter.MessageItemAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.ChatMessageActivity;
import com.beta.cls.angelcar.gao.MessageCollectionGao;
import com.beta.cls.angelcar.gao.MessageGao;
import com.beta.cls.angelcar.manager.http.ApiService;
import com.beta.cls.angelcar.manager.http.HttpChatManager;
import com.beta.cls.angelcar.util.BlogMessage;
import com.beta.cls.angelcar.util.PostBlogMessage;
import com.google.gson.Gson;
import com.hndev.library.api.ConnectAPi;
import com.hndev.library.api.MessageAPi;
import com.hndev.library.manager.Callback;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by humnoy on 26/1/59.
 */
public class ChatSellFragment extends Fragment {
    public static final String ARGS_MESSAGE_BY = "user";
    @Bind(R.id.list_view)
    ListView listView;

    private List<BlogMessage> message;

    private static final String TAG = "ChatSellFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_view_layout, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            initMessage();
        }

        initListener();
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlogMessage blogMessage = message.get(position);
                Intent intent = new Intent(getActivity(), ChatMessageActivity.class);
                intent.putExtra("messageBy",ARGS_MESSAGE_BY);
                intent.putExtra("BlogMessage", Parcels.wrap(blogMessage));
                startActivity(intent);
            }
        });
    }
    private void initMessage() {
        MessageAPi messageAPi = new MessageAPi.ViewMessageOutBuilder()
                .setMessage("2015062900001")
                .build();
        new ConnectAPi(messageAPi, new Callback() {
            @Override
            public void onSucceed(String s, boolean isSucceed) {
                Gson gson = new Gson();
                PostBlogMessage postBlogMessage = gson.fromJson(s, PostBlogMessage.class);
                message = postBlogMessage.getMessage();
                MessageItemAdapter messageItemAdapter = new MessageItemAdapter(postBlogMessage.getMessage());
                listView.setAdapter(messageItemAdapter);
            }
        });


        /*ApiService call = HttpChatManager.getInstance().getService();
        call.message("viewclient","2015062900001").enqueue(new retrofit2.Callback<MessageCollectionGao>() {
            @Override
            public void onResponse(Call<MessageCollectionGao> call, Response<MessageCollectionGao> response) {
               if (response.isSuccess()) {
                   for (MessageGao m : response.body().getMessage()) {
                       Log.i(TAG, "onResponse: m2 " + m.getMessageText());
                   }
               }else {
                   try {
                       Log.i(TAG, "onResponse: "+response.errorBody().string());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
            }

            @Override
            public void onFailure(Call<MessageCollectionGao> call, Throwable t) {
                Log.i(TAG, "onResponse: "+t.toString());
            }
        });*/

    }

    @Override
    public void onResume() {
        super.onResume();
        initMessage();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
