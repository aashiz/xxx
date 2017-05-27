package com.aashiz.ercroutine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aashiz.ercroutine.utilities.AlarmReceiver;

import java.util.Calendar;

public class RemainderService extends Service {
    public static boolean isSet = false;


    public RemainderService() {

    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    AlarmManager mManager;

    @Override
    public void onCreate() {
        Log.d("SERVICE", "Inside service onCreate");
        super.onCreate();
        mManager = (AlarmManager) getSystemService(ALARM_SERVICE);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isSet){
            Intent i = new Intent(getBaseContext(),AlarmReceiver.class);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY,8);
            c.set(Calendar.MINUTE,15);
            i.putExtra("TIME",c.getTimeInMillis());
            i.putExtra("FLAG",true);
            PendingIntent in = PendingIntent.getBroadcast(getBaseContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            mManager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,in);
            isSet = true ;
        }
        return START_STICKY;
    }
}
