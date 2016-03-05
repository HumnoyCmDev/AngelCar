package com.beta.cls.angelcar.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beta.cls.angelcar.Adapter.MessageAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.MessageAdminCollectionGao;
import com.beta.cls.angelcar.gao.MessageCollectionGao;
import com.beta.cls.angelcar.gao.MessageGao;
import com.beta.cls.angelcar.manager.MessageManager;
import com.beta.cls.angelcar.manager.http.HttpChatManager;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by humnoy on 26/1/59.
 */
public class ChatAllFragment extends Fragment{
    @Bind(R.id.list_view)
    ListView listView;
    private static final String TAG = "ChatAllFragment";

    private List<MessageGao> message;

    MessageManager messageManager;
    MessageAdapter adapter;

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
        initDataMessage();

    }

    private void initDataMessage() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                Call<MessageCollectionGao> callClient =
                        HttpChatManager.getInstance().getService()
                                .messageClient("2016010700001");
                Call<MessageAdminCollectionGao> callAdmin =
                        HttpChatManager.getInstance().getService()
                                .messageAdmin("26");

                try {
                    Response<MessageCollectionGao> responseClient = callClient.execute();
                    if (responseClient.isSuccess()){
                        messageManager.setMessageGao(responseClient.body());
                    }else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Response<MessageAdminCollectionGao> responseAdmin = callAdmin.execute();
                    if (responseAdmin.isSuccess()){
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
                adapter.setGao(messageManager.getMessageGao().getMessage());
                adapter.notifyDataSetChanged();
            }
        }.execute();


    }

    private void initInstance() {
        adapter = new MessageAdapter();
        listView.setAdapter(adapter);
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });
    }


}
