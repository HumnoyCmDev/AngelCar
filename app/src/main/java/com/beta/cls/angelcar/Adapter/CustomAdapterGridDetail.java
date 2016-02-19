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
import com.beta.cls.angelcar.gao.CarDetailGao;

import java.util.List;




public class CustomAdapterGridDetail extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CarDetailGao> mPosts;
    private ViewHolder mViewHolder;

    private Bitmap mBitmap;
    private CarDetailGao mPost;
    private Activity mActivity;

    public CustomAdapterGridDetail(Activity activity, List<CarDetailGao> posts) {
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
            convertView = mInflater.inflate(R.layout.grid_detail_row, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.cardetail_sub = (TextView) convertView.findViewById(R.id.sub_detail);


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

        mViewHolder.cardetail_sub.setText(mPost.getCarDetailSub());


        return convertView;
    }

    private static class ViewHolder {

        TextView cardetail_sub;

    }
}
