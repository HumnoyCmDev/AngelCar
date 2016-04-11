package com.beta.cls.angelcar.Adapter;

/**
 * Created by Developer on 12/15/2015. 14:35
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.dao.PostCarDao;
import com.beta.cls.angelcar.utils.LineUp;
import com.hndev.library.view.AngelCarPost;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListViewPostAdapter extends BaseAdapter implements Filterable {

    private PostCarCollectionDao dao;
    private List<PostCarDao> daoFilter;
    private Filter planetFilter;

    public void setDao(PostCarCollectionDao dao) {
        this.dao = dao;
        daoFilter = dao.getListCar();
    }

    public int getCount() {
        if (daoFilter == null) return 0;
        return daoFilter.size();
    }

    public PostCarDao getItem(int position) {
        return daoFilter.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        return position % 2 == 0 ?1:0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        PostCarDao postItem = getItem(position);
        int switching_position = getItemViewType(position);
        switch (switching_position){
            case 0: view = inFlaterLayoutRight(view,parent,postItem);
                break;
            case 1: view = inFlaterLayoutLeft(view,parent,postItem);
                break;
        }
        return view;
    }

    private View inFlaterLayoutLeft(View view, ViewGroup parent,PostCarDao postItem){
        ViewHolderItemLeft holder;
        if(view != null) {
            holder = (ViewHolderItemLeft) view.getTag();
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_left, parent, false);
            holder = new ViewHolderItemLeft(view);
            view.setTag(holder);
        }
            String topic = LineUp.getInstance().subTopic(postItem.getCarDetail());
            String detail = LineUp.getInstance().subDetail(postItem.getCarDetail());
            String carName = postItem.getCarName();
            String bast_url_image = postItem.getCarImagePath();
            String urlImage = bast_url_image.replaceFirst("chatcarimage","thumbnailcarimages");
            holder.angelCarPost.setPictureProduct("http://angelcar.com/"+urlImage);
            holder.angelCarPost.setTitle(topic);
            holder.angelCarPost.setDetails(carName +" "+ detail.replaceAll("<n>"," "));

        return view;
    }

    private View inFlaterLayoutRight(View view, ViewGroup parent,PostCarDao postItem){
        ViewHolderItemRight holder;
        if(view != null) {
            holder = (ViewHolderItemRight) view.getTag();
        }else {
            view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_right, parent, false);
            holder = new ViewHolderItemRight(view);
            view.setTag(holder);
        }
            String topic = LineUp.getInstance().subTopic(postItem.getCarDetail());
            String detail = LineUp.getInstance().subDetail(postItem.getCarDetail());
            String carName = postItem.getCarName();
            String bast_url_image = postItem.getCarImagePath() != null ? postItem.getCarImagePath():"";
            String urlImage = bast_url_image.replaceFirst("chatcarimage","thumbnailcarimages");
            holder.angelCarPost.setPictureProduct("http://angelcar.com/"+urlImage);
            holder.angelCarPost.setTitle(topic);
            holder.angelCarPost.setDetails(carName +" "+ detail.replaceAll("<n>"," "));

        return view;
    }

    public class ViewHolderItemLeft {
        @Bind(R.id.item_post) AngelCarPost angelCarPost;
        public ViewHolderItemLeft(View v) {
            ButterKnife.bind(this,v);
        }
    }

    public class ViewHolderItemRight {
        @Bind(R.id.item_post) AngelCarPost angelCarPost;
        public ViewHolderItemRight(View v) {
            ButterKnife.bind(this,v);
        }
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }

    /************
    *Inner Class*
    *************/

    private class PlanetFilter extends Filter {

        public PlanetFilter() {
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = dao.getListCar();
                results.count = dao.getListCar().size();
            }else {
                List<PostCarDao> nGao = new ArrayList<>();
                for (PostCarDao g : daoFilter){
                    if(g.getCarName().toUpperCase().startsWith(constraint.toString().toUpperCase())){
                        nGao.add(g);
                    }
                }

                results.values = nGao;
                results.count = nGao.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                daoFilter = (List<PostCarDao>) results.values;
                notifyDataSetChanged();
            }
        }
    }


}
