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
    MessageCollectionDao dao;


    private static final String TAG = "ChatSellFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_view_layout, container, false);
        initInstances(v,savedInstanceState);
        return v;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        dao = new MessageCollectionDao();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        ButterKnife.bind(this, rootView);
        adapter = new MessageAdapter();
        listView.setAdapter(adapter);

        loadMessage();
        listView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }


    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        adapter = new MessageAdapter();
//        listView.setAdapter(adapter);
//
////        if (savedInstanceState == null) {
//            loadMessage();
////        }
//        listView.setOnItemClickListener(onItemClickListener);
//    }


    private void loadMessage() {

        HttpManager.getInstance().getService()
                .messageClient(Registration.getInstance().getUserId())
                .enqueue(new retrofit2.Callback<MessageCollectionDao>() {
            @Override
            public void onResponse(Call<MessageCollectionDao> call, Response<MessageCollectionDao> response) {
               if (response.isSuccessful()) {
//                   dao = response.body();
                   dao.setListMessage(response.body().getListMessage());
                   adapter.setDao(dao.getListMessage());
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
//        loadMessage();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /***************
     *Listener Zone*
     ***************/
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MessageDao messageDao = dao.getListMessage().get(position);
            Call<PostCarCollectionDao> call =
                    HttpManager.getInstance().getService().loadCarModel(messageDao.getMessageCarId());
            call.enqueue(new CallbackLoadCarModel(getContext(),messageDao.getMessageFromUser()));
        }
    };

    /*****************
    *Inner Class Zone*
    ******************/

}
