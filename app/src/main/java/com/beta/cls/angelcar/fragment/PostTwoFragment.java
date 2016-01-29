package com.beta.cls.angelcar.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;


import com.beta.cls.angelcar.manager.CarDataType;
import com.beta.cls.angelcar.Adapter.CustomAdapterGridSub;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.util.TotalCarData;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PostTwoFragment extends Fragment {
    private static String ARG_DATA_CAR_TYPE = "ARGS_DATA_CART_TYPE";

    @Bind(R.id.grid_sub_model) GridView mGridView;

    private FragmentActivity myContext;
    private ProgressDialog mProgressDialog;
    private CustomAdapterGridSub mAdapter;
    private String dataCarType,dataCarTypeSub;


    public static PostTwoFragment newInstance(String dataCarType) {
        Bundle args = new Bundle();
        args.putString(ARG_DATA_CAR_TYPE,dataCarType);
        PostTwoFragment fragment = new PostTwoFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_two_post, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new GetJsonCarType().execute();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView c = (TextView) v.findViewById(R.id.name_cartype);
                dataCarTypeSub = c.getText().toString();
                Toast.makeText(v.getContext(), dataCarTypeSub, Toast.LENGTH_SHORT).show();
                showDialog();
            }
        });


    }

    public void showDialog() {
        final Dialog d = new Dialog(getActivity());
        d.setTitle("ปีรถของท่าน");
        d.setContentView(R.layout.dialog_year);
        //Button b1 = (Button) d.findViewById(R.id. button1);
        //Button b2 = (Button) d.findViewById(R.id.button2);
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
                PostThreeFragment PostFragment = PostThreeFragment.newInstance(dataCarType,dataCarTypeSub);//new PostThreeFragment();
                FragmentTransaction transaction = fragManager.beginTransaction();
                transaction.replace(R.id.fragment_container, PostFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.i("test", "onClick: ");


            }
        });

        d.show();


    }

    private class GetJsonCarType extends AsyncTask<Void,Void,String>{
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
                    builder.url("http://www.usedcar.co.th/getdata.php?cartype="
                            + dataCarType).build();
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
            Gson gson = new Gson();
            TotalCarData blog = gson.fromJson(string, TotalCarData.class);
            List<CarDataType> posts = blog.getRows();
            mAdapter = new CustomAdapterGridSub(getActivity(), posts);
            mGridView.setAdapter(mAdapter);
            mProgressDialog.dismiss();

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
    }

}






