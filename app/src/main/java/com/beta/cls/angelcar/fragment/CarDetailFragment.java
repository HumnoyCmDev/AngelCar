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
import com.beta.cls.angelcar.gao.CarDetailGao;
import com.beta.cls.angelcar.gao.CarDetailCollectionGao;
import com.beta.cls.angelcar.model.InformationFromUser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.google.gson.Gson;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class CarDetailFragment extends Fragment {
    private static String ARG_InformationFromUser = "ARG_InformationFromUser";

    private FragmentActivity myContext;
    private ProgressDialog mProgressDialog;
    private GridView mGridView;
    private CustomAdapterGridDetail mAdapter;

    private List<CarDetailGao> posts;
    private InformationFromUser user;



    public static CarDetailFragment newInstance(InformationFromUser user) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_InformationFromUser, Parcels.wrap(user));
        CarDetailFragment fragment = new CarDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three_post, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.grid_detail_model);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(v.getContext(), posts.get(position).getCarDetailSub(), Toast.LENGTH_SHORT).show();
                user.setTypeSubDetail(posts.get(position).getCarDetailSub());

                AllPostFragment fragment = AllPostFragment.newInstance(user);
                myContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();


            }
        });


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

        return rootView;

    }

    private void showData(String jsonString) {
        Gson gson = new Gson();
        CarDetailCollectionGao blog = gson.fromJson(jsonString, CarDetailCollectionGao.class);

        /*StringBuilder builder = new StringBuilder();
        builder.setLength(0);*/

       posts = blog.getRows();

        mAdapter = new CustomAdapterGridDetail(getActivity(), posts);
        mGridView.setAdapter(mAdapter);
    }


    public void show() {

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