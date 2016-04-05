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
    public boolean isHeader(int position) {
        return position == -1;
    }

    public void setDao(PostCarCollectionDao dao) {
        this.dao = dao;
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
        PostCarDao item = dao.getRows().get(position);
        String bast_url_image = item.getCarImagePath() != null ? item.getCarImagePath():"";
        String urlImage = bast_url_image.replaceFirst("chatcarimage","thumbnailcarimages");
        Picasso.with(context).load("http://angelcar.com/"+urlImage).into(viewHolder.shopImage);
        viewHolder.carName.setText(item.getCarName());
    }

    @Override
    public int getItemCount() {
        if (dao == null) return 0;
        if (dao.getRows() == null) return 0;
        return dao.getRows().size();
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter(this,dao.getRows());
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
            shopAdapter.dao.getRows().clear();
            PostCarCollectionDao dao = new PostCarCollectionDao();
            dao.setRows((List<PostCarDao>) results.values);
            shopAdapter.setDao(dao);
            shopAdapter.notifyDataSetChanged();
        }
    }

}