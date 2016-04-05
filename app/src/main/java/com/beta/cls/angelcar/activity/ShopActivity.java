package com.beta.cls.angelcar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beta.cls.angelcar.Adapter.ShopAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.dao.ProfileDao;
import com.beta.cls.angelcar.dao.ShopCollectionDao;
import com.beta.cls.angelcar.manager.Registration;
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity {
    private static final String TAG = "ShopActivity";

    @Bind(R.id.recycler_car) RecyclerView recyclerCar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.shopProfilePicture) ImageView shopProfilePicture;
    @Bind(R.id.tvShopName) TextView shopName;
    @Bind(R.id.tvShopNumber) TextView shopNumber;
    @Bind(R.id.tvShopDescription) TextView shopDescription;

    private GridLayoutManager manager;
    private ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        initToolbar();
        initInstance();
        loadData();

    }

    private void loadData() {
        String userRef = Registration.getInstance().getUserId();
        String shopRef = Registration.getInstance().getShopRef();
        Call<ShopCollectionDao> call = HttpManager.getInstance()
                .getService().loadDataShop(userRef, shopRef);
        call.enqueue(new Callback<ShopCollectionDao>() {
            @Override
            public void onResponse(Call<ShopCollectionDao> call, Response<ShopCollectionDao> response) {
                if (response.isSuccessful()){
                    initProfile(response.body().getProfileDao());
                    PostCarCollectionDao dao = new PostCarCollectionDao();
                    dao.setRows(response.body().getPostCarDao());
                    adapter.setDao(dao);
                }else {
                    try {
                        Log.e(TAG, "onResponse:"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShopCollectionDao> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    private void initProfile(ProfileDao profileDao){
        Picasso.with(ShopActivity.this)
                .load(""+profileDao.getShopLogo())
                .placeholder(R.drawable.loading)
                .into(shopProfilePicture);
        shopName.setText(profileDao.getShopName());
        shopDescription.setText(profileDao.getShopDescription());
        shopNumber.setText(profileDao.getShopNumber());
    }


    private void initInstance() {
       manager = new GridLayoutManager(this,3);
        recyclerCar.setLayoutManager(manager);
        adapter = new ShopAdapter();
        recyclerCar.setAdapter(adapter);
        manager.setSpanSizeLookup(spanSizeLookupManager);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.buttonBack)
    public void onClickBackHome() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /***************
     *Listener Zone*
     ***************/
    GridLayoutManager.SpanSizeLookup spanSizeLookupManager = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            //กำหนด จำนวน item ใน grid
            return adapter.isHeader(position) ? manager.getSpanCount() : 1;
        }
    };

    /*****************
    *Inner Class Zone*
    ******************/

}
