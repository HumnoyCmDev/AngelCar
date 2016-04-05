package com.beta.cls.angelcar.fragment;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.beta.cls.angelcar.Adapter.ListViewPostAdapter;
import com.beta.cls.angelcar.activity.DetailCarActivity;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.anim.ResizeHeight;
import com.beta.cls.angelcar.dao.CountCarCollectionDao;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.dao.PostCarDao;
import com.beta.cls.angelcar.dialog.FilterBrandDialog;
import com.beta.cls.angelcar.dialog.FilterSubDetailDialog;
import com.beta.cls.angelcar.dialog.FilterSubDialog;
import com.beta.cls.angelcar.dialog.YearDialog;
import com.beta.cls.angelcar.manager.Registration;
import com.beta.cls.angelcar.manager.http.HttpManager;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    public static final String SAVE_STATE_GAO = "SAVE_STATE_GAO";
    public static final int REQUEST_CODE_BRAND = 1;
    public static final int REQUEST_CODE_SUB = 2;
    public static final int REQUEST_CODE_SUB_DETAIL = 3;
    public static final int REQUEST_CODE_YEAR = 4;


    @Bind(R.id.list_view) ListView listView;
    @Bind(R.id.swipe_container) SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.ctFilter) ScrollView ctFilter;
    @Bind(R.id.btFilter) ImageView btFilter;
    @Bind(R.id.countCarAll) TextView countCarAll;
    @Bind(R.id.countCarDay) TextView countCarDay;
    @Bind(R.id.countCarMonth) TextView countCarMonth;

    //Filter
    @Bind(R.id.filterBrand) TextView tvBrand;
    @Bind(R.id.filterSub) TextView tvSub;
    @Bind(R.id.filterSubDetail) TextView tvSubDetail;
    @Bind(R.id.filterYear) TextView tvYear;
    @Bind(R.id.filterToggleGear) ToggleButton toggleGear;
    HashMap<String,String> hashMapFilter;


