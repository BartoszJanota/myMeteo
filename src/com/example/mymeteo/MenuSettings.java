package com.example.mymeteo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MenuSettings extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_settings);
		
		// Getting data stored in Shared Preferences to set default values of units on start
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		boolean isKilometres = settings.getBoolean("isKilometres",true);
		if (isKilometres){ 
			RadioButton degreeButton = (RadioButton) findViewById(R.id.radio_KMH);
			degreeButton.setChecked(true);
		}
		else{ 
			RadioButton degreeButton = (RadioButton) findViewById(R.id.radio_MPH);
			degreeButton.setChecked(true);
		}
		boolean isCelsius = settings.getBoolean("isCelsius",true);
		if (isCelsius){ 
			RadioButton degreeButton = (RadioButton) findViewById(R.id.radio_C);
			degreeButton.setChecked(true);
		}
		else{ 
			RadioButton degreeButton = (RadioButton) findViewById(R.id.radio_F);
			degreeButton.setChecked(true);
		}
	}
	
	// Called when the Radio button is clicked
	public void onRadioButtonClickedDegree(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_C:
	            if (checked){
	            	SharedPreferences.Editor editor = settings.edit();
	        	    editor.putBoolean("isCelsius", true);
	        	    //Commit the edits
	        	    editor.commit();
	        	    break;
	            }
	        case R.id.radio_F:
	            if (checked){
	            	SharedPreferences.Editor editor = settings.edit();
	        	    editor.putBoolean("isCelsius", false);
	        	    //Commit the edits
	        	    editor.commit();
	        	    break;
	            }
	    }
	}
	
	// Called when the Radio button is clicked
	public void onRadioButtonClickedSpeed(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_KMH:
	            if (checked){
	            	SharedPreferences.Editor editor = settings.edit();
	        	    editor.putBoolean("isKilometres", true);
	        	    //Commit the edits
	        	    editor.commit();
	        	    break;
	            }
	        case R.id.radio_MPH:
	            if (checked){
	            	SharedPreferences.Editor editor = settings.edit();
	        	    editor.putBoolean("isKilometres", false);
	        	    //Commit the edits
	        	    editor.commit();
	        	    break;
	            }
	    }
	}
}
