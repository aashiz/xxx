package com.aashiz.ercroutine.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aashiz.ercroutine.R;

import org.w3c.dom.Text;

/**
 * Created by Gaming on 4/25/2017.
 */

public class PopUpFactory  extends DialogFragment{


    String UpdateText = "Updating schedules..";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null){
            UpdateText = savedInstanceState.getString("TEXT");
        }
        View view = inflater.inflate(R.layout.dummypop,container);
        getDialog().setTitle("Working");
        getDialog().setCanceledOnTouchOutside(false);
        //((TextView)this.getActivity().findViewById(R.id.update)).setText(UpdateText);
        return view ;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public static PopUpFactory createNew(String s) {
        PopUpFactory k = new PopUpFactory();
        Bundle bu = new Bundle();
        bu.putCharSequence("TEXT",s);
        k.setArguments(bu);
        return k ;
    }
}
