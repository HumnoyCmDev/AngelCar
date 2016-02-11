package com.hndev.library.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hndev.library.R;


/**
 * Created by humnoy on 5/2/59.
 */
@Deprecated
public class UIMessage extends FrameLayout {
    private static final String TAG = "UIChat";
    private CardView cardView;
    private TextView tvText;
//    private int colorBackground = 0;
//    private float textSize = 10;
    public UIMessage(Context context) {
        super(context);
        initInflater();
        initInstance();
    }

    public UIMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflater();
        initInstance();
        initialize(attrs,0,0);
    }

    public UIMessage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflater();
        initInstance();
        initialize(attrs,defStyleAttr,0);
    }

    @TargetApi(21)
    public UIMessage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflater();
        initInstance();
        initialize(attrs,defStyleAttr,defStyleRes);
    }

    private void initialize(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.UIMessage,defStyleAttr,defStyleRes);
        int colorBackground = Color.GRAY;
        float textSize = 14;
        String str = "Hi HN Developer";
        int tvColor = 0;

        try {
            colorBackground = a.getColor(R.styleable.UIMessage_colorCardMessage, Color.GRAY);
            textSize = a.getDimensionPixelSize(R.styleable.UIMessage_textSize,14);
            str = a.getString(R.styleable.UIMessage_text);
            tvColor = a.getColor(R.styleable.UIMessage_textColor, Color.WHITE);
        }finally {
            a.recycle();

            setCardBackgroundColor(colorBackground);

            setTextSize(convertTodp(textSize));
            setText(str);
            setTextColor(tvColor);
        }


    }

    private void initInflater() {
        inflate(getContext(), R.layout.custom_view_ui_message, this);
    }

    private void initInstance() {
        cardView = (CardView) findViewById(R.id.custom_view_ui_chat_card);
        tvText = (TextView) findViewById(R.id.custom_view_ui_chat_text);

    }

    public void setCardBackgroundColor(int color){
        cardView.setCardBackgroundColor(color);
    }
    public void setTextSize(float size){
        tvText.setTextSize(size);
    }

    public void setText(String s){
        tvText.setText(s);
    }
    public void setTextColor(int color){
        tvText.setTextColor(color);
    }

    private float convertTodp(float size){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = size / (metrics.densityDpi / 160f);
        return dp;
    }


    /// save state view ด้วยนะ
}
