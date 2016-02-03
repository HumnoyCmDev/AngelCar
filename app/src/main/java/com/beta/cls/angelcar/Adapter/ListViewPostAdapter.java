package com.beta.cls.angelcar.Adapter;

/**
 * Created by ABaD on 12/15/2015.
 */
import android.content.Context;
import android.text.Html;
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

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view != null) {
            holder = (ViewHolder) view.getTag();
        }else {
            view = mInflater.inflate(R.layout.list_view_item_post_left, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        //coding
//        holder.textView.setText(Html.fromHtml(
//                        ""+getItem(position).getCartype()+" "+
//                         ""+getItem(position).getCarTypeSub()+" "+
//                        ""+getItem(position).getCarDetailSub()+" "+
//                        ""+getItem(position).getCardetail()+" "
//        ));

        return view;
    }

    public class ViewHolder{
        @Bind(R.id.list_view_item_post) TextView textView;
        public ViewHolder(View v) {
            ButterKnife.bind(this,v);
        }
    }
}
