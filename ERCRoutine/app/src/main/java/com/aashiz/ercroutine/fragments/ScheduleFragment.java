package com.aashiz.ercroutine.fragments;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aashiz.ercroutine.R;
import com.aashiz.ercroutine.listadapter;
import com.aashiz.ercroutine.utilities.DataLoader;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class ScheduleFragment extends MainFragment {

    LinkedHashMap<String,String> data ;
    String department ,year ;
    int day;
    DataLoader loader;

    private boolean isAlreadyInitialized = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences set = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getBaseContext());
        department = set.getString("DEPARTMENT","BME");
        year = set.getString("YEAR","II");
        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Log.i("Today"," " + day );
        loader = new DataLoader(this.getActivity(),department,year,day);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.fragment_routine,null);
    }
     float x1,x2 ;
    Spinner spin ;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        self = this ;
        setListeners(R.id.routineList);
        spin = (Spinner)this.getActivity().findViewById(R.id.dayOfWeek);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item);
        spin.setSelection(day-1);

//        loader = new DataLoader(this.getActivity(),department,year,day);
//        loader.fetchData();
        setListAdapter();
      spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if(isAlreadyInitialized) {
//                  Toast.makeText(getContext(), "-+" + position, Toast.LENGTH_SHORT).show();
//                  fetchData(department,year,position+1);
//                  setListAdapter(new listadapter(getActivity().getBaseContext(),data));
                  loader = new DataLoader(getActivity(),department,year,position+1);
                  setListAdapter();

              }
              isAlreadyInitialized = true;
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });
    }

    public void setListAdapter() {

        try {
            loader.newFetchData();
        } catch (IOException e) {
            e.printStackTrace();
            //Error in reading data
            loader.data = null;
        } catch (JSONException e) {
            e.printStackTrace();
            loader.data = null;

        }



        ListView myView = (ListView)getActivity().findViewById(R.id.routineList);
        if(loader.data!= null) {
            if(loader.data.isEmpty()){
                ((TextView)this.getActivity().findViewById(R.id.empty)).setText("Hurray!! No Classes Today , Please Check For Updates");

            }else {
                ((TextView)this.getActivity().findViewById(R.id.empty)).setText("");

            }
            listadapter adapter = new listadapter(getActivity(),loader.data);



            myView.setAdapter(adapter);

            myView.invalidate();
        }
        else{
            ((TextView)this.getActivity().findViewById(R.id.empty)).setText("No Data Found!!, Please Check For Updates");
        }


    }

    @Override
    public void reload(){
        loader = new DataLoader(this.getActivity(),department,year,day);
        setListAdapter();
        super.reload();
    }

   boolean flag ;
    @Override
    public void onSwipeLeft() {
        super.onSwipeLeft();
        flag = false ;
        Log.d("SWIPE","Swipe Left");
        spin.setSelection(getNewDay());
        spin.callOnClick();
    }

    @Override
    public void onSwipeRight() {
        flag = true ;
        super.onSwipeRight();
        spin.setSelection(getNewDay() );
        spin.callOnClick();
        Log.d("SWIPE","Swipe Right");
    }

    int getNewDay() {
        int k ;
        if(flag) {
            k = spin.getSelectedItemPosition() - 1;
            k = k== -1 ? 6 : k ;
        }else {
            k = spin.getSelectedItemPosition() + 1 ;
            k = k ==7 ? 0: k ;
        }



        return k ;
    }
    static ScheduleFragment static_fragment ;
    public static ScheduleFragment createNewInstance(){
        if(static_fragment == null){
            Log.d("schedule_fragment","Inside null wala ma..");
            static_fragment = new ScheduleFragment();
        }
        return  static_fragment ;
    }
}