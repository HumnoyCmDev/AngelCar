package com.beta.cls.angelcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.beta.cls.angelcar.Adapter.CustomAdapterGrid;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.model.InformationFromUser;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BrandFragment extends Fragment {

    private FragmentActivity myContext;
    @Bind(R.id.gridview) GridView gridview;

    public BrandFragment() {
        /*ข้อมูลรถ*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brand, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CustomAdapterGrid adapter = new CustomAdapterGrid(getActivity(),
                getIntsImageID());
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                InformationFromUser user = new InformationFromUser();
                user.setBrand(getBrand(position));

                CarTypeFragment TwoFragment = CarTypeFragment.newInstance(user);
                myContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, TwoFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    private int[] getIntsImageID() {
        return new int[]{R.drawable.toyota
                    , R.drawable.honda, R.drawable.nissan
                    , R.drawable.isuzu, R.drawable.mitsubishi
                    , R.drawable.chevrolet, R.drawable.ford
                    , R.drawable.mazda, R.drawable.benz
                    , R.drawable.audi, R.drawable.bmw
                    , R.drawable.hyundai, R.drawable.kia
                    , R.drawable.landrover, R.drawable.mini
                    , R.drawable.suzuki, R.drawable.volkswagen
                    , R.drawable.volvo, R.drawable.tata
                    , R.drawable.foton, R.drawable.hino
                    , R.drawable.holden, R.drawable.honda
                    , R.drawable.hummer, R.drawable.hyundai};
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
    }

    private String getBrand(int position){
        switch (position){
            case 0 : return "toyota";
            case 1 : return "honda";
            case 2 : return "nissan";
            case 3 : return "isuzu";
            case 4 : return "mitsubishi";
            case 5 : return "chevrolet";
            case 6 : return "ford";
            case 7 : return "mazda";
            case 8 : return "benz";
            case 9 : return "audi";
            case 10 : return "bmw";
            case 11 : return "hyundai";
            case 12 : return "kia";
            case 13 : return "land rover";
            case 14 : return "mini";
            case 15 : return "suzuki";
            case 16 : return "volkswagen";
            case 17 : return "volvo";
            case 18 : return "tata";
        }
        return "";
    }
}