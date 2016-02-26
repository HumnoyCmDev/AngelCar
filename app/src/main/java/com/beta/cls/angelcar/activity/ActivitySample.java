package com.beta.cls.angelcar.activity;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.fragment.SampleDialogAllPost;
import com.beta.cls.angelcar.fragment.YearFragmentDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by humnoy on 20/2/59.
 */
public class ActivitySample extends AppCompatActivity {

    private static final String TAG = "ActivitySample";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_fragment_all_post);
        ButterKnife.bind(this);

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("YearFragmentDialog");
//        if (fragment != null){
//            ft.remove(fragment);
//        }
//        ft.addToBackStack(null);
//
//        YearFragmentDialog dialog = new YearFragmentDialog();
//        dialog.setTargetFragment(getFragmentMa,0);
//        dialog.show(getSupportFragmentManager(),"YearFragmentDialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && data != null){
            Toast.makeText(ActivitySample.this,data.getStringExtra("num"),Toast.LENGTH_SHORT).show();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
