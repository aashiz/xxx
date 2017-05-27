package com.aashiz.ercroutine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.aashiz.ercroutine.fragments.About;
import com.aashiz.ercroutine.fragments.ExamRoutine;
import com.aashiz.ercroutine.fragments.FragmentNote;
import com.aashiz.ercroutine.fragments.MainFragment;
import com.aashiz.ercroutine.fragments.PopUpFactory;
import com.aashiz.ercroutine.fragments.ScheduleFragment;
import com.aashiz.ercroutine.fragments.SyllabusFragment;
import com.aashiz.ercroutine.utilities.DataReader;
import com.aashiz.ercroutine.utilities.UpdaterService;
import com.aashiz.ercroutine.utilities.UpdaterServiceApp;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String USERPROPERTY = "DEPARTMENT_YEAR";
    public static MainActivity FilesDir ;
    FrameLayout fragmentContainer ;
    PopUpFactory progressBar ;
    DisplayMetrics dm ;
    private String department ;
    private String year ;
//    private FirebaseAnalytics mFirebase ;
    private int lastOpened ;
    public static int CLASS_SCHEDULE = R.id.nav_schedule, EXAM_ROUTINE = R.id.nav_routine , NOTES = R.id.nav_notes , SYLLABUS = R.id.nav_syllabus,ABOUT =R.id.nav_about;
    HashMap <String,String> map = new HashMap<>();
    private String selectedFragment;
    private NavigationView navigationView ;
    int currSelected ;
    MainFragment mainfrag,saved_frag ;
    boolean cflag = false,eflag=false;
    private boolean nflag =  false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    /**
     * Checks if the user is opening the app for the first time.
     * Note that this method should be placed inside an activity and it can be called multiple times.
     * @return boolean
     */


    private boolean isFirstTime() {

            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            return mPreferences.getBoolean("FIRST_RUN", true);


    }
//    InterstitialAd interad ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FilesDir = this;
        super.onCreate(savedInstanceState);
        //Checking of First Run and setting of Data...\]
        map.put("I","First");
        map.put("I-A","First");
        map.put("I-B","First");
        map.put("II","Second");
        map.put("II-A","Second");
        map.put("II-B","Second");
        map.put("III","Third");
        map.put("IV","Fourth");
        if(isFirstTime()){ //If first time run first run.java

            Intent i = new Intent(this,FirstRun.class) ;
            i.putExtra("ISFIRSTTIME",true);
            startActivity(i);
            finish();
            return;
        }

//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3059651012850041~9878199816");
        //Get Data from Internal Storages
        setContentView(R.layout.activity_main);
        SharedPreferences set = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        department = set.getString("DEPARTMENT","BME");
        year = set.getString("YEAR","II");
        lastOpened = set.getInt("LASTOPENED",CLASS_SCHEDULE);
        Log.d("DEBUG","Last opened = "+lastOpened);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(department +" - " + map.get(year) + " Year");
//        dm =  new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(lastOpened);
        currSelected = lastOpened ;
        fragmentContainer = (FrameLayout)findViewById(R.id.fragment);
        saved = (FrameLayout)findViewById(R.id.saved_fragment);



