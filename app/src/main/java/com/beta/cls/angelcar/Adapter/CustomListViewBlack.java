package com.beta.cls.angelcar.Adapter;

/**
 * Created by ABaD on 12/18/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beta.cls.angelcar.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ABaD on 12/18/2015.
 */


public class CustomListViewBlack extends ArrayAdapter<String> {
    ArrayList<String> STR;
    LayoutInflater INFLATER;
    List<Integer> GRAVITY;

    public CustomListViewBlack(Context context, int textViewResourceId
            , ArrayList<String> objects, List<Integer> gv) {
        super(context, textViewResourceId, objects);
        INFLATER = (LayoutInflater)context.getSystemService
                (context.LAYOUT_INFLATER_SERVICE);
        STR = objects;
        GRAVITY = gv;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = INFLATER.inflate(R.layout.listview_simple_row
                , parent, false);
        TextView textView = (TextView) row.findViewById(R.id.txt1);
        textView.setGravity(GRAVITY.get(position));
        textView.setTextColor(Color.BLACK);
        textView.setText(STR.get(position));
        return row;
    }
}