package com.aashiz.ercroutine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class FirstRun extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener {
	String fileName ;
	boolean isFirstTime ;
	boolean flag = false ;
	String[] items= {"I-A", "I-B" ,"II-A" ,"II-B","III","IV"};
	String[] ditems = {"I","II","III","IV"};

	@Override
	public void onBackPressed() {
		if(isFirstTime) {
			Toast.makeText(this, "Please Select your department and semester and press GO", Toast.LENGTH_SHORT).show();
		}else {
				super.onBackPressed();
			}

	}


	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
		fileName = getFilesDir().getAbsolutePath();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_run);
		
		isFirstTime = getIntent().getBooleanExtra("ISFIRSTTIME",true);
		//

        
	    Button button = (Button)findViewById(R.id.button);
		button.setOnClickListener(this);


		Spinner y = (Spinner)findViewById(R.id.department);
		y.setOnItemSelectedListener(this);
		
		
		}

	@Override
	public void onClick(View v) {
		Spinner y = (Spinner)findViewById(R.id.year);
		Spinner d = (Spinner)findViewById(R.id.department);

		String department = (String)d.getSelectedItem();
		String year = (String)y.getSelectedItem();



		Toast.makeText(this,department + " - " + year ,Toast.LENGTH_SHORT).show();

		try {
			setupC(department, year);
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("DEBUG","Failed in Class-Schedule First Run");
		}
		try {
			setupE(department, year);
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("DEBUG","Failed in Exam-schedule First Run");
		}
		try {
			setupN(department, year);
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("DEBUG","Failed in notes First Run");
		}


		Intent n = new Intent(this,MainActivity.class);
		n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(n);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor edit = settings.edit();
		edit.putString("DEPARTMENT",department);
		edit.putString("YEAR",year);
		edit.putInt("LASTOPENED",R.id.nav_schedule);
		edit.putBoolean("FIRST_RUN",false);
		edit.putString("LOCATION",getFilesDir().getAbsolutePath());
		edit.commit();
		finish();
	}

	private void setupC(String department,String year) throws IOException {
		//Class-Schedule
			String filename =  "class-schedule/"+department+"-"+year+".json" ;

				File ofile = new File(fileName + "/" +filename) ;
				new File(fileName + "/class-schedule").mkdir();

		if(ofile.createNewFile()){
			FileOutputStream ostream = new FileOutputStream(ofile);

			InputStream istream = getAssets().open(filename);

			int k ;
			while((k=istream.read() )!= -1){
				ostream.write(k);
				ostream.flush();
			}
			ostream.close();
			istream.close();

		}



	}

	private void setupE(String department,String year) throws IOException{
		String filename = "exam-routine/" + department + "-" + year + "_examroutine.json";
		File ofile = new File(fileName + "/" +filename) ;
		new File(fileName + "/exam-routine").mkdir();
		int k ;
		if(ofile.createNewFile()){
			FileOutputStream ostream = new FileOutputStream(ofile);
			InputStream istream = getAssets().open(filename); //for another file
			while( (k =istream.read())!= -1){
				ostream.write(k);
				ostream.flush();
			}
			istream.close();
			ostream.close();
		}
		}

	private void setupN(String department,String year) throws IOException{
		String filename = "notes/" + department + "-" + year + ".json";
		File ofile = new File(fileName + "/" +filename) ;
		new File(fileName + "/notes").mkdir();
		int  k;
		if(ofile.createNewFile()){
			FileOutputStream ostream = new FileOutputStream(ofile);
			InputStream istream = getAssets().open(filename); //for another file
			while( (k =istream.read())!= -1){
				ostream.write(k);
				ostream.flush();
			}
			istream.close();
			ostream.close();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner k = (Spinner)findViewById(R.id.year);
		SpinnerAdapterImp ad;

		TextView y = (TextView) view ;
		if(y.getText().equals("BCE")  ){
			ad = new SpinnerAdapterImp(this.getBaseContext(),items) ;
			k.setAdapter(ad);
			flag = true ;

		}else {
			ad = new SpinnerAdapterImp(this.getBaseContext(),ditems);
			k.setAdapter(ad);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
