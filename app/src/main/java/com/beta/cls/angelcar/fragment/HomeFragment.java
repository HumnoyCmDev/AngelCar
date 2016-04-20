package com.beta.cls.angelcar.fragment;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.beta.cls.angelcar.Adapter.ListViewPostAdapter;
import com.beta.cls.angelcar.activity.DetailCarActivity;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.anim.ResizeHeight;
import com.beta.cls.angelcar.dao.CarBrandDao;
import com.beta.cls.angelcar.dao.CarSubDao;
import com.beta.cls.angelcar.dao.CountCarCollectionDao;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.dao.PostCarDao;
import com.beta.cls.angelcar.dialog.FilterBrandDialog;
import com.beta.cls.angelcar.dialog.FilterSubDetailDialog;
import com.beta.cls.angelcar.dialog.FilterSubDialog;
import com.beta.cls.angelcar.dialog.YearDialog;
import com.beta.cls.angelcar.manager.Registration;
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.beta.cls.angelcar.model.FilterCarModel;

import org.parceler.Parcels;

import java.io.IOException;

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

//    public static final String ARG_DRAWABLE_LOGO = "logo";
    public static final String ARG_BRAND = "brand";
    public static final String ARG_SUB = "sub";
    public static final String ARG_SUB_DETAIL = "subDetail";


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
//    @Bind(R.id.filterToggleGear) ToggleButton toggleGear;

//    private HashMap<String,String> hashMapFilter;
    FilterCarModel filterCarModel;

    private static final String TAG = "HomeFragment";

    private PostCarCollectionDao dao;
    private ListViewPostAdapter adapter;

    public HomeFragment() {
        super();
    }

    public static HomeFragment newInstance() {
        //        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return new HomeFragment();
    }
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initInstances(rootView,savedInstanceState);
        return rootView;

    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
//        hashMapFilter = new HashMap<>();
        filterCarModel = new FilterCarModel();
        dao = new PostCarCollectionDao();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        ButterKnife.bind(this,rootView);

        adapter = new ListViewPostAdapter();
        adapter.setDao(dao);
        listView.setAdapter(adapter);
        mSwipeRefresh.setColorSchemeColors(
                Color.parseColor("#104F94"),
                Color.parseColor("#104F94"),
                Color.parseColor("#FFC11E"),
                Color.parseColor("#FFC11E"));

        mSwipeRefresh.setOnRefreshListener(pullRefresh);
        listView.setOnItemClickListener(onItemClickListViewListener);
        listView.setOnScrollListener(onScrollListener);

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
                }

            }
        });
        if (savedInstanceState == null){
            loadData();
        }
    }



    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initInstance();
        if (savedInstanceState == null){
            loadData();
        }else { // Restore
            dao = Parcels.unwrap(savedInstanceState.getParcelable(SAVE_STATE_GAO));
            adapter.setDao(dao);
            adapter.notifyDataSetChanged();
        }

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
                }

            }
        });
    }*/

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
        outState.putParcelable(SAVE_STATE_GAO,Parcels.wrap(dao));

    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        dao = Parcels.unwrap(savedInstanceState.getParcelable(SAVE_STATE_GAO));
//        adapter.setDao(dao);
//        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.filterBrand,R.id.filterSub,R.id.filterSubDetail,R.id.filterYear,R.id.buttonSearch})
    public void OnclickFilter(View v){
        @SuppressLint("CommitTransaction") FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()){

            case R.id.filterBrand:
                //Clear Object
                clearObject();
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
                if (isNullFilter(filterCarModel,0)) {
                    Fragment fragmentSub = getFragmentManager().findFragmentByTag("FilterSubDialog");
                    if (fragmentSub != null) {
                        ft.remove(fragmentSub);
                    }
                    ft.addToBackStack(null);
                    FilterSubDialog dialogSub = FilterSubDialog
                            .newInstance(filterCarModel);
                    dialogSub.setTargetFragment(this, REQUEST_CODE_SUB);
                    dialogSub.show(getFragmentManager(), "FilterSubDialog");
                }
                break;

            case R.id.filterSubDetail:
                if (isNullFilter(filterCarModel,1)){
                    Fragment fragmentSubDetail = getFragmentManager().findFragmentByTag("FilterSubDialog");
                    if (fragmentSubDetail != null) {
                        ft.remove(fragmentSubDetail);
                    }
                    ft.addToBackStack(null);
                    FilterSubDetailDialog dialogSub = FilterSubDetailDialog
                            .newInstance(filterCarModel);
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

    @OnClick({R.id.radioGearAll,R.id.radioGearAt,R.id.radioGearMt})
    void radioButtonGear(View view){
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.radioGearMt){
            filterCarModel.setGear(checked ? "1" : "0");
        }else if (view.getId() == R.id.radioGearAt){
            filterCarModel.setGear(checked ? "2" : "0");
        }else {// all
            filterCarModel.setGear(checked ? "0" : "0");
        }
        Log.i(TAG, "radioButtonGear: "+filterCarModel.getGear());
    }

    private boolean isNullFilter(FilterCarModel filterCarModel ,int mode){
        if (mode == 0){
            if (filterCarModel.getBrandDao() != null && !filterCarModel.getBrandDao().getBrandName().isEmpty())
            return true;
        }else {
            if (filterCarModel.getBrandDao() != null && filterCarModel.getSubDao() != null &&
                    !filterCarModel.getBrandDao().getBrandName().isEmpty() &&
                    !filterCarModel.getSubDao().getSubName().isEmpty())
            return true;
        }
        return false;
    }

    private void clearObject() {
        if (filterCarModel != null) {
            filterCarModel.clear();
            filterCarModel = null;
            filterCarModel = new FilterCarModel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case REQUEST_CODE_BRAND: // brand
                    CarBrandDao model = Parcels.unwrap(data.getParcelableExtra(ARG_BRAND));
//                    int drawableLogo = data.getIntExtra(ARG_DRAWABLE_LOGO,R.drawable.toyota);
//                    typedArray.recycle();
                    filterCarModel.setBrandDao(model);
                    tvBrand.setText(model.getBrandName());
                    tvSub.setText("All");
                    tvSubDetail.setText("All");
                    break;

                case REQUEST_CODE_SUB: // sub
                    CarSubDao modelSub = Parcels.unwrap(data.getParcelableExtra(ARG_SUB));
                    filterCarModel.setSubDao(modelSub);
                    tvSub.setText(modelSub.getSubName());
                    tvSubDetail.setText("All");
                    break;

                case REQUEST_CODE_SUB_DETAIL: // sub detail
                    CarSubDao modelSubDetail = Parcels.unwrap(data.getParcelableExtra(ARG_SUB_DETAIL));
                    filterCarModel.setSubDetailDao(modelSubDetail);
                    tvSubDetail.setText(modelSubDetail.getSubName());
                    break;

                case REQUEST_CODE_YEAR: // year
                    int resultYear = data.getIntExtra("TAG_YEAR",2016);
                    filterCarModel.setYear(resultYear);
                    tvYear.setText(String.valueOf(resultYear));
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
                PostCarDao item = dao.getListCar().get(position);
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
                dao = response.body();
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