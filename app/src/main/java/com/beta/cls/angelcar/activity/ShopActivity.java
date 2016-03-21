package com.beta.cls.angelcar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beta.cls.angelcar.R;

import butterknife.Bind;
import butterknife.ButterKnife;
@Deprecated
public class ShopActivity extends AppCompatActivity {

    @Bind(R.id.image_profile_shop) ImageView profileImage;
    @Bind(R.id.recyclerShop) RecyclerView recyclerShop;
    @Bind(R.id.tvName_shop) TextView tvName;
    @Bind(R.id.tvDetail_shop) TextView tvNameShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        recyclerShop.setLayoutManager(new GridLayoutManager(this,3));
        ShopItemAdapter adapter = new ShopItemAdapter();
        recyclerShop.setAdapter(adapter);
       

    }


    public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder>{

        @Override
        public ShopItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_shop,parent,false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ShopItemAdapter.ViewHolder holder, int position) {
            // coding
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.itemPictureShop) ImageView itemPic;
            @Bind(R.id.itemTextShop) TextView itemTextShop;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);

            }
        }
    }
}
