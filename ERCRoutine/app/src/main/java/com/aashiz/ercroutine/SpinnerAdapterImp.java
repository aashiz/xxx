package com.aashiz.ercroutine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Gaming on 4/30/2017.
 */

public class SpinnerAdapterImp extends BaseAdapter {
    String[] data ;
    Context parent;
    private LayoutInflater inflater;
    public  SpinnerAdapterImp(Context context, String[] st){
        data = st ;
        parent = context ;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        Log.i("Size" , "Size of data " + data.length );
        return data.length;
    }

    @Override
    public Object getItem(int position) {

        Log.i("Information" , "" + position + " " + data[position] );
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup par) {

        convertView = inflater.inflate(R.layout.spin_item,null);

        TextView text1 = (TextView) convertView.findViewById(R.id.spinText);


        text1.setText((String)this.getItem(position)) ;

        return convertView;
    }
}