//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        interad = new InterstitialAd(this);
//        interad.setAdUnitId("ca-app-pub-3059651012850041/6823341811");
//        interad.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                Log.d("ADV","Advertisement loaded...");
//            }
//
//            @Override
//            public void onAdClosed() {
//                requestNewInterstitial();
//
//            }
//        });
//
//        mFirebase  = FirebaseAnalytics.getInstance(this);
//        mFirebase.setUserProperty(USERPROPERTY,department +"-"+ year );
//        Log.d("DEBUG","Selectedfragment = "+selectedFragment);
//

        //Pre loading syllabus fragment
      preload();


        requestNewInterstitial();
        callFragment(lastOpened);

         intentService = new Intent(this,RemainderService.class);
        Log.d("DEBUG","Service status " + isMyServiceRunning(RemainderService.class));
        if(!isMyServiceRunning(RemainderService.class)) {
            startService(intentService);
        }
        //Toast.makeText(this,"Width="+dm.widthPixels+" Height="+dm.heightPixels+" Density="+dm.densityDpi,Toast.LENGTH_SHORT).show();
    }
    Intent intentService ;
    FrameLayout saved ;

    private void preload() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                saved_frag =  SyllabusFragment.newInstance(department,year);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.saved_fragment,saved_frag);
                ft.commit();
            }
        });
    }

    private void requestNewInterstitial() {
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//
//        interad.loadAd(adRequest);
//
//
//        Log.d("ADV","AD requested intestitiial " +interad.getAdUnitId());

    }


    private void callFragment(int id) {
        FragmentManager mg = getSupportFragmentManager();
        FragmentTransaction st = mg.beginTransaction();
        saved.setVisibility(View.INVISIBLE);
        fragmentContainer.setVisibility(View.VISIBLE);
        if (id == CLASS_SCHEDULE) {
            // Call Fragments
            selectedFragment = "C";
            mainfrag = ScheduleFragment.createNewInstance();
            st.replace(R.id.fragment,mainfrag);

//            st.addToBackStack(null);
            st.commit();

            if(selectedFragment !=null && !cflag) {
                new SilentUpdater(this).execute(new UpdaterService(this, department, year, selectedFragment));
                cflag = true ;
            }

        } else if (id == EXAM_ROUTINE) {
            selectedFragment ="E";
            mainfrag  = new ExamRoutine();
            st.replace(R.id.fragment,mainfrag);
            st.commit();

            if(selectedFragment !=null && !eflag) {
                new SilentUpdater(this).execute(new UpdaterService(this, department, year, selectedFragment));
                eflag = true ;
            }
        } else if (id == NOTES) {

            selectedFragment ="N";
            mainfrag = new FragmentNote() ;

            st.replace(R.id.fragment,mainfrag);
            st.commit();
            if(selectedFragment !=null && !nflag) {
                new SilentUpdater(this).execute(new UpdaterService(this, department, year, selectedFragment));
                nflag = true ;
            }
//            if(interad.isLoaded()){
//                interad.show();
//            }
        }else if(id == ABOUT){
            mainfrag= new About();
            st.replace(R.id.fragment,mainfrag);
            st.commit();
        }else if(id==SYLLABUS){
            mainfrag = saved_frag ;
//            mainfrag= SyllabusFragment.newInstance(department,year);
//            st.replace(R.id.fragment,mainfrag);
//            st.commit();
            fragmentContainer.setVisibility(View.INVISIBLE);
            saved.setVisibility(View.VISIBLE);


            Log.d("SCRTOOP",toString());
        }
        else if(id == R.id.nav_Choose){
         Intent i = new Intent(this,FirstRun.class);
            i.putExtra("ISFIRSTTIME",false);
            navigationView.setCheckedItem(R.id.nav_schedule);
            startActivity(i);
//            finishActivity(0);


        }




    }


    boolean doubleBackToExitPressedOnce = false ;
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {

//                stopService(intentService);
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            if(mainfrag == null){
                super.onBackPressed();
                return;
            }
            mainfrag.handleBackKey();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 600);


        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt("LASTOPENED",currSelected);
        edit.apply();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager m = getSupportFragmentManager();
            progressBar = new PopUpFactory(); //Get new progressbar defined in popupfactory
            progressBar.show(m,"Fragment for Update");
            DataReader read = new DataReader(this); //Data reader class has asynctask and it takes updater service
            switch(currSelected){
                case R.id.nav_schedule:
                    read.execute(new UpdaterService(this,department,year,"C"));
                    break;
                case R.id.nav_routine:
                    read.execute(new UpdaterService(this,department,year,"E"));
                    break;
                case R.id.nav_notes:
                    read.execute(new UpdaterService(this,department,year,"N"));
                    break;
                default:
                    read.execute(new UpdaterService(this,department,year,"ALL"));
            }
        }else if(id == R.id.nav_updateapp){

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if(item.getItemId() ==R.id.nav_updateapp){
            stopService(intentService);
            FragmentManager m = getSupportFragmentManager();
            progressBar = PopUpFactory.createNew("Checking For Update.."); //Get new progressbar defined in popupfactory
            progressBar.show(m,"Fragment for Update");
            DataReader read = new DataReader(this);
            read.execute(new UpdaterServiceApp(this));
            return false ;
        }
        currSelected = item.getItemId();
        currSelected =  (currSelected == R.id.nav_Choose) ||(currSelected== R.id.nav_syllabus )? R.id.nav_schedule : currSelected ;
        callFragment(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void stopProgressBar() {
        progressBar.dismiss();

    }

    public void upDateFragment() {

      try{
         mainfrag.reload();
      }catch (Exception ex){
          ex.printStackTrace();
      }

    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
