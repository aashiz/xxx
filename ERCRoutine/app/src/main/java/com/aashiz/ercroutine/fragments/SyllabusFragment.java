package com.aashiz.ercroutine.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aashiz.ercroutine.MainActivity;
import com.aashiz.ercroutine.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link SyllabusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyllabusFragment extends MainFragment {
    static SyllabusFragment self ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DEPARTMENT = "BME";
    private static final String YEAR = "II";
    private WebView webView ;
    // TODO: Rename and change types of parameters
    private String Department;
    private String Year;



    public SyllabusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param department Parameter 1.
     * @param year Parameter 2.
     * @return A new instance of fragment syllabus.
     */
    // TODO: Rename and change types and number of parameters
    public static SyllabusFragment newInstance(String department, String year) {

        self = new SyllabusFragment();
        Bundle args = new Bundle();
        args.putString(DEPARTMENT, department);
        args.putString(YEAR, year);
        self.setArguments(args);
        return self;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Department = getArguments().getString(DEPARTMENT);
            Year = getArguments().getString(YEAR);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_syllabus, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity p = (MainActivity)getActivity() ;
        ((TextView)(p.findViewById(R.id.txtHeader))).setText("Loading...");
        final k s = new k(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d("DEBUG","In a new Thread");

                webView =(WebView) p.findViewById(R.id.syllabus);
                webView.setWebViewClient(s);
                Log.d("SCRTOOP",p.toString());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("file:///android_asset/syllabus/index.html#"+Department);
            }
        });




      }


    class k extends WebViewClient{
        SyllabusFragment s;
        public k(SyllabusFragment f){
            s=f;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("PAGEFINISHED","PAGE finished loading");
            try {
                TextView xx = (TextView) s.getActivity().findViewById(R.id.txtHeader);
                Log.d("PAGEFINISHED", url);
                url = url.substring(url.indexOf("#") + 1, url.length());
                Log.d("PAGEFINISHED", url);
                if (!url.contains("page")) {
                    xx.setText(url);
                }
            }catch(NullPointerException nx){

            }



        }
    }

    @Override
    public void handleBackKey() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.handleBackKey();
        }
    }
}
