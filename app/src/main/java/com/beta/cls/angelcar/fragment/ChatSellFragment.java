package com.beta.cls.angelcar.fragment;

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
import com.beta.cls.angelcar.dao.MessageCollectionDao;
import com.beta.cls.angelcar.dao.MessageDao;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.manager.CallbackLoadCarModel;
import com.beta.cls.angelcar.manager.Registration;
import com.beta.cls.angelcar.manager.http.HttpManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by humnoy on 26/1/59.
 */
public class ChatSellFragment extends Fragment {
//    public static final String ARGS_MESSAGE_BY = "user";
    @Bind(R.id.list_view) ListView listView;


    MessageAdapter adapter;
    MessageCollectionDao gao;


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

        adapter = new MessageAdapter();
        listView.setAdapter(adapter);

        if (savedInstanceState == null) {
            initMessage();
        }
        listView.setOnItemClickListener(onItemClickListener);
    }


    private void initMessage() {

        HttpManager.getInstance().getService()
                .messageClient(Registration.getInstance().getUserId())
                .enqueue(new retrofit2.Callback<MessageCollectionDao>() {
            @Override
            public void onResponse(Call<MessageCollectionDao> call, Response<MessageCollectionDao> response) {
               if (response.isSuccessful()) {
                   gao = response.body();
                   adapter.setGao(gao.getMessage());
                   adapter.notifyDataSetChanged();
               }else {
                   try {
                       Log.i(TAG, "onResponse: "+response.errorBody().string());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
            }

            @Override
            public void onFailure(Call<MessageCollectionDao> call, Throwable t) {
                Log.i(TAG, "onResponse: "+t.toString());
            }
        });

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

    /***************
     *Listener Zone*
     ***************/
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MessageDao messageDao = gao.getMessage().get(position);
            Call<PostCarCollectionDao> call =
                    HttpManager.getInstance().getService().loadCarModel(messageDao.getMessageCarId());
            call.enqueue(new CallbackLoadCarModel(getContext(),messageDao.getMessageFromUser()));
        }
    };

    /*****************
    *Inner Class Zone*
    ******************/

}
