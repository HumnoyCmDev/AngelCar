package com.hndev.library.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.hndev.library.R;
import com.hndev.library.view.sate.BundleSavedState;
import com.squareup.picasso.Picasso;

import javax.xml.parsers.DocumentBuilder;

public class AngelCarMessage extends BaseCustomViewGroup {

    private CircularImageView iconProfile;
    private RoundedImageView image;
    private TextView message;
    private LinearLayout background;

    private int colorBackground;
    private int colorMessage;
    private float radius = 35;
    private String msg;

    public enum Position{
       left,right
    }

    private Position position = Position.left;

    public AngelCarMessage(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public AngelCarMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithAttrs(attrs, 0, 0);
        initInflate();
        initInstances();
    }

    public AngelCarMessage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWithAttrs(attrs, defStyleAttr, 0);
        initInflate();
        initInstances();
    }

    @TargetApi(21)
    public AngelCarMessage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
    }

    private void initInflate() {
        switch (position){
            case left:inflate(getContext(), R.layout.custom_view_ui_angelcar_message_left, this);
                break;
            case right:inflate(getContext(), R.layout.custom_view_ui_angelcar_message_ring, this);
                break;
        }

    }

    private void initInstances() {
        // findViewById here
        iconProfile = (CircularImageView) findViewById(R.id.custom_view_iu_angelcar_icon_profile);
        message = (TextView) findViewById(R.id.custom_view_iu_angelcar_message);
        background = (LinearLayout) findViewById(R.id.custom_view_iu_angelcar_background);
        image = (RoundedImageView) findViewById(R.id.custom_view_iu_angelcar_image);

        message.setText(msg);
        message.setTextColor(colorMessage);
        setBackground(colorBackground,radius);

    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AngelCarMessage,
                defStyleAttr, defStyleRes);

        try {
            position = a.getInt(R.styleable.AngelCarMessage_cls_position,0) == 0 ? Position.left : Position.right;
            colorBackground = a.getColor(
                    R.styleable.AngelCarMessage_cls_colorBackground,
                    Color.parseColor("#ffa600"));
            colorMessage = a.getColor(
                    R.styleable.AngelCarMessage_cls_colorMessage
                    ,Color.WHITE);
            radius = a.getDimensionPixelSize(R.styleable.AngelCarMessage_cls_radius,35);
            msg = a.getString(R.styleable.AngelCarMessage_cls_message);
        } finally {
            a.recycle();
        }
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

    public void setMessage(String msg) {

        if (!msg.contains("<img>") && !msg.contains("</img>")) {
            this.msg = msg;
            background.setVisibility(VISIBLE);
            image.setVisibility(GONE);
            this.message.setText(msg);
        }else{
            this.msg = msg;
            background.setVisibility(GONE);
            image.setVisibility(VISIBLE);
            String strStart = "<img>";
            String strEnd = "</img>";
//        String rulImage = url.substring(strStart.length(),url.length()-strEnd.length());
            String rulImage = msg.substring(strStart.length(),msg.lastIndexOf("</img>"));
            Picasso.with(getContext())
                    .load(rulImage)
                    .error(R.drawable.simple_product)
                    .into(image);
        }
    }


    public void setIconProfile(String url) {
        Picasso.with(getContext())
                .load(url)
                .error(R.drawable.ic_hndeveloper)
                .into(iconProfile);
    }

    public void setBackground(int color,float radius){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            background.setBackground(gradientDrawable);
        }else {
            background.setBackgroundDrawable(gradientDrawable);
        }
    }

    public void setBackground(int color){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            background.setBackground(gradientDrawable);
        }else {
            background.setBackgroundDrawable(gradientDrawable);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * 2 / 3;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
        setMeasuredDimension(width,height);*/


        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int newWidth = (width / 8) * 7;
        int newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth,MeasureSpec.EXACTLY);
        // หลอกขนาดของลูก
        super.onMeasure(newWidthMeasureSpec,heightMeasureSpec);
        // หลอกขนาดของแม่
//        setMeasuredDimension(width,heightMeasureSpec);
    }


}
