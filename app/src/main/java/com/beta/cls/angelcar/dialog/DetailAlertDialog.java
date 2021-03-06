package com.beta.cls.angelcar.dialog;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.ViewPictureActivity;
import com.beta.cls.angelcar.banner.ImageBanner;
import com.beta.cls.angelcar.dao.PictureCollectionDao;
import com.beta.cls.angelcar.dao.PostCarDao;
import com.beta.cls.angelcar.manager.Permission;
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.beta.cls.angelcar.utils.ViewFindUtils;
import com.flyco.banner.anim.select.ZoomInEnter;
import com.flyco.banner.transform.DepthTransformer;
import com.flyco.banner.widget.Banner.base.BaseBanner;

import org.parceler.Parcels;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by humnoyDeveloper on 5/4/59. 18:25
 */
public class DetailAlertDialog extends DialogFragment{
    private static String ARGS_MODEL_CAR = "ARG_MODEL_CAR";
    private static String ARGS_USER_BY = "ARG_BY_USER"; // Shop,User

//    private String user;
    private PostCarDao dao;
    private View decorView;
    public static DetailAlertDialog newInstance(PostCarDao dao,String user) {
        Bundle args = new Bundle();
        DetailAlertDialog fragment = new DetailAlertDialog();
        args.putParcelable(ARGS_MODEL_CAR, Parcels.wrap(dao));
        args.putString(ARGS_USER_BY,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        user = getArguments().getString(ARGS_USER_BY);
        dao = Parcels.unwrap(getArguments().getParcelable(ARGS_MODEL_CAR));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detail,container,false);
        initInstances(view, savedInstanceState);
        return view;
    }

    private void initInstances(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        decorView = view;
        if (savedInstanceState == null){
            loadImage();
        }

    }

    @OnClick(R.id.buttonPhone)
    public void callPhone(){
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.M) {
            Permission permission = new Permission(getActivity());
            if (permission.isAsKForPermission(Manifest.permission.CALL_PHONE,"AngelCar ต้องการขอสิทธิ์ในการจัดการโทร")){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0801689720"));
                startActivity(intent);
            }

        }else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0801689720"));
            startActivity(intent);
        }

    }

    private void loadImage() {
        /*load image*/
        Call<PictureCollectionDao> callLoadPictureAll =
                HttpManager.getInstance().getService().loadAllPicture(String.valueOf(dao.getCarId()));
        callLoadPictureAll.enqueue(loadPictureCallback);
    }

    private void pictureCarDetail(final PictureCollectionDao dao) {
        ImageBanner sib = ViewFindUtils.find(decorView, R.id.detailImage);
        sib
                .setTransformerClass(DepthTransformer.class)
                .setSelectAnimClass(ZoomInEnter.class)
                .setSource(dao.getListPicture())
                .startScroll();

        sib.setOnItemClickL(new BaseBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {

                    Intent intent = new Intent(getContext(), ViewPictureActivity.class);
                    intent.putExtra("PICTURE_DAO", Parcels.wrap(dao));
                    intent.putExtra("POSITION",position);
                    startActivity(intent);

            }
        });
    }

    @OnClick({R.id.buttonEdit,R.id.buttonClose})
    public void buttonListener(View v){
        if (v.getId() == R.id.buttonEdit){

            return;
        }
            dismiss();
    }

    /***************
 *Listener Zone*
 ***************/
    Callback<PictureCollectionDao> loadPictureCallback = new Callback<PictureCollectionDao>() {
        @Override
        public void onResponse(Call<PictureCollectionDao> call, Response<PictureCollectionDao> response) {
            if (response.isSuccessful()) {
                //Picture banner
                pictureCarDetail(response.body());
            } else {
                try {
                    Log.i("Dialog", "onResponse: " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PictureCollectionDao> call, Throwable t) {
            Log.e("Dialog", "onFailure: ", t);
        }
    };
}
