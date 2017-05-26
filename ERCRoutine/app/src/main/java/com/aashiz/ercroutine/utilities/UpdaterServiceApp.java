package com.aashiz.ercroutine.utilities;

import com.aashiz.ercroutine.MainActivity;
import com.aashiz.ercroutine.R;

/**
 * Created by Gaming on 4/28/2017.
 */

public class UpdaterServiceApp extends UpdaterService {
    boolean isUpdateAvailable = false ;
    int localVersion ;
    public UpdaterServiceApp(MainActivity k){
        super();
        parent = k;
        isUpdateApp = true ;
        checkLocalVersion();
        checkForInternet();
    }

    private void checkLocalVersion(){
       localVersion = Integer.parseInt( parent.getResources().getString(R.string.app_version));
    }

    @Override
    public void start(){
        if(!isInternet){
            result = "No Internet Available. Please Try again Later.";
            return;
        }
        int k = checkInternetVersion();
        if(k==-1){
            return;
        }
        if(k > localVersion){
            //Do the work
            result = "Update Available. Click Ok to download it. Update version=" + k;
            isUpdateAvailable = true ;
            return;
        }else {
            result = "Already Updated to latest Version";
        }

    }

    private int checkInternetVersion() {
        String k = fetchURL(ConstantURL+"/app_version.json");
        int i;
        try{
            i = Integer.parseInt(k);
        }catch(Exception ex){
            i = -1;
            result = k ;
        }
        return i ;
    }
}
