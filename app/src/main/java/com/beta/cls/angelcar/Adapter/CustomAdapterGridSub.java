package com.beta.cls.angelcar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.manager.CarDataType;

import java.util.List;

public class CustomAdapterGridSub extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CarDataType> mPosts;
    private ViewHolder mViewHolder;

    private Bitmap mBitmap;
    private CarDataType mPost;
    private Activity mActivity;

    public CustomAdapterGridSub(Activity activity, List<CarDataType> posts) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mPosts = posts;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return mPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_row, parent, false);
            mViewHolder = new ViewHolder();
             mViewHolder.cartype_sub = (TextView) convertView.findViewById(R.id.name_cartype);


            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        mPost = mPosts.get(position);

        /*if (mPost.getThumbnail() != null) {



            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        URL url = new URL(mPost.getThumbnail());
                        mBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    } catch (MalformedURLException e) {

                    } catch (IOException e) {

                    }
                    return null;
                }
            }.execute();


            mViewHolder.thumbnail.setImageBitmap(mBitmap);
        }*/

        // ถ้าใช้ Picasso ก็ uncomment ข้างล้างนี้ แล้วลบ AsyncTask ออก
        // Picasso.with(mActivity)
        //    .load(mPost.getThumbnail())
        //    .into(mViewHolder.thumbnail);

        mViewHolder.cartype_sub.setText(mPost.getCartypeSub());


        return convertView;
    }

    private static class ViewHolder {

        TextView cartype_sub;

    }
}