package com.beta.cls.angelcar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beta.cls.angelcar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 5/2/59.
 */
public class ShowCaseFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    @Bind(R.id.imageShowCase) ImageView imgShowCase;
    @Bind(R.id.showText) TextView showText;

    public ShowCaseFragment() {
        super();
    }

    public static ShowCaseFragment newInstance(int page) {
        ShowCaseFragment fragment = new ShowCaseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_case, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }

        Bundle arg = getArguments();
        if (arg != null){
            int i = arg.getInt(ARG_PAGE);
            showText.setText("Help Pager : "+i);
        }
    }
}
