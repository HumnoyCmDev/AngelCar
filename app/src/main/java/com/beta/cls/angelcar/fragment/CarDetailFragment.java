package com.beta.cls.angelcar.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.beta.cls.angelcar.Adapter.CustomAdapterGridDetail;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.PostActivity;
import com.beta.cls.angelcar.dao.CarDetailCollectionDao;
import com.beta.cls.angelcar.interfaces.OnSelectData;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.model.InformationFromUser;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CarDetailFragment extends Fragment {
    private static String SAVE_INSTANCE_CAT_DETAIL = "SAVE_INSTANCE_CAT_DETAIL";

    private ProgressDialog mProgressDialog;

     @Bind(R.id.grid_detail_model) GridView mGridView;

    private CustomAdapterGridDetail mAdapter;

    CarDetailCollectionDao gao;
    private InformationFromUser user;

    private static final String TAG = "CarDetailFragment";

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
        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_INSTANCE_CAT_DETAIL,Parcels.wrap(gao));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            gao = Parcels.unwrap(savedInstanceState.getParcelable(SAVE_INSTANCE_CAT_DETAIL));
            mAdapter = new CustomAdapterGridDetail(getActivity(), gao.getRows());
            mGridView.setAdapter(mAdapter);
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                user.setTypeSubDetail(gao.getRows().get(position).getCarDetailSub());
                BusProvider.getInstance().post(user);
                OnSelectData onSelectData = (OnSelectData) getActivity();
                onSelectData.onSelectedCallback(PostActivity.CALLBACK_CAR_TYPE_DETAIL);

            }
        });

    }

    private void showData(String jsonString) {
        Gson gson = new Gson();
        gao = gson.fromJson(jsonString, CarDetailCollectionDao.class);
        mAdapter = new CustomAdapterGridDetail(getActivity(), gao.getRows());
        mGridView.setAdapter(mAdapter);
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


    @Subscribe
    public void getProduceData(InformationFromUser iuser) {
        this.user = iuser;
        if (iuser.getTypeSub() == null) return;
        new LoadDetail().execute();
    }


    /***************
    * Listener Zone
    * *************/


    /***************
     * InnerClass Zone
     * *************/

    class LoadDetail extends AsyncTask<Void,Void,String>{
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

    }


}