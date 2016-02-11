package com.hndev.library.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.hndev.library.R;
import com.hndev.library.view.sate.BundleSavedState;
import com.squareup.picasso.Picasso;

/**
 * Created by humnoy on 6/2/59.
 */
public class AngelCarPost extends BaseCustomViewGroup{

    private RelativeLayout background;

    private CircularImageView ic_Profile;
    private CircularImageView ic_Product;

    private TextView title;
    private TextView details;

    private String strTitle = "";
    private String strDetails = "";

    private int colorTextTiele = 0;
    private int colorTextDetail = 0;
    private int position = 0;
    float radius = 0;
    int colorBackground = 0;

    public AngelCarPost(Context context) {
        super(context);
        initInflater();
        initInstance();
    }

    public AngelCarPost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs,0,0);
        initInflater();
        initInstance();
    }

    public AngelCarPost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs,defStyleAttr,0);
        initInflater();
        initInstance();
    }

    @TargetApi(21)
    public AngelCarPost(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(attrs,defStyleAttr,defStyleRes);
        initInflater();
        initInstance();
    }

    private void initInflater() {
        if (position == 0) {
            inflate(getContext(), R.layout.custom_view_ui_post_left, this);
        }else {
            inflate(getContext(), R.layout.custom_view_ui_post_right, this);
        }
    }

    private void initInstance() {
        ic_Profile = (CircularImageView) findViewById(R.id.custom_view_ui_post_icon_profile);
        ic_Product = (CircularImageView) findViewById(R.id.custom_view_ui_post_image_product);
        title = (TextView) findViewById(R.id.custom_view_ui_post_title);
        details = (TextView) findViewById(R.id.custom_view_ui_post_details);
        background = (RelativeLayout) findViewById(R.id.custom_view_ui_post_background);

        //inti

//        title.setMaxLines(1);
//        title.setEllipsize(TextUtils.TruncateAt.END);

        setTitle(strTitle);
        setDetails(strDetails);
        setTextColorTitle(colorTextTiele);
        setTextColorDetail(colorTextDetail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            background.setBackground(createBackground(colorBackground,radius));
        }else {
            background.setBackgroundDrawable(createBackground(colorBackground,radius));
        }

    }
    private void initialize(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.UIPost,defStyleAttr,defStyleRes);


        try {
            colorBackground = a.getColor(R.styleable.UIPost_colorBackground, Color.rgb(248,204,20));
            position = a.getInt(R.styleable.UIPost_position,0);
            colorTextTiele = a.getColor(R.styleable.UIPost_colorTitle,Color.BLACK);
            colorTextDetail = a.getColor(R.styleable.UIPost_colorDetail,Color.BLACK);
            strTitle = a.getString(R.styleable.UIPost_textTitle);
            strDetails = a.getString(R.styleable.UIPost_textDetails);

            radius = a.getDimensionPixelSize(R.styleable.UIPost_radius,0);

        }finally {
            a.recycle();
        }
    }

    public void setIc_Profile(String urlImage){
        Picasso.with(getContext())
                .load(urlImage)
                .into(ic_Profile);
    }
    public void setIc_Profile(int resourceId){
        Picasso.with(getContext())
                .load(resourceId)
                .into(ic_Profile);
    }

    public void setIc_Product(String urlImage){
        Picasso.with(getContext())
                .load(urlImage)
                .into(ic_Product);
    }

    public void setIc_Product(int resourceId){
        Picasso.with(getContext())
                .load(resourceId)
                .into(ic_Product);
    }

    public void setTextColorTitle(int color){
        title.setTextColor(color);
    }

    public void setTextColorDetail(int color){
        details.setTextColor(color);
    }

    public void setTitle(String title){
        if(title != null)
        this.title.setText(title);
    }

    public void setDetails(String details){
        if(title != null)
        this.details.setText(details);
    }


    private float convertTodp(float size){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = size / (metrics.densityDpi / 160f);
        return dp;
    }

    private GradientDrawable createBackground(int color,float radius){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

}
