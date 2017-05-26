package com.aashiz.ercroutine.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Gaming on 2/25/2017.
 */

public class DataLoader {
    String DEPARTMENT,YEAR;
    int DAY;
    Activity CONTEXT ;
    String JSON = null ;
    public LinkedHashMap<String,String> data;
    private String type = "C";
    String fileName = "" ;

    public DataLoader(Activity c,String department,String year,String typ){
        CONTEXT = c;
        DEPARTMENT = department;
        YEAR = year ;
        this.type = typ;
        data = new LinkedHashMap<>();
        fileName = CONTEXT.getFilesDir().getAbsolutePath();
    }


    public DataLoader(String fileDirName, String department, String year, int i) {
        fileName = fileDirName ;
        DEPARTMENT = department ;
        YEAR = year ;
        DAY = i ;
    }

    public JSONObject getJson() {
        JSONObject obj ;
        try {
            readJSON();
             obj = new JSONObject(JSON);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }

        return obj ;
    }

    public String getFirstTime() {


        Iterator<String> arr ;
        try {
             arr = arrangeSerially(getJson().getJSONObject(String.valueOf(DAY)).names());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return arr.next();
    }

    public DataLoader(Activity context , String department, String year, int day){
        DEPARTMENT = department;
        YEAR = year ;
        DAY= day ;
        CONTEXT = context ;
        data = new LinkedHashMap<>();
        fileName = CONTEXT.getFilesDir().getAbsolutePath();
    }

    public DataLoader(Activity t,String d,String y){ ///Constructer for loading exam schedule
        DEPARTMENT = d;
        YEAR = y ;
        CONTEXT = t;
        type =  "E" ;
        data = new LinkedHashMap<>();
        fileName = CONTEXT.getFilesDir().getAbsolutePath();
    }



    private void readJSON() throws IOException {
        if(this.JSON != null){
            return;
        }
        String filename ;
        if(type=="C") {
                filename = fileName + "/" + "class-schedule/" + DEPARTMENT + "-" + YEAR + ".json";
        Log.i("FileName", filename);
        }else if(type=="E"){
                filename =  fileName + "/" + "exam-routine/" +DEPARTMENT+"-" +YEAR +"_examroutine.json" ;
        }else if(type=="N") {
            filename = fileName+ "/notes/" + DEPARTMENT + "-" +YEAR+".json";
        }else {
            throw new IOException("No Type Specified");
        }

        String myJson = "" ;
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line ;
        while((line = reader.readLine())!= null){
            myJson += line ;
        }
        this.JSON = myJson;
    }

    public void newFetchData() throws IOException, JSONException {
        readJSON();
        String myJson = this.JSON ;
        //Now parsing json...
        Log.d("FILE",myJson);
        JSONObject obj = new JSONObject(myJson);
        if( !obj.isNull(String.valueOf(DAY))){
            JSONObject newObj = obj.getJSONObject(String.valueOf(DAY));
            Log.d("DEBUG",newObj.toString());

            Iterator<String> it =arrangeSerially(newObj.names());

            data.clear();
            while(it.hasNext()){
                String key = it.next();
                Log.d("DEBUG","key=" + key + "hash="+key.hashCode());
                data.put(key,newObj.getString(key));
            }
        }else {
            data.clear();
        }
    }

    private Iterator<String> arrangeSerially(JSONArray it) {
        int l = it.length();
        ArrayList<String> sort = new ArrayList<String>(l);
        for(int a=0;a<l;a++){
            sort.add(a,it.optString(a));
        }
        if(Build.VERSION.SDK_INT  > Build.VERSION_CODES.KITKAT ){
//            Log.d("DEBUG","Greater than kitkat");
            return sort.iterator();
        }

        for(int i=0;i<l;i++){

            for(int j=i+1;j<l;j++){
                String curr = sort.get(i) ;
                int k1 = Integer.parseInt(curr.split(":")[0]);
                String curr2 =sort.get(j);
                int k2 =  Integer.parseInt(curr2.split(":")[0]);
                k1 = k1<10 ? k1+12 :k1 ;
                k2 = k2<10 ? k2+12 :k2 ;
                if(k1>k2){
                    sort.remove(i);
                    sort.add(i,curr2);
                    sort.remove(j);
                    sort.add(j,curr);

                }
            }
        }

        return sort.iterator();
    }

    public void fetchDataExam() throws JSONException, IOException {
        readJSON();
        String myJson = JSON;
        //Now parsing json...

        JSONObject obj = new JSONObject(myJson);

            Iterator<String> it = obj.keys();
            data.clear();
            while(it.hasNext()){
                String key = it.next();
                if(!key.equals("version")) {
                    data.put(key, obj.getString(key));
                }
            }

    }




    //Function to get Version of the current file if exception occurs return -1
    public int getVersion(){

            int x = -1;
        try {
            readJSON();
            x = new JSONObject(this.JSON).getInt("version");
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return x;

    }


    public void writeToFile(String s) throws IOException {

        String filename ;
        if(type=="C") {
            filename = fileName+ "/class-schedule/" + DEPARTMENT + "-" + YEAR + ".json";
            Log.i("FileName", filename);
        }else if(type=="E"){
            filename =  fileName + "/exam-routine/" +DEPARTMENT+"-" +YEAR +"_examroutine.json" ;
        }else if(type=="N"){
            filename =  fileName + "/notes/" +DEPARTMENT+"-" +YEAR +".json" ;
        }else {
            throw new IOException("No Type Specified");
        }
        FileOutputStream out = new FileOutputStream(filename);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        writer.write(s);
        writer.flush();
    }

    public JSONObject getNote() {
        try {
            readJSON();
            JSONObject obj = new JSONObject(JSON);
            return obj ;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null ;

    }

    public JSONObject getSortedJSON() {
//        Iterator<String> arr ;
        JSONObject obj ;
        try {

            obj = getJson().getJSONObject(String.valueOf(DAY));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj ;
    }

    public Iterator<String> getSortedName() {


        Iterator<String> arr ;
        try {
            arr = arrangeSerially(getJson().getJSONObject(String.valueOf(DAY)).names());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return arr;
    }
}
