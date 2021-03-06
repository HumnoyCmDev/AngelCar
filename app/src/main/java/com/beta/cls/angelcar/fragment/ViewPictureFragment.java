package com.beta.cls.angelcar.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.PictureDao;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.parceler.Parcels;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoyDeveloper on 6/4/59. 15:31
 */
public class ViewPictureFragment extends Fragment{
    private static String ARGS_PICTURE = "ARGS_PICTURE";
    private static final String BUNDLE_STATE = "ImageViewState";

    @Bind(R.id.pictureFull) SubsamplingScaleImageView scaleImageView;
    private Target target;

    public static ViewPictureFragment newInstance(PictureDao dao) {
        Bundle args = new Bundle();
        ViewPictureFragment fragment = new ViewPictureFragment();
        args.putParcelable(ARGS_PICTURE, Parcels.wrap(dao));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_picture,container,false);
        initInstance(v,savedInstanceState);
        return v;
    }

    private void initInstance(View v, Bundle savedInstanceState) {
        ButterKnife.bind(this,v);

        ImageViewState imageViewState = null;
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_STATE)) {
            imageViewState = (ImageViewState)savedInstanceState.getSerializable(BUNDLE_STATE);
        }

        PictureDao dao = Parcels.unwrap(getArguments().getParcelable(ARGS_PICTURE));
        String src = "http://angelcar.com/"+dao.getCarImagePath().replace("chatcarimage","thumbnailcarimages");

        target = new Target(imageViewState);

        Glide.with(this).load(src)
                .asBitmap()
                .placeholder(R.drawable.loading)
                .into(target);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.clear(target);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    //    SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
//        @Override
//        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//            scaleImageView.setImage(ImageSource.cachedBitmap(resource));
//        }
//    };

    private class Target extends SimpleTarget<Bitmap>{
        ImageViewState imageViewState;

        public Target(ImageViewState imageViewState) {
            this.imageViewState = imageViewState;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            scaleImageView.setImage(ImageSource.cachedBitmap(resource),imageViewState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        View rootView = getView();
        if (rootView != null) {
            ImageViewState state = scaleImageView.getState();
            if (state != null) {
                outState.putSerializable(BUNDLE_STATE, state);
            }
        }

    }
}
