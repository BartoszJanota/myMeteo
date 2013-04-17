package com.example.mymeteo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DayDescription extends Activity {
	
	// Shared preferences name
	public static final String PREFS_NAME = "MyPrefsFile";
	// JSON Node names
    public static final String TAG_DATA = "data";
    public static final String TAG_CURRENT_CONDITION = "current_condition";
    public static final String TAG_PRECIPMM = "precipMM";
    public static final String TAG_WEATHERDESC = "weatherDesc";
    public static final String TAG_VALUE = "value";
    public static final String TAG_VALUEURL = "valueURL";
    public static final String TAG_WEATHERICONURL = "weatherIconUrl";
    public static final String TAG_WINDDIR16POINT = "winddir16Point";
    public static final String TAG_WINDSPEEDKMPH = "windspeedKmph";
    public static final String TAG_WINDSPEEDMILES = "windspeedMiles";
    public static final String TAG_REQUEST = "request";
    public static final String TAG_QUERY = "query";
    public static final String TAG_IMAGE = "image";
    public static final String TAG_WEATHER = "weather";
    public static final String TAG_DATE = "date";
    public static final String TAG_TEMPMAXC = "tempMaxC";
    public static final String TAG_TEMPMINC = "tempMinC";
    public static final String TAG_TEMPMAXF = "tempMaxF";
    public static final String TAG_TEMPMINF = "tempMinF";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_description);
		
		// Getting Intent from MainActivity
		Intent intent = getIntent();
	    String date = intent.getStringExtra(MainActivity.TAG_DATE);
	    String precipMM = intent.getStringExtra(MainActivity.TAG_PRECIPMM);
	    String tempMaxC = intent.getStringExtra(MainActivity.TAG_TEMPMAXC);
	    String tempMaxF = intent.getStringExtra(MainActivity.TAG_TEMPMAXF);
	    String tempMinC = intent.getStringExtra(MainActivity.TAG_TEMPMINC);
	    String tempMinF = intent.getStringExtra(MainActivity.TAG_TEMPMINF);
	    String value = intent.getStringExtra(MainActivity.TAG_VALUE);
	    String winddir16Point = intent.getStringExtra(MainActivity.TAG_WINDDIR16POINT);
	    String windSpeedKMPH= intent.getStringExtra(MainActivity.TAG_WINDSPEEDKMPH);
	    String windSpeedMiles = intent.getStringExtra(MainActivity.TAG_WINDSPEEDMILES);	
    	
	    // Building UI
	    TextView textDate = (TextView) findViewById(R.id.date);
        textDate.setText("for: " + date);
    	TextView textValueDesc = (TextView) findViewById(R.id.valueDesc);
        textValueDesc.setText("outside: " + value);
        TextView textPrecipMM = (TextView) findViewById(R.id.precipMM);
        textPrecipMM.setText("precipitation: " + precipMM + " mm");
        TextView textWindDir = (TextView) findViewById(R.id.windDir);
        textWindDir.setText("wind direction: " + winddir16Point);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		boolean isKilometres = settings.getBoolean("isKilometres",true);
		if (isKilometres){ 
			TextView textWindSpeed = (TextView) findViewById(R.id.windSpeed);
            textWindSpeed.setText("wind speed: " + windSpeedKMPH + " KMPH");
		}
		else{ 
			TextView textWindSpeed = (TextView) findViewById(R.id.windSpeed);
            textWindSpeed.setText("wind speed: " + windSpeedMiles + " MPH");
		}
		boolean isCelsius = settings.getBoolean("isCelsius",true);
		if (isCelsius){ 
			TextView textTemp = (TextView) findViewById(R.id.temp);
            textTemp.setText("min/max: " + tempMinC + "/" + tempMaxC + " " + (char) 0x00B0 + "C" );
		}
		else{ 
			TextView textTemp = (TextView) findViewById(R.id.temp);
            textTemp.setText("min/max: " + tempMinF + "/" + tempMaxF + " " + (char) 0x00B0 + "F" );
		}
		
		// Getting a Parcelable Object from Intent
	    Bitmap image = intent.getParcelableExtra(MainActivity.TAG_IMAGE);    
    	ImageView icon = (ImageView) findViewById(R.id.imageView);
    	icon.setImageBitmap(image);
	}
}
