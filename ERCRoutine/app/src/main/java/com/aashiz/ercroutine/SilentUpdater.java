package com.aashiz.ercroutine;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.aashiz.ercroutine.utilities.UpdaterService;

public class SilentUpdater extends AsyncTask<UpdaterService,Long,String>
{
    public SilentUpdater() {}

    String fileDir ;
    MainActivity k ;
    boolean flag = false;
    public SilentUpdater(MainActivity s) {
        k = s ;
        flag = true ;
    }

    public SilentUpdater(String fileDirName) {
        fileDir = fileDirName;
    }

    @Override
    protected String doInBackground(UpdaterService... params) {
        String result = "" ;
        for(UpdaterService t :params){
            t.start();
            result += t.getResult();
        }
        return result ;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("DEBUG",s);
        if(s.contains("Updated") && flag) {
            k.upDateFragment();
        }
        super.onPostExecute(s);
    }
}
