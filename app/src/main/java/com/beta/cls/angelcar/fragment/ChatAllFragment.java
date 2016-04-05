package com.beta.cls.angelcar.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.beta.cls.angelcar.Adapter.MessageAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.CarIdDao;
import com.beta.cls.angelcar.dao.MessageAdminCollectionDao;
import com.beta.cls.angelcar.dao.MessageCollectionDao;
import com.beta.cls.angelcar.dao.MessageDao;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.dialog.DeleteChatDialog;
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
// TODO แก้ไขการโหลดข้อมูล แชท Sell Buy ให้ดึงข้อมูลจากที่นี้เลย
public class ChatAllFragment extends Fragment{
    @Bind(R.id.list_view)
    ListView listView;
    private static final String TAG = "ChatAllFragment";

    MessageManager messageManager;
    MessageAdapter adapter;
//    PostCarDao item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageManager = new MessageManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_view_layout,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initInstance();
        initListener();
        loadCarId();
//        initDataMessage();

    }

    private void loadCarId() {
        Call<CarIdDao> loadCarId =
                HttpManager.getInstance().getService().loadCarId(
                        Registration.getInstance().getShopRef());
        loadCarId.enqueue(carIdDaoCallback);

    }

    private void initDataMessage(final CarIdDao body) {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                Call<MessageCollectionDao> callClient =
                        HttpManager.getInstance().getService()
                                .messageClient(Registration.getInstance().getUserId());

                Call<MessageAdminCollectionDao> callAdmin =
                        HttpManager.getInstance().getService()
                                .messageAdmin(body.getAllCarId());

                try {
                    Response<MessageCollectionDao> responseClient = callClient.execute();
                    if (responseClient.isSuccessful()){
                        messageManager.setMessageDao(responseClient.body());
                    }else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Response<MessageAdminCollectionDao> responseAdmin = callAdmin.execute();
                    if (responseAdmin.isSuccessful()){
                        messageManager.updateDataToLastPosition(
                                responseAdmin.body().getMessageAdminGao()
                                        .convertToMessageCollectionGao());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.setGao(messageManager.getMessageDao().getMessage());
                adapter.notifyDataSetChanged();
            }
        }.execute();


    }

    private void initInstance() {
        adapter = new MessageAdapter();
        listView.setAdapter(adapter);
    }

    private void initListener() {
        listView.setOnItemLongClickListener(onItemLongClickListener);
        listView.setOnItemClickListener(onItemClickListener);
    }

    private void deleteDialog(MessageDao dao) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("DeleteChatDialog");
        if (fragment != null){
            ft.remove(fragment);
        }
        ft.addToBackStack(null);
        DeleteChatDialog deleteChatDialog =
                DeleteChatDialog.newInstance(dao.getMessageFromUser());
        deleteChatDialog.show(getFragmentManager(),"DeleteChatDialog");
    }

    /**************
     *Listener Zone*
     ***************/

    Callback<CarIdDao> carIdDaoCallback = new Callback<CarIdDao>() {
        @Override
        public void onResponse(Call<CarIdDao> call, Response<CarIdDao> response) {
            if (response.isSuccessful()) {
                initDataMessage(response.body());
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

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MessageDao dao = messageManager.getMessageDao().getMessage().get(position);
            Call<PostCarCollectionDao> call =
                    HttpManager.getInstance().getService().loadCarModel(dao.getMessageCarId());
            call.enqueue(new CallbackLoadCarModel(getContext(),dao.getMessageFromUser()));

        }

    };

    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getContext(), "OnItem Long Click" + position, Toast.LENGTH_LONG).show();
            MessageDao dao = messageManager.getMessageDao().getMessage().get(position);
//            deleteDialog(dao);
            return true;
        }
    };


    /*****************
     *Inner Class Zone*
     ******************/


}
