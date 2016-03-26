package com.beta.cls.angelcar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beta.cls.angelcar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopActivity extends AppCompatActivity {

    @Bind(R.id.recycler_car) RecyclerView recyclerCar;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final GridLayoutManager manager = new GridLayoutManager(this,3);


        recyclerCar.setLayoutManager(manager);
        final ShopItemAdapter adapter = new ShopItemAdapter();
        recyclerCar.setAdapter(adapter);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //กำหนด จำนวน item ใน grid
                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });

    }


    public class ShopItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private static final int ITEM_VIEW_TYPE_HEADER = 0;
        private static final int ITEM_VIEW_TYPE_ITEM = 1;

        public boolean isHeader(int position) {
            return position == 0 || position == 5;
        }

        @Override
        public int getItemViewType(int position) {
            return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_VIEW_TYPE_HEADER){
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_line_brand,parent,false);

                HeaderViewHolder headerViewHolder = new HeaderViewHolder(v);
                return headerViewHolder;
            }

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_shop,parent,false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // coding
        }

        @Override
        public int getItemCount() {
            return 50+1;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public ViewHolder(View itemView) {
                super(itemView);
//                ButterKnife.bind(this,itemView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Log.i("Shop", "onClick: "+getAdapterPosition());
            }
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder{
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
