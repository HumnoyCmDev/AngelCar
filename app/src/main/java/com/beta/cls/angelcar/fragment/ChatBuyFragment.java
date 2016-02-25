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

import com.beta.cls.angelcar.Adapter.MessageAdapter;
import com.beta.cls.angelcar.Adapter.MessageItemAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.ChatMessageActivity;
import com.beta.cls.angelcar.gao.MessageAdminCollectionGao;
import com.beta.cls.angelcar.gao.MessageGao;
import com.beta.cls.angelcar.util.BlogMessage;
import com.beta.cls.angelcar.interfaces.AsyncResult;
import com.beta.cls.angelcar.manager.LoadMessageAsync;
import com.beta.cls.angelcar.util.PostBlogArrayMessage;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.google.gson.Gson;
import com.hndev.library.util.MessageAPI;
import com.squareup.otto.Subscribe;


import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 26/1/59.
 */
public class ChatBuyFragment extends Fragment {
    public static final String ARGS_MESSAGE_BY = "shop";

    @Bind(R.id.list_view)
    ListView listView;

    private List<BlogMessage> message;
    private MessageItemAdapter itemAdapter;

    private static final String TAG = "ChatBuyFragment";


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

//        if (savedInstanceState == null) {
//            initMessage();
//        }
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

    }


    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe // broadcast
    public void onEvent(PostBlogArrayMessage blogArrayMessage){
//        message = blogArrayMessage.getMessageViewByAdmin().get(0).getMessage();
//            itemAdapter =
//                    new MessageItemAdapter(message);
//            listView.setAdapter(itemAdapter);


    }

    @Subscribe
    public void onProductMessageAdminGao(MessageAdminCollectionGao gao){
        // TODO Retrofit 2.0 ReQuest Message #Code 001
        MessageGao messageGao = gao.getMessageAdmin().get(0).getMessage().get(0);
        Log.i(TAG, "onProductMessageAdminGao: "+messageGao.getMessagesTamp().toString());

        MessageAdapter adapter = new MessageAdapter(gao.getMessageAdmin().get(0).getMessage());
        listView.setAdapter(adapter);
    }


}

