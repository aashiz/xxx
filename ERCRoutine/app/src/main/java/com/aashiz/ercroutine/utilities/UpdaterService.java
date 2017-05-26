package com.aashiz.ercroutine.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.aashiz.ercroutine.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Gaming on 4/25/2017.
 */

public class UpdaterService {

    protected String ConstantURL = "https://erc-routine-project.firebaseio.com";
    protected String result = "" ;
    String department,year,type ;
    protected MainActivity parent;
    boolean isInternet;
    protected boolean isUpdateApp = false ;
    public UpdaterService(MainActivity s,String d, String y, String t){
        department =d;
        year=y;
        type=t ;
        parent = s ;
        checkForInternet() ;
    }
    public UpdaterService(){

    }

    protected void checkForInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) parent.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        isInternet = (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }

    public void start() {
        if(!isInternet){
            result= "No Internet Connection Available Right Now";
            return;
        }
        switch(type){
            case "C":
                doTheWork("class-schedules") ;
                break;
            case "E":
                doTheWork("exam-schedules");
                break;
            case "N":
                doTheWork("notes");
                break;
            case "ALL":
                massUpdater() ;
        }
    }

    private void massUpdater() {
        result = "Mass Updated..";
    }


    private void doTheWork(String s) {
        //Check Version
            String url =  ConstantURL + "/"+s+"/" + department + "/" +year + "/version.json";
            String out = fetchURL(url);
            Log.d("INTERNETDATA",out + " " + url);
            int x = -1;
            try {
                x = Integer.parseInt(out);
            }catch(NumberFormatException e){

            }
            if(x != -1){
                int curr =  new DataLoader(parent,department,year,type).getVersion();
                if(curr < x){
                    //Fetch the data
                    url = ConstantURL +  "/"+s+"/"  + department + "/" +year + ".json";
                    out = fetchURL(url);
                    if(out.contains("ERROR")){
                        result = "Error in Updating: " + out ;
                        return;
                    }else {
                        DataLoader loader = new DataLoader(parent,department,year,type);
                        try {
                            loader.writeToFile(out);
                        } catch (IOException e) {
                            e.printStackTrace();
                            result = "Error in Writing to file:" + e.getMessage();
                            return ;

                        }
                        result = "Successfully Updated!!";

                        return;
                    }
                }else {
                    result = "Already in Updated State!" ;
                    return ;
                }
            }
        result = "No data Available right now :(  " + out.substring(0,out.length() > 10 ? 10:out.length());

    }

    protected String fetchURL(String url) {
        InputStream in = null;
        String xy ;
        in = openHttpConnection(url);
        if(in == null){
            return "URLCONNECTIONERROR";
        }
        try {
            int x ;
//                        Toast.makeText(CONTEXT,"x="+ x,Toast.LENGTH_SHORT).show();
            xy="" ;
            while((x=in.read())!=-1){
//                    Log.i("DATA","File:- "+x);
                xy+= (char)x;
            }
            Log.i("Internet;","KK"+xy.length());

        } catch (IOException e) {
            e.printStackTrace();
            xy = "READWRITEERROR";
//                        Toast.makeText(CONTEXT,"-failed",Toast.LENGTH_SHORT).show();
        }
        return xy ;
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }

            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(60000);

            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public boolean isUpdateApp() {
        return isUpdateApp;
    }

    public String getResult() {
        return result ;
    }
}
