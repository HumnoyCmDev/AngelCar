package com.beta.cls.angelcar.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;


import com.beta.cls.angelcar.gao.CarDataTypeCollectionGao;
import com.beta.cls.angelcar.gao.CarDataTypeGao;
import com.beta.cls.angelcar.Adapter.CustomAdapterGridSub;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.manager.http.ApiService;
import com.beta.cls.angelcar.manager.http.HttpPostManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CarTypeFragment extends Fragment {
    private static String ARG_DATA_CAR_TYPE = "ARGS_DATA_CART_TYPE";
    private static final String TAG = "CarTypeFragment";
    
    
    @Bind(R.id.grid_sub_model) GridView mGridView;

    private FragmentActivity myContext;
    private ProgressDialog mProgressDialog;
    private CustomAdapterGridSub mAdapter;
    private String dataCarType,dataCarTypeSub;

    List<CarDataTypeGao> posts;

    public static CarTypeFragment newInstance(String dataCarType) {
        Bundle args = new Bundle();
        args.putString(ARG_DATA_CAR_TYPE,dataCarType);
        CarTypeFragment fragment = new CarTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            dataCarType = args.getString(ARG_DATA_CAR_TYPE);
        }
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


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(v.getContext(), "--"+posts.get(position).getCarTypeSub(), Toast.LENGTH_SHORT).show();
                dataCarTypeSub = posts.get(position).getCarTypeSub();
                showDialog();


            }
        });
        
        ApiService server = HttpPostManager.getInstance().getService();
        Call<CarDataTypeCollectionGao> call = server.loadCarType(dataCarType);
        call.enqueue(new Callback<CarDataTypeCollectionGao>() {
            @Override
            public void onResponse(Call<CarDataTypeCollectionGao> call, Response<CarDataTypeCollectionGao> response) {
                if (response.isSuccess()){
                    posts = response.body().getRows();
                    mAdapter = new CustomAdapterGridSub(getActivity(), posts);
                    mGridView.setAdapter(mAdapter);
                }else {
                    try {
                        Log.i(TAG, "onResponse: "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CarDataTypeCollectionGao> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult: "+requestCode + " , "+ resultCode);

        if (resultCode == getActivity().RESULT_OK){

        }
    }

    public void showDialog() {
        final Dialog d = new Dialog(getActivity());
        d.setTitle("ปีรถของท่าน");
        d.setContentView(R.layout.dialog_year);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(2016);
        np.setValue(year);
        np.setMinValue(1950);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setWrapSelectorWheel(false);
        np.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int x = ((NumberPicker) v).getValue();
                Toast.makeText(v.getContext(), "Number selected" + x, Toast.LENGTH_SHORT).show(); //value ปี
                d.dismiss();

                FragmentManager fragManager = myContext.getSupportFragmentManager();
                CarDetailFragment PostFragment = CarDetailFragment.newInstance(dataCarType,dataCarTypeSub);
                FragmentTransaction transaction = fragManager.beginTransaction();
                transaction.replace(R.id.fragment_container, PostFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.i("test", "onClick: ");


            }
        });

        d.show();


    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
    }

}






