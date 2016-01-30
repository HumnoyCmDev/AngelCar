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
import android.widget.Toast;

import com.beta.cls.angelcar.Adapter.MessageItemAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.ChatMessageActivity;
import com.beta.cls.angelcar.api.MessageAPI;
import com.beta.cls.angelcar.api.model.BlogMessage;
import com.beta.cls.angelcar.api.model.LoadMessageAsync;
import com.beta.cls.angelcar.api.model.PostBlogArrayMessage;
import com.beta.cls.angelcar.api.model.PostBlogMessage;
import com.beta.cls.angelcar.manager.AsyncResultChat;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        MessageAPI api = new MessageAPI();
        api.outMessage("2015062900001");
//        Log.i(TAG, "initMessage: "+api.getURL());
        new LoadMessageAsync(new AsyncResultChat() {
            @Override
            public void onSucceed(PostBlogMessage messages) {
                message = messages.getMessage();
                MessageItemAdapter messageItemAdapter = new MessageItemAdapter(getActivity(),
                        messages.getMessage());
                listView.setAdapter(messageItemAdapter);
            }
            @Override
            public void onFail() {

            }
        }).execute(api);
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
