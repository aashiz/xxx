package com.aashiz.ercroutine.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashiz.ercroutine.R;
import com.aashiz.ercroutine.fragments.MainFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComingSoon.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComingSoon#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComingSoon extends MainFragment {
    // TODO: Rename parameter arguments, choose names that match


    public ComingSoon() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComingSoon.
     */

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coming_soon, container, false);
    }




}
