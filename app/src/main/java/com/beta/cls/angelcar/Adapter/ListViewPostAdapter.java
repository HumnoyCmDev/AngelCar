package com.beta.cls.angelcar.Adapter;

/**
 * Created by ABaD on 12/15/2015.
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.FeedPostGao;
import com.hndev.library.view.AngelCarPost;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListViewPostAdapter extends BaseAdapter {

    private List<FeedPostGao> postItems;
    public ListViewPostAdapter(List<FeedPostGao> postItems) {
        this.postItems = postItems;
    }

    public int getCount() {
        if(postItems == null) return 0;
        return postItems.size();
    }

    public FeedPostGao getItem(int position) {
        return postItems.get(position);
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
        FeedPostGao postItem = getItem(position);
        int switching_position = getItemViewType(position);
        switch (switching_position){
            case 0: view = inFlaterLayoutRight(view,parent,postItem);
                break;
            case 1: view = inFlaterLayoutLeft(view,parent,postItem);
                break;
        }
        return view;
    }

    private View inFlaterLayoutLeft(View view, ViewGroup parent,FeedPostGao postItem){
        ViewHolderItemLeft holder;
        if(view != null) {
            holder = (ViewHolderItemLeft) view.getTag();
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_left, parent, false);
            holder = new ViewHolderItemLeft(view);
            view.setTag(holder);
        }
        holder.angelCarPost.setDetails(
                "" + postItem.getCarType() + " " +
                        "" + postItem.getCarTypeSub() + " " +
                        "" + postItem.getCarDetailSub() + " " +
                        "" + postItem.getCarDetail());

        return view;
    }

    private View inFlaterLayoutRight(View view, ViewGroup parent,FeedPostGao postItem){
        ViewHolderItemRight holder;
        if(view != null) {
            holder = (ViewHolderItemRight) view.getTag();
        }else {
            view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_right, parent, false);
            holder = new ViewHolderItemRight(view);
            view.setTag(holder);
        }
        holder.angelCarPost.setDetails(
                "" + postItem.getCarType() + " " +
                        "" + postItem.getCarTypeSub() + " " +
                        "" + postItem.getCarDetailSub() + " " +
                        "" + postItem.getCarDetail());
        return view;
    }

    public class ViewHolderItemLeft {
        @Bind(R.id.item_post)
        AngelCarPost angelCarPost;
        public ViewHolderItemLeft(View v) {
            ButterKnife.bind(this,v);
        }
    }

    public class ViewHolderItemRight {
        @Bind(R.id.item_post)
        AngelCarPost angelCarPost;
        public ViewHolderItemRight(View v) {
            ButterKnife.bind(this,v);
        }
    }
}
