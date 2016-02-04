package com.beta.cls.angelcar.Adapter;

/**
 * Created by ABaD on 12/15/2015.
 */
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.manager.FeedPostItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListViewPostAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<FeedPostItem> postItem;

    private static final String TAG = "ListViewPostAdapter";

    public ListViewPostAdapter(Context context,List<FeedPostItem> postItem) {
        mInflater = LayoutInflater.from(context);
        this.postItem = postItem;
    }


    public int getCount() {
        return postItem.size();
    }

    public FeedPostItem getItem(int position) {
        return postItem.get(position);
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
        FeedPostItem postItem = getItem(position);
        int switching_position = getItemViewType(position);
        switch (switching_position){
            case 0: view = inFlaterLayoutRight(view,parent,postItem);
                break;
            case 1: view = inFlaterLayoutLeft(view,parent,postItem);
                break;
        }
        return view;//inFlaterLayoutLeft(view,parent,postItem);
    }

    private View inFlaterLayoutLeft(View view, ViewGroup parent,FeedPostItem postItem){
        ViewHolderItemLeft holder;
        if(view != null) {
            holder = (ViewHolderItemLeft) view.getTag();
        }else {
            view = mInflater.inflate(R.layout.list_view_item_post_left, parent, false);
            holder = new ViewHolderItemLeft(view);
            view.setTag(holder);
        }
        //coding
        holder.textView.setText(Html.fromHtml(
                "" + postItem.getCartype() + " " +
                        "" + postItem.getCarTypeSub() + " " +
                        "" + postItem.getCarDetailSub() + " " +
                        "" + postItem.getCardetail() + " "
        ));
        return view;
    }

    private View inFlaterLayoutRight(View view, ViewGroup parent,FeedPostItem postItem){
        ViewHolderItemRight holder;
        if(view != null) {
            holder = (ViewHolderItemRight) view.getTag();
        }else {
            view = mInflater.inflate(R.layout.list_view_item_post_right, parent, false);
            holder = new ViewHolderItemRight(view);
            view.setTag(holder);
        }
        //coding
        holder.textView.setText(Html.fromHtml(
                "" + postItem.getCartype() + " " +
                        "" + postItem.getCarTypeSub() + " " +
                        "" + postItem.getCarDetailSub() + " " +
                        "" + postItem.getCardetail() + " "
        ));
        return view;
    }

    public class ViewHolderItemLeft {
        @Bind(R.id.list_view_item_post) TextView textView;
        public ViewHolderItemLeft(View v) {
            ButterKnife.bind(this,v);
        }
    }

    public class ViewHolderItemRight {
        @Bind(R.id.list_view_item_post) TextView textView;
        public ViewHolderItemRight(View v) {
            ButterKnife.bind(this,v);
        }
    }
}
