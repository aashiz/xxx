package com.aashiz.ercroutine;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;

/**
 * Created by Gaming on 2/21/2017.
 */

public class listadapter extends BaseAdapter {

   private LinkedHashMap<String,String> data ;
    private Context parent ;
    LayoutInflater inflater ;

    public  listadapter(Context context, LinkedHashMap<String,String> st){
        data = st ;
        parent = context ;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        Log.i("Size" , "Size of data " + data.size() );
        return data.size();
    }

    @Override
    public Object getItem(int position) {

        Log.i("Information" , "" + position + " " + data.keySet().toArray()[position] );
        return data.keySet().toArray()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup par) {

        convertView = inflater.inflate(R.layout.list_item,null);

        TextView text1 = (TextView) convertView.findViewById(R.id.textTime);
        TextView text2 = (TextView) convertView.findViewById(R.id.textPeriod);

        text1.setText((String)this.getItem(position)) ;
        text2.setText(data.get(getItem(position)));
        return convertView;
    }
}
