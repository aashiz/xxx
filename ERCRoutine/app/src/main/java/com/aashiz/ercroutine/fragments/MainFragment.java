package com.aashiz.ercroutine.fragments;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aashiz.ercroutine.R;
import com.aashiz.ercroutine.utilities.OnSwipeTouchListener;

/**
 * Created by Gaming on 4/26/2017.
 */

public class MainFragment extends Fragment {

    MainFragment self;
    public void reload() {

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.detach(this).attach(this).commit();
        Log.d("Fragment Recreate","Fragment Recreated!!" + this.getTag());
    }


    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }



        protected void setListeners(int id) {

            OnSwipeTouchListener ost = new OnSwipeTouchListener(getActivity()) {
                @Override
                public void onSwipeRight() {
                    if (self == null) {
                        super.onSwipeRight();
                        return;
                    }
                    self.onSwipeRight();
//                this.onSwipeRight();
                }

                @Override
                public void onSwipeLeft() {
                    if (self == null) {
                        super.onSwipeLeft();
                        return;
                    }
                    self.onSwipeLeft();

                }
            } ;
            getActivity().findViewById(id).setOnTouchListener(ost);
            getActivity().findViewById(R.id.touchListener).setOnTouchListener(ost);


        }


        public void handleBackKey() {
            Toast.makeText(getContext(), "Please Press Back Twice to exit the App", Toast.LENGTH_SHORT).show();

        }

    }

