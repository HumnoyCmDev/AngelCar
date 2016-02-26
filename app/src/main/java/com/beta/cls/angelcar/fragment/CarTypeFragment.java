package com.beta.cls.angelcar.fragment;

import android.app.Activity;
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
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.beta.cls.angelcar.model.InformationFromUser;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CarTypeFragment extends Fragment {
    private static String ARG_InformationFromUser = "ARG_InformationFromUser";
    private static String TAG = "CarTypeFragment";
    private static int DIALOG_YEAR = 99;
    
    
    @Bind(R.id.grid_sub_model) GridView mGridView;

    private FragmentActivity myContext;
    private ProgressDialog mProgressDialog;
    private CustomAdapterGridSub mAdapter;
    private InformationFromUser user;

    private List<CarDataTypeGao> posts;

    public static CarTypeFragment newInstance(InformationFromUser user) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_InformationFromUser, Parcels.wrap(user));
        CarTypeFragment fragment = new CarTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            user = Parcels.unwrap(args.getParcelable(ARG_InformationFromUser));
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
                user.setTypeSub(posts.get(position).getCarTypeSub());
//                showDialog();

                dialogSelectYear();

            }
        });
        
        ApiService server = HttpManager.getInstance().getService();
        Call<CarDataTypeCollectionGao> call = server.loadCarType(user.getBrand());
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

    private void dialogSelectYear() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag("YearFragmentDialog");
        if (fragment != null){
            ft.remove(fragment);
        }
        ft.addToBackStack(null);
        YearFragmentDialog dialog = new YearFragmentDialog();
        dialog.setTargetFragment(this,DIALOG_YEAR);
        dialog.show(getFragmentManager(),"YearFragmentDialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DIALOG_YEAR && resultCode == Activity.RESULT_OK && data != null){
            CarDetailFragment fragment = CarDetailFragment.newInstance(user);
            myContext.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    private Dialog d;
    private NumberPicker np;
    int intYear = 0;

    public void showDialog() {
        d = new Dialog(getActivity());
        d.setTitle("ปีรถของท่าน");
        d.setContentView(R.layout.dialog_year);
        np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(2016);
        np.setValue(Calendar.getInstance().get(Calendar.YEAR));
        np.setMinValue(1950);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setWrapSelectorWheel(false);


        np.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intYear = ((NumberPicker) v).getValue();
                Toast.makeText(v.getContext(), "Number selected" + intYear, Toast.LENGTH_SHORT).show(); //value ปี

                user.setYear(intYear);

                CarDetailFragment fragment = CarDetailFragment.newInstance(user);
                myContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                d.dismiss();


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






