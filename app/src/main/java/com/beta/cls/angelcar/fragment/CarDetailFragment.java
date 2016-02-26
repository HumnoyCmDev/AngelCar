package com.beta.cls.angelcar.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.beta.cls.angelcar.Adapter.CustomAdapterGridDetail;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.PostActivity;
import com.beta.cls.angelcar.gao.CarDetailGao;
import com.beta.cls.angelcar.gao.CarDetailCollectionGao;
import com.beta.cls.angelcar.interfaces.OnSelectData;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.model.InformationFromUser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.google.gson.Gson;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CarDetailFragment extends Fragment {
    private static String SAVE_INSTANCE_CAT_DETAIL = "SAVE_INSTANCE_CAT_DETAIL";

    private ProgressDialog mProgressDialog;

     @Bind(R.id.grid_detail_model) GridView mGridView;

    private CustomAdapterGridDetail mAdapter;

    CarDetailCollectionGao gao;
    private InformationFromUser user;

    public static CarDetailFragment newInstance() {
        CarDetailFragment fragment = new CarDetailFragment();
        return fragment;
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three_post, container, false);
        ButterKnife.bind(this,rootView);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                user.setTypeSubDetail(gao.getRows().get(position).getCarDetailSub());
                BusProvider.getInstance().post(user);
                OnSelectData onSelectData = (OnSelectData) getActivity();
                onSelectData.onSelectedCallback(PostActivity.CALLBACK_CAR_TYPE_DETAIL);


            }
        });

        return rootView;

    }

    private void showData(String jsonString) {
        Gson gson = new Gson();
        CarDetailCollectionGao blog = gson.fromJson(jsonString, CarDetailCollectionGao.class);
        gao = blog;
        mAdapter = new CustomAdapterGridDetail(getActivity(), gao.getRows());
        mGridView.setAdapter(mAdapter);

        Log.i(TAG, "showData: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private static final String TAG = "CarDetailFragment";

    @Subscribe
    public void getProduceData(InformationFromUser iuser){
        this.user = iuser;
        if (iuser.getTypeSub() == null) return;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mProgressDialog = new ProgressDialog(getActivity());

                mProgressDialog.setIndeterminate(false);
                mProgressDialog.show();
            }

            protected String doInBackground(Void... voids) {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request =
                        builder.url("http://www.usedcar.co.th/getdatathreefragment.php?cartype="
                                + user.getBrand()
                                + "&carsubtype="
                                + user.getTypeSub()).build();

                try {

                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                showData(string);

                mProgressDialog.dismiss();
            }

        }.execute();
    }


}