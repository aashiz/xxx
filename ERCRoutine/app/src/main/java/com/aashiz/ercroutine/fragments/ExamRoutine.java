package com.aashiz.ercroutine.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.aashiz.ercroutine.utilities.DataLoader;
import com.aashiz.ercroutine.R;
import com.aashiz.ercroutine.listadapter;

import org.json.JSONException;

import java.io.IOException;

public class ExamRoutine extends MainFragment {

    String department ,year ;
    DataLoader loader ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences set = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getBaseContext());
        department = set.getString("DEPARTMENT","BME");
        year = set.getString("YEAR","II");
        loader = new DataLoader(this.getActivity(),department,year);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter();



    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_examroutine,null);

    }



    private void setListAdapter() {

        try {
            loader.fetchDataExam();
//            Toast.makeText(this.getContext(),loader.getVersion()+"-version",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            loader.data = null;
        } catch (JSONException e) {
            e.printStackTrace();
            loader.data = null;

        }



        ListView myView = (ListView)getActivity().findViewById(R.id.examScheduleList);
        if(loader.data!= null) {
            if(loader.data.isEmpty()){
                ((TextView)this.getActivity().findViewById(R.id.eempty)).setText("Hurray!! No Classes Today , Please Check For Updates");

            }else {
                ((TextView)this.getActivity().findViewById(R.id.eempty)).setText("");

            }
            listadapter adapter = new listadapter(getActivity(),loader.data);



            myView.setAdapter(adapter);
            myView.invalidate();
        }
        else{
            ((TextView)this.getActivity().findViewById(R.id.eempty)).setText("No Data Found!!, Please Check For Updates");
        }


    }

    @Override
    public void reload(){
        loader = new DataLoader(this.getActivity(),department,year);
        setListAdapter();
        super.reload();
    }
}