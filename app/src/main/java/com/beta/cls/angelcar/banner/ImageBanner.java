package com.beta.cls.angelcar.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.PictureDao;
import com.beta.cls.angelcar.utils.ViewFindUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.squareup.picasso.Picasso;

public class ImageBanner extends BaseIndicatorBanner<PictureDao, ImageBanner> {
    private ColorDrawable colorDrawable;

    public ImageBanner(Context context) {
        this(context, null, 0);
    }

    public ImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        colorDrawable = new ColorDrawable(Color.parseColor("#555555"));
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
//        final PictureDao item = mDatas.get(position)
//        tv.setText(item.getTopic());
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.adapter_simple_image, null);
        ImageView iv = ViewFindUtils.find(inflate, R.id.iv);

        final PictureDao item = mDatas.get(position);
        int itemWidth = mDisplayMetrics.widthPixels;
        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

        String urlImage ="http://angelcar.com/"+item.getCarImagePath()
                .replace("chatcarimage","thumbnailcarimages");

        if (!TextUtils.isEmpty(urlImage)) {
            Glide.with(mContext)
                    .load(urlImage)
                    .override(itemWidth, itemHeight)
                    .centerCrop()
                    .placeholder(colorDrawable)
                    .into(iv);
        } else {
            iv.setImageDrawable(colorDrawable);
        }

        return inflate;
    }


}
