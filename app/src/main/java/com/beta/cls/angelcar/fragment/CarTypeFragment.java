package com.beta.cls.angelcar.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.GridView;


import com.beta.cls.angelcar.activity.PostActivity;
import com.beta.cls.angelcar.dao.CarDataTypeCollectionDao;
import com.beta.cls.angelcar.Adapter.CustomAdapterGridSub;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dialog.YearDialog;
import com.beta.cls.angelcar.interfaces.OnSelectData;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.manager.http.ApiCarService;
import com.beta.cls.angelcar.manager.http.HttpUsedCarManager;
import com.beta.cls.angelcar.model.InformationFromUser;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CarTypeFragment extends Fragment {
    private static String SAVE_INSTANCE_USER_INFO = "SAVE_INSTANCE_USER_INFO";
    private static String SAVE_INSTANCE_CAT_TYPE_SUB = "SAVE_INSTANCE_CAT_TYPE_SUB";
    private static String TAG = "CarTypeFragment";
    private static int DIALOG_YEAR = 99;
    
    
    @Bind(R.id.grid_sub_model) GridView mGridView;

    private CustomAdapterGridSub mAdapter;
    private InformationFromUser user;

    CarDataTypeCollectionDao gao;

    public static CarTypeFragment newInstance() {
        CarTypeFragment fragment = new CarTypeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_car_type, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            user = Parcels.unwrap(savedInstanceState.getParcelable(SAVE_INSTANCE_USER_INFO));
            gao = Parcels.unwrap(savedInstanceState.getParcelable(SAVE_INSTANCE_CAT_TYPE_SUB));
                mAdapter = new CustomAdapterGridSub(gao.getRows());
                mGridView.setAdapter(mAdapter);
            Log.i(TAG, "onActivityCreated: restore");

        }
        Log.i(TAG, "onActivityCreated: ");
        mGridView.setOnItemClickListener(selectTypeListener);

    }

    private void dialogSelectYear() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("YearDialog");
        if (fragment != null){
            ft.remove(fragment);
        }
        ft.addToBackStack(null);
        YearDialog dialog = new YearDialog();
        dialog.setTargetFragment(this,DIALOG_YEAR);
        dialog.show(getFragmentManager(),"YearDialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DIALOG_YEAR && resultCode == Activity.RESULT_OK && data != null){
            user.setYear(data.getIntExtra(YearDialog.ARG_YEAR,2016));
            BusProvider.getInstance().post(user);
            OnSelectData onSelectData = (OnSelectData) getActivity();
            onSelectData.onSelectedCallback(PostActivity.CALLBACK_CAR_TYPE);
        }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Subscribe
    public void getProduceData(InformationFromUser user){
        this.user = user;
        ApiCarService server = HttpUsedCarManager.getInstance().getService();
        Call<CarDataTypeCollectionDao> call = server.loadCarType(user.getBrand());
        call.enqueue(carDataTypeCollectionGaoCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_INSTANCE_USER_INFO,Parcels.wrap(user));
        if (gao == null) gao = new CarDataTypeCollectionDao();
        outState.putParcelable(SAVE_INSTANCE_CAT_TYPE_SUB,Parcels.wrap(gao));
    }

    /***************
     * Listener Zone
     * *************/
    AdapterView.OnItemClickListener selectTypeListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            user.setTypeSub(gao.getRows().get(position).getCarTypeSub());
            dialogSelectYear();
        }
    };

    Callback<CarDataTypeCollectionDao> carDataTypeCollectionGaoCallback = new Callback<CarDataTypeCollectionDao>() {
        @Override
        public void onResponse(Call<CarDataTypeCollectionDao> call, Response<CarDataTypeCollectionDao> response) {
            if (response.isSuccessful()) {
                gao = response.body();
                mAdapter = new CustomAdapterGridSub(gao.getRows());
                mGridView.setAdapter(mAdapter);
            } else {
                try {
                    Log.i(TAG, "onResponse: " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<CarDataTypeCollectionDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };
}






