package com.aashiz.ercroutine.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.aashiz.ercroutine.R;
import com.aashiz.ercroutine.utilities.DataLoader;
import com.aashiz.ercroutine.utilities.NotesListAdapter;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by Gaming on 5/3/2017.
 */

public class FragmentNote extends MainFragment {

    private DataLoader loader;
    private String department ;
    private String year ;
    private ListView list ;
    private TextView tx ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences set = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getBaseContext());
        department = set.getString("DEPARTMENT","BME");
        year = set.getString("YEAR","II");
        loader = new DataLoader(this.getActivity(),department,year,"N");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tx = (TextView)getActivity().findViewById(R.id.noteHeader);
        list = (ListView)getActivity().findViewById(R.id.listNote);
        setListAdapter();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note,null);
    }

    @Override
    public void reload() {
        loader = new DataLoader(this.getActivity(),department,year,"N");
        setListAdapter();
        super.reload();
    }

    private void setListAdapter() {

        JSONObject obj = loader.getNote() ;
        if(obj != null){
            NotesListAdapter adp = new NotesListAdapter(getActivity(),obj);
            list.setAdapter(adp);
            list.setOnItemClickListener(adp);
            tx.setText("Materials for "+ department + " " + year + " year");

        }else {
            list.setVisibility(View.INVISIBLE);
            tx.setText("No Data Available, Update");
        }
    }
}
