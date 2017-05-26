package com.aashiz.ercroutine.utilities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.aashiz.ercroutine.MainActivity;
import com.aashiz.ercroutine.R;

/**
 * Created by Gaming on 2/22/2017.
 */

//This is a class which represents asynctask which does updating task.. the parameter is updatervice class

public class DataReader extends AsyncTask<UpdaterService,String,String> {
    private MainActivity parent ;
    boolean isUpdateApp = false ;
    public DataReader(MainActivity a){
        parent = a;
    }

    @Override
    protected String doInBackground(UpdaterService... params) {
        UpdaterService current = params[0] ;


        current.start();
        return current.getResult() ;
    }

    @Override
    protected void onPostExecute(String s) {

        final String t = s ;
        parent.stopProgressBar();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle("Result");
        builder.setMessage(s);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    if(t.contains("download")){
                        try {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(parent.getString(R.string.officialSite)));
                            parent.startActivity(myIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(parent, "No application can handle this request."
                                    + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
        parent.upDateFragment();
        super.onPostExecute(s);

    }


}