//    Animation animDown,animUp;

    private static final String TAG = "HomeFragment";

    private PostCarCollectionDao gao;
    private ListViewPostAdapter adapter;

    public HomeFragment() {
        super();
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hashMapFilter = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initInstance();
        if (savedInstanceState == null){
            loadData();
        }else { // Restore
            gao = Parcels.unwrap(savedInstanceState.getParcelable(SAVE_STATE_GAO));
            adapter.setDao(gao);
            adapter.notifyDataSetChanged();
        }

//        animDown = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_slide_down);
//        animUp = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_slide_up);

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctFilter.getVisibility() == View.GONE) {
                    ResizeHeight resizeHeight = new ResizeHeight(ctFilter,1000,true);
                    resizeHeight.setInterpolator(new BounceInterpolator());
                    resizeHeight.setDuration(1000);
                    ctFilter.startAnimation(resizeHeight);
                    ctFilter.setVisibility(View.VISIBLE);
                } else {
                    ResizeHeight resizeHeight = new ResizeHeight(ctFilter,0,false);
                    resizeHeight.setDuration(500);
                    ctFilter.startAnimation(resizeHeight);
//                    ctFilter.setVisibility(View.GONE);
//                    ctFilter.startAnimation(animUp);
                }

            }
        });
    }

    private void initInstance() {
        adapter = new ListViewPostAdapter();
        listView.setAdapter(adapter);
        mSwipeRefresh.setColorSchemeColors(
                Color.parseColor("#104F94"),
                Color.parseColor("#104F94"),
                Color.parseColor("#FFC11E"),
                Color.parseColor("#FFC11E"));

        mSwipeRefresh.setOnRefreshListener(pullRefresh);
        listView.setOnItemClickListener(onItemClickListViewListener);
        listView.setOnScrollListener(onScrollListener);

    }

    private void loadData(){

        Call<PostCarCollectionDao> call =
                HttpManager.getInstance().getService().loadPost();
        call.enqueue(callbackLoadPostCar);

        // load count car
        Call<CountCarCollectionDao> callLoadCountCar =
                HttpManager.getInstance().getService().loadCountCar();
        callLoadCountCar.enqueue(countCarCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_STATE_GAO,Parcels.wrap(gao));

    }

    @OnClick({R.id.filterBrand,R.id.filterSub,R.id.filterSubDetail,R.id.filterYear,R.id.buttonSearch})
    public void OnclickFilter(View v){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.filterBrand:
                Fragment fragment = getFragmentManager().findFragmentByTag("FilterBrandDialog");
                if (fragment != null){
                    ft.remove(fragment);
                }
                ft.addToBackStack(null);
                FilterBrandDialog dialog = new FilterBrandDialog();
                dialog.setTargetFragment(this,REQUEST_CODE_BRAND);
                dialog.show(getFragmentManager(),"FilterBrandDialog");
                break;
            case R.id.filterSub:
                if (hashMapFilter.containsKey("BRAND")) {
                    Fragment fragmentSub = getFragmentManager().findFragmentByTag("FilterSubDialog");
                    if (fragmentSub != null) {
                        ft.remove(fragmentSub);
                    }
                    ft.addToBackStack(null);
                    FilterSubDialog dialogSub = FilterSubDialog.newInstance(hashMapFilter.get("BRAND"));
                    dialogSub.setTargetFragment(this, REQUEST_CODE_SUB);
                    dialogSub.show(getFragmentManager(), "FilterSubDialog");
                }
                break;
            case R.id.filterSubDetail:
                if (hashMapFilter.containsKey("BRAND") &&
                        hashMapFilter.containsKey("SUB")){
                    Fragment fragmentSubDetail = getFragmentManager().findFragmentByTag("FilterSubDialog");
                    if (fragmentSubDetail != null) {
                        ft.remove(fragmentSubDetail);
                    }
                    ft.addToBackStack(null);
                    FilterSubDetailDialog dialogSub = FilterSubDetailDialog.newInstance(hashMapFilter.get("BRAND"),hashMapFilter.get("SUB"));
                    dialogSub.setTargetFragment(this, REQUEST_CODE_SUB_DETAIL);
                    dialogSub.show(getFragmentManager(), "FilterSubDialog");
                }
                break;
            case R.id.filterYear:
                Fragment fragmentYear = getFragmentManager().findFragmentByTag("YearDialog");
                if (fragmentYear != null){
                    ft.remove(fragmentYear);
                }
                ft.addToBackStack(null);
                YearDialog dialogYear = new YearDialog();
                dialogYear.setTargetFragment(this,REQUEST_CODE_YEAR);
                dialogYear.show(getFragmentManager(),"YearDialog");
                break;
            default: // button Search
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_BRAND: // brand
                    String result = data.getStringExtra("BRAND");
                    tvBrand.setText(result);
                    filterChecked("BRAND",result);
                    tvSub.setText("All");
                    tvSubDetail.setText("All");
                    if (hashMapFilter.containsKey("SUB")) hashMapFilter.remove("SUB");
                    if (hashMapFilter.containsKey("SUB_DETAIL")) hashMapFilter.remove("SUB_DETAIL");
                    break;
                case REQUEST_CODE_SUB: // sub
                    String resultSub = data.getStringExtra("SUB");
                    tvSub.setText(resultSub);
                    filterChecked("SUB",resultSub);
                    tvSubDetail.setText("All");
                    if (hashMapFilter.containsKey("SUB_DETAIL")) hashMapFilter.remove("SUB_DETAIL");
                    break;
                case REQUEST_CODE_SUB_DETAIL: // sub detail
                    String resultSubDetail = data.getStringExtra("SUB_DETAIL");
                    tvSubDetail.setText(resultSubDetail);
                    filterChecked("SUB_DETAIL",resultSubDetail);
                    break;
                case REQUEST_CODE_YEAR: // year
                    int resultYear = data.getIntExtra("TAG_YEAR",2016);
                    tvYear.setText(String.valueOf(resultYear));
                    filterChecked("TAG_YEAR",String.valueOf(resultYear));
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void filterChecked(String key,String values ){
            hashMapFilter.put(key,values);
    }

    /**************
    *Listener Zone*
    ***************/
    SwipeRefreshLayout.OnRefreshListener pullRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mSwipeRefresh.setEnabled(firstVisibleItem == 0);
        }
    };
    AdapterView.OnItemClickListener onItemClickListViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (Registration.getInstance().getUserId() != null) {
                PostCarDao item = gao.getRows().get(position);
                Intent intent = new Intent(getActivity(), DetailCarActivity.class);
                intent.putExtra("PostCarDao", Parcels.wrap(item));
                intent.putExtra("intentForm", 0);
                intent.putExtra("messageFromUser",Registration.getInstance().getUserId());
                startActivity(intent);
            }else {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("RegistrationAlertFragment");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                RegistrationAlertFragment fragment = RegistrationAlertFragment.newInstance();
                fragment.setCancelable(false);
                fragment.show(ft, "RegistrationAlertFragment");
            }

        }
    };

    Callback<PostCarCollectionDao> callbackLoadPostCar = new Callback<PostCarCollectionDao>() {
        @Override
        public void onResponse(Call<PostCarCollectionDao> call, Response<PostCarCollectionDao> response) {
            mSwipeRefresh.setRefreshing(false);
            if (response.isSuccessful()) {
                gao = response.body();
                adapter.setDao(response.body());
                adapter.notifyDataSetChanged();
//                adapter.getFilter().filter("toyota");
            } else {
                Toast.makeText(getActivity(),
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<PostCarCollectionDao> call, Throwable t) {
            mSwipeRefresh.setRefreshing(false);
            Toast.makeText(getActivity(),
                    t.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    };

    Callback<CountCarCollectionDao> countCarCallback = new Callback<CountCarCollectionDao>() {
        @Override
        public void onResponse(Call<CountCarCollectionDao> call, Response<CountCarCollectionDao> response) {
            if (response.isSuccessful()) {
                CountCarCollectionDao.CountCarGao countCarGao = response.body().getRows().get(0);
                countCarAll.setText("ทั้งหมด " + countCarGao.getCountAll() + " คัน");
                countCarMonth.setText("เดือนนี้ " + countCarGao.getCountMonth() + " คัน");
                countCarDay.setText("วันนี้ " + countCarGao.getCountDay() + " คัน");
            } else {
                try {
                    Log.i(TAG, "onResponse: " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<CountCarCollectionDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    /*****************
    *Inner Class Zone*
    ******************/

}