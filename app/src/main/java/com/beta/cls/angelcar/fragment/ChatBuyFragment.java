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
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.ChatActivity;
import com.beta.cls.angelcar.gao.MessageAdminCollectionGao;
import com.beta.cls.angelcar.gao.MessageGao;
import com.beta.cls.angelcar.manager.MessageManager;
import com.beta.cls.angelcar.manager.http.ApiChatService;
import com.beta.cls.angelcar.manager.http.HttpChatManager;


import org.parceler.Parcels;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by humnoy on 26/1/59.
 */
public class ChatBuyFragment extends Fragment {
    public static final String ARGS_MESSAGE_BY = "shop";

    @Bind(R.id.list_view)
    ListView listView;

//    MessageAdminCollectionGao gao;
    MessageAdapter adapter;
    MessageManager messageManager;

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

        adapter = new MessageAdapter();
        listView.setAdapter(adapter);
        messageManager = new MessageManager();

        loadMessage();
        initListener();
    }

    private void loadMessage() {
        ApiChatService server = HttpChatManager.getInstance().getService();
        Call<MessageAdminCollectionGao> call = server.messageAdmin("27");
        call.enqueue(new retrofit2.Callback<MessageAdminCollectionGao>() {
            @Override
            public void onResponse(Call<MessageAdminCollectionGao> call, Response<MessageAdminCollectionGao> response) {
                if (response.isSuccess()){

                    messageManager.setMessageGao(response.body()
                            .getMessageAdminGao()
                            .convertToMessageCollectionGao());
                    adapter.setGao(messageManager.getMessageGao().getMessage());
                    adapter.notifyDataSetChanged();

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

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageGao messageGao =
                        messageManager.getMessageGao()
                                .getMessage().get(position);

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("messageBy",ARGS_MESSAGE_BY);
                intent.putExtra("MessageGao", Parcels.wrap(messageGao));
                startActivity(intent);
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
//        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        BusProvider.getInstance().unregister(this);
    }
}

