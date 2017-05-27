package com.aashiz.ercroutine.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aashiz.ercroutine.MainActivity;
import com.aashiz.ercroutine.RemainderService;

import java.util.Calendar;

/**
 * Created by Gaming on 5/18/2017.
 */

public class AlarmReceiver  extends BroadcastReceiver{
    NotificationManager mManager ;
    Intent intent ;
    Context c ;


    int mId = 100;
    private void showNotification(String message) {
        if(message == null){
            return ;
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c);
        mBuilder.setSmallIcon(android.R.drawable.ic_dialog_info);
        mBuilder.setContentTitle("ERC Routine");
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setContentText(message);


        Intent notificationIntent = new Intent(parent, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(parent, 0,
                notificationIntent, 0);

        long pattern[] = {0,100};
        mBuilder.setVibrate(pattern);
        mBuilder.setContentIntent(intent );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mManager.notify(mId,mBuilder.build());

        }else {
            mManager.notify(mId,mBuilder.getNotification());
        }
    }


    String TAG = "Network-ERC-Routine";
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            RemainderService.isSet = false ;
            Intent pushIntent = new Intent(context, RemainderService.class);
            context.startService(pushIntent);
            return;
        }

        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
                Log.i(TAG, "Network " + ni.getTypeName() + " connected");

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(TAG, "There's no network connectivity");
            }
        }

        if(!intent.getBooleanExtra("FLAG",false)){
            return ;
        }

        this.parent = context;
        this.intent = intent ;
        mManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        c= context;
        Log.d("SERVICE","received broadcast intent" + intent.getAction());
        long time = intent.getLongExtra("TIME",0);
        Calendar d = Calendar.getInstance();
        if(time != 0){
                Calendar c = Calendar.getInstance();
                //c.setTimeInMillis(time);
                c.set(Calendar.HOUR_OF_DAY,8);
                c.set(Calendar.MINUTE,15);
                Log.d("ALARM-RECEIVER","Broadcast event received for event at" + c.getTime().toString() + " received at " + d.getTime().toString());
                populate();
                Log.d("ALARM-RECEIVER",msg);

                if( (d.getTimeInMillis() - c.getTimeInMillis()) <  60*60*1000){
                    populate() ;
                    //msg = "Hello Worlds";
                    msg= msg == null ? "Hurray! No classes Today" : msg  ;
                    showNotification(msg);
                    return;
                }
            Log.d("ALARM-RECEIVER","Event already expired no notification shown!!");
        }


        }

    String msg ;
    String department,year ;
    Context parent ;
    private void populate() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(parent);
        department = settings.getString("DEPARTMENT","BME");
        year = settings.getString("YEAR","II");
        String fileDirName = settings.getString("LOCATION",null);

        Log.d("DEBUG","REceived intent "  + intent.getPackage());
        DataLoader loader = new DataLoader(fileDirName,department,year, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        String first_time = loader.getFirstTime() ;
        if(first_time ==null){
            msg =  null ;
        } else {
            msg =  "Your classes will start at "+ first_time.split("-")[0] + ".\n" +
                    "Click here to know the classes today.";
        }
    }
}
