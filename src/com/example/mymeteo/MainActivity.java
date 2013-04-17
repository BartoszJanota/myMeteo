package com.example.mymeteo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	private ProgressDialog progressDialog;
	// Shared preferences name
	public static final String PREFS_NAME = "MyPrefsFile";
	// JSON Node names
    public static final String TAG_DATA = "data";
    public static final String TAG_CURRENT_CONDITION = "current_condition";
    public static final String TAG_CLOUDCOVER = "cloudcover";
    public static final String TAG_HUMIDITY = "humidity";
    public static final String TAG_PRECIPMM = "precipMM";
    public static final String TAG_PRESSURE = "pressure";	
    public static final String TAG_TEMP_C = "temp_C";
    public static final String TAG_TEMP_F = "temp_F";
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
    
    public String cloudcover;
    public String temp_C;
    public String temp_F;
    public String humidity;
    public String precipMM;
    public String pressure;
    public String valueDesc;
    public String valueURL;
    public String winddir16Point;
    public String windSpeedKMPH;
    public String windSpeedMiles;
    public String query;
    
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> entryList = new ArrayList<HashMap<String, String>>();
    Parser jParser = new Parser();
    
    // JSONArrays
    JSONArray weather = null;
    JSONArray current_condition = null;
    JSONArray weatherDesc = null;
    JSONArray weatherDescCurr = null;
    JSONArray weatherIconUrl = null;
    JSONArray weatherIconUrlCurr = null;
    JSONArray request = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startFetching();
	}
	
	// To refresh data when clicking Back button
	@Override
	protected void onRestart(){
		super.onRestart();
		setContentView(R.layout.activity_main);
		startFetching();
	}
	
	// Main of the program
	protected void startFetching(){
        
		//url from where the JSON has to be retrieved
    	String url = "http://free.worldweatheronline.com/feed/weather.ashx?q=Cracow,Poland&format=json&num_of_days=5&key=652e93c9d6153629131202";
    	
    	//Checking if the user has an internet connection. If negative, show a toast 
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo info = cm.getActiveNetworkInfo();
    	if (info != null) {
    	    if (!info.isConnected()) {
    	        Toast.makeText(this, "Please check your wireless connection and try again.", Toast.LENGTH_SHORT).show();
    	    }
    	    //if positive, fetch the articles in background thread
    	    else new fetchArticles().execute(url);
    	}	
    	else {
    	    Toast.makeText(this, "Please check your wireless connection and try again.", Toast.LENGTH_SHORT).show();
    	}
	}
	
    class fetchArticles extends AsyncTask<String, String, String> {
   	 
        // progress dialog while task is being carried on in background
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
 
        //fetching all articles from the json
        @Override
        protected String doInBackground(String... args) {
        	
        	String url = "http://free.worldweatheronline.com/feed/weather.ashx?q=Cracow,Poland&format=json&num_of_days=5&key=652e93c9d6153629131202";
        	JSONObject json = jParser.getJSONFromUrl(url);
            
            try {
                // Getting Arrays from the major json object
            	JSONObject data = json.getJSONObject(TAG_DATA);
                current_condition = data.getJSONArray(TAG_CURRENT_CONDITION);
                weather = data.getJSONArray(TAG_WEATHER);
                request = data.getJSONArray(TAG_REQUEST);
                // Getting values from json
                JSONObject a = current_condition.getJSONObject(0);
                cloudcover = a.getString(TAG_CLOUDCOVER);
                humidity = a.getString(TAG_HUMIDITY);
                precipMM = a.getString(TAG_PRECIPMM);
                pressure = a.getString(TAG_PRESSURE);
                temp_C = a.getString(TAG_TEMP_C);
                temp_F = a.getString(TAG_TEMP_F);
                weatherDescCurr = a.getJSONArray(TAG_WEATHERDESC);
                JSONObject e = weatherDescCurr.getJSONObject(0);
                valueDesc = e.getString(TAG_VALUE);
                weatherIconUrlCurr = a.getJSONArray(TAG_WEATHERICONURL);
                JSONObject g = weatherIconUrlCurr.getJSONObject(0);
                valueURL = g.getString(TAG_VALUE);
                winddir16Point = a.getString(TAG_WINDDIR16POINT);
                windSpeedKMPH = a.getString(TAG_WINDSPEEDKMPH);
                windSpeedMiles = a.getString(TAG_WINDSPEEDMILES);
                JSONObject h = request.getJSONObject(0);
                query = h.getString(TAG_QUERY);   
            } catch (JSONException e) {
                e.printStackTrace();
              }
            
            // Running UI and setting views
            runOnUiThread(new Runnable() {
                public void run() {
                	
                	// Checking units stored in Shared Preferences
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
                        textTemp.setText(temp_C + " " + (char) 0x00B0 + "C" );
            		}
            		else{ 
            			TextView textTemp = (TextView) findViewById(R.id.temp);
                        textTemp.setText(temp_F + " " + (char) 0x00B0 + "F");
            		}
                	
            		// Getting an image from URL
            		new ImageFromUrl().run();
            		
                	TextView textLocation = (TextView) findViewById(R.id.query);
                    textLocation.setText(query);
                	TextView textValueDesc = (TextView) findViewById(R.id.valueDesc);
                    textValueDesc.setText("outside: " + valueDesc);
                    TextView textPressure = (TextView) findViewById(R.id.pressure);
                    textPressure.setText("pressure: " + pressure + " hPa");
            		TextView textCloudCover = (TextView) findViewById(R.id.cloudcover);
                    textCloudCover.setText("cloudcover: " + cloudcover + " %");
                    TextView textHumidity = (TextView) findViewById(R.id.humidity);
                    textHumidity.setText("humidity: " + humidity + " %");
                    TextView textPrecipMM = (TextView) findViewById(R.id.precipMM);
                    textPrecipMM.setText("precipitation: " + precipMM + " mm");
                    TextView textWindDir = (TextView) findViewById(R.id.windDir);
                    textWindDir.setText("wind direction: " + winddir16Point);

                }
            });
            return null;
        } 	
 
        //on execution of the task, dismiss the progress dialog
        protected void onPostExecute(String args) {
            progressDialog.dismiss();
        }
        
    }
    
    // Class to get an image from URL
    class ImageFromUrl extends Thread {
		
		public ImageFromUrl(){}
		
		// Setting an image
		public void run(){
			Bitmap image = loadImage(valueURL);
        	ImageView icon = (ImageView) findViewById(R.id.imageView);
        	icon.setImageBitmap(image);
		}
		
		public Bitmap loadImage(String imageUrl) {
			try {
				HttpGet httpRequest = new HttpGet(imageUrl);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
				InputStream instream = bufHttpEntity.getContent();
				Bitmap bmp = BitmapFactory.decodeStream(instream);
				return bmp;
			} catch (Exception e){
				return null;
			}
		}
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
   
	// Called when the menu button is clicked
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
		    Intent intent_settings = new Intent(this, MenuSettings.class);
			startActivity(intent_settings);
			return true;
		case R.id.action_refresh:
			this.onRestart();
			return true;
		case R.id.action_about:
			Intent intent_about = new Intent(this, MenuAbout.class);
			startActivity(intent_about);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Called when the user clicks the forecast button
	public void goForecast(View view) {
	    Intent intent = new Intent(this, Forecast.class);
		startActivity(intent);
	}
}
