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
import com.beta.cls.angelcar.dao.CarIdDao;
import com.beta.cls.angelcar.dao.MessageAdminCollectionDao;
import com.beta.cls.angelcar.dao.MessageDao;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.manager.CallbackLoadCarModel;
import com.beta.cls.angelcar.manager.MessageManager;
import com.beta.cls.angelcar.manager.Registration;
import com.beta.cls.angelcar.manager.http.HttpManager;


import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by humnoy on 26/1/59.
 */
public class ChatBuyFragment extends Fragment {
//    public static final String ARGS_MESSAGE_BY = "shop";

    @Bind(R.id.list_view) ListView listView;

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
        Call<CarIdDao> loadCarId =
                HttpManager.getInstance().getService().loadCarId(
                        Registration.getInstance().getShopRef());
        loadCarId.enqueue(carIdDaoCallback);

    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDao messageDao =
                        messageManager.getMessageDao().getMessage().get(position);
                Call<PostCarCollectionDao> call =
                        HttpManager.getInstance().getService().loadCarModel(messageDao.getMessageCarId());
                call.enqueue(new CallbackLoadCarModel(getContext(),messageDao.getMessageFromUser()));
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

    /**************
    *Listener Zone*
    ***************/
    Callback<CarIdDao> carIdDaoCallback = new Callback<CarIdDao>() {
        @Override
        public void onResponse(Call<CarIdDao> call, Response<CarIdDao> response) {
            if (response.isSuccessful()) {
                Call<MessageAdminCollectionDao> callMessageAdmin =
                        HttpManager.getInstance().getService()
                                .messageAdmin(response.body().getAllCarId());
                callMessageAdmin.enqueue(adminCollectionDaoCallback);
            }else {
                try {
                    Log.i(TAG, "onResponse: "+response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<CarIdDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    Callback<MessageAdminCollectionDao> adminCollectionDaoCallback = new Callback<MessageAdminCollectionDao>() {
        @Override
        public void onResponse(Call<MessageAdminCollectionDao> call, Response<MessageAdminCollectionDao> response) {
            if (response.isSuccessful()) {
                messageManager.setMessageDao(response.body()
                        .getMessageAdminGao()
                        .convertToMessageCollectionGao());
                adapter.setGao(messageManager.getMessageDao().getMessage());
                adapter.notifyDataSetChanged();

            } else {
                try {
                    Log.i(TAG, "onResponse: error" + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<MessageAdminCollectionDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    /******************
     *Inner Class Zone*
     ******************/

}

