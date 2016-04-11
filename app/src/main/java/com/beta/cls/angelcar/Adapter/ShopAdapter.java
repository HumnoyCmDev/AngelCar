package com.beta.cls.angelcar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.dao.PostCarDao;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoyDeveloper on 28/3/59. 16:41
 */
public class ShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    private PostCarCollectionDao dao;
    private Filter planetFilter;
    private Context context;
    private RecyclerOnItemClickListener recyclerOnItemClickListener;

    public boolean isHeader(int position) {
        return position == -1;// +
    }

    public void setDao(PostCarCollectionDao dao) {
        this.dao = dao;
    }

    public void setOnclickListener(RecyclerOnItemClickListener recyclerOnItemClickListener){
        this.recyclerOnItemClickListener = recyclerOnItemClickListener;
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
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // coding
        ViewHolder viewHolder = (ViewHolder) holder;
        PostCarDao item = dao.getListCar().get(position);
        String bast_url_image = item.getCarImagePath() != null ? item.getCarImagePath():"";
        String urlImage = bast_url_image.replaceFirst("chatcarimage","thumbnailcarimages");

        Glide.with(context)
                .load("http://angelcar.com/"+urlImage)
                .placeholder(R.drawable.loading)
                .into(viewHolder.shopImage);

        viewHolder.carName.setText(item.getCarName());
    }

    @Override
    public int getItemCount() {
        if (dao == null) return 0;
        if (dao.getListCar() == null) return 0;
        return dao.getListCar().size();
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter(this,dao.getListCar());
        return planetFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.itemShopSetting) ImageView shopSetting;
        @Bind(R.id.itemShopImage) ImageView shopImage;
        @Bind(R.id.itemShopCar) TextView carName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            shopSetting.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (recyclerOnItemClickListener != null) {
                if (v instanceof ImageView) { // setting
                    recyclerOnItemClickListener
                            .OnClickSettingListener(v, getAdapterPosition());
                } else { // item
                    recyclerOnItemClickListener
                            .OnClickItemListener(v,getAdapterPosition());
                }
            }
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    /******************
     *Inner Class Zone*
     ******************/
    private class PlanetFilter extends Filter {
        private ShopAdapter shopAdapter;
        private  List<PostCarDao> originalDao;
        private  List<PostCarDao> daoFilterList;

        public PlanetFilter(ShopAdapter shopAdapter,  List<PostCarDao> originalDao) {
            this.shopAdapter = shopAdapter;
            this.originalDao = originalDao;
            daoFilterList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            daoFilterList.clear();
            FilterResults results = new FilterResults();
            if (constraint.length() == 0){
                daoFilterList.addAll(originalDao);
            }else {
                for (PostCarDao g : originalDao){
                    if(g.getCarName().toUpperCase().startsWith(constraint.toString().toUpperCase())){
                        daoFilterList.add(g);
                    }
                }
            }
            results.values = daoFilterList;
            results.count = daoFilterList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shopAdapter.dao.getListCar().clear();
            PostCarCollectionDao dao = new PostCarCollectionDao();
            dao.setListCar((List<PostCarDao>) results.values);
            shopAdapter.setDao(dao);
            shopAdapter.notifyDataSetChanged();
        }
    }

    public interface RecyclerOnItemClickListener{
        void OnClickItemListener(View v, int position);
        void OnClickSettingListener(View v, int position);
    }

}