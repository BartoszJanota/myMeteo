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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Forecast extends ListActivity {
	
	private ProgressDialog progressDialog;
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
		setContentView(R.layout.activity_forecast);
		startFetching();
	}
	
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
    	    //if positive, fetch the articles in background
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
            progressDialog = new ProgressDialog(Forecast.this);
            progressDialog.setMessage("Loading ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
 
        //fetching all articles from the json and arrange them in list view
        @Override
        protected String doInBackground(String... args) {
        	String url = "http://free.worldweatheronline.com/feed/weather.ashx?q=Cracow,Poland&format=json&num_of_days=5&key=652e93c9d6153629131202";

        	JSONObject json = jParser.getJSONFromUrl(url);
            
            try {
                // Getting Array of Entries
            	JSONObject data = json.getJSONObject(TAG_DATA);
                
                current_condition = data.getJSONArray(TAG_CURRENT_CONDITION);
                weather = data.getJSONArray(TAG_WEATHER);
                request = data.getJSONArray(TAG_REQUEST);
          
                JSONObject a = current_condition.getJSONObject(0);
                String precipMM = a.getString(TAG_PRECIPMM);
                weatherDescCurr = a.getJSONArray(TAG_WEATHERDESC);
                JSONObject e = weatherDescCurr.getJSONObject(0);
                String valueDesc = e.getString(TAG_VALUE);
                weatherIconUrlCurr = a.getJSONArray(TAG_WEATHERICONURL);
                JSONObject g = weatherIconUrlCurr.getJSONObject(0);
                String valueURL = g.getString(TAG_VALUE);
                String winddir16Point = a.getString(TAG_WINDDIR16POINT);
                String windSpeedKMPH = a.getString(TAG_WINDSPEEDKMPH);
                String windSpeedMiles = a.getString(TAG_WINDSPEEDMILES);
                
             // looping through all items to put them into ArrayList
                for(int i1 = 0; i1 < weather.length(); i1++){
                    JSONObject c = weather.getJSONObject(i1);
                    
                    // Storing each json item in variable c
                    String date = c.getString(TAG_DATE);
                    precipMM = c.getString(TAG_PRECIPMM);
                    String tempMaxC = c.getString(TAG_TEMPMAXC);
                    String tempMaxF = c.getString(TAG_TEMPMAXF);
                    String tempMinC = c.getString(TAG_TEMPMINC);
                    String tempMinF = c.getString(TAG_TEMPMINF);
                    weatherDesc = c.getJSONArray(TAG_WEATHERDESC);
                    JSONObject b = weatherDesc.getJSONObject(0);
                    valueDesc = b.getString(TAG_VALUE);
                    weatherIconUrl = c.getJSONArray(TAG_WEATHERICONURL);
                    JSONObject d = weatherIconUrl.getJSONObject(0);
                    valueURL = d.getString(TAG_VALUE);
                    winddir16Point = c.getString(TAG_WINDDIR16POINT);
                    windSpeedKMPH = c.getString(TAG_WINDSPEEDKMPH);
                    windSpeedMiles = c.getString(TAG_WINDSPEEDMILES);
                    
                    // new HashMap
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    hashMap.put(TAG_DATE, date);
                    hashMap.put(TAG_PRECIPMM, precipMM);
                    hashMap.put(TAG_TEMPMAXC, tempMaxC);
                    hashMap.put(TAG_TEMPMAXF, tempMaxF);
                    hashMap.put(TAG_TEMPMINC, tempMinC);
                    hashMap.put(TAG_TEMPMINF, tempMinF);
                    hashMap.put(TAG_VALUE, valueDesc);
                    hashMap.put(TAG_VALUEURL, valueURL);
                    hashMap.put(TAG_WINDDIR16POINT, winddir16Point);
                    hashMap.put(TAG_WINDSPEEDKMPH, windSpeedKMPH);
                    hashMap.put(TAG_WINDSPEEDMILES, windSpeedMiles);
                    // adding HashMap to ArrayList
                    entryList.add(hashMap);
                }
            } catch (JSONException e) {
                e.printStackTrace();
             }
 
            runOnUiThread(new Runnable() {
                public void run() {
                	
                    // updating listview with the parsed items
                	ListAdapter adapter = new SimpleAdapter(Forecast.this, entryList,
                            R.layout.list_item,
                            new String[] { TAG_DATE, TAG_VALUE}, 
                            new int[] {R.id.date, R.id.valueDesc});
                    setListAdapter(adapter);
                    	
                    // selecting single ListView item
                    ListView lv = getListView();
             
                    // Launching new screen on Selecting Single ListItem
                    lv.setOnItemClickListener(new OnItemClickListener() {
             
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
                            
                        	// getting values from selected ListItem
                            // Starting new intent
                        	
                        	Intent in = new Intent(getApplicationContext(), DayDescription.class);
                        	// Looping throw ArrayList to find the proper data put into Extra
                        	for (HashMap<String,String> s : entryList){
                            	if (s.get(TAG_DATE) == ((TextView) view.findViewById(R.id.date)).getText().toString()){
                            		// putting Exstras to show the proper data in DayDescription
                            		in.putExtra(TAG_DATE, s.get(TAG_DATE));
                            		in.putExtra(TAG_PRECIPMM, s.get(TAG_PRECIPMM));
                            		in.putExtra(TAG_TEMPMAXC, s.get(TAG_TEMPMAXC));
                            		in.putExtra(TAG_TEMPMAXF, s.get(TAG_TEMPMAXF));						
                            		in.putExtra(TAG_TEMPMINC, s.get(TAG_TEMPMINC));
                            		in.putExtra(TAG_TEMPMINF, s.get(TAG_TEMPMINF));
                            		in.putExtra(TAG_VALUE, s.get(TAG_VALUE));
                            		in.putExtra(TAG_VALUEURL, s.get(TAG_VALUEURL));
                            		in.putExtra(TAG_WINDDIR16POINT, s.get(TAG_WINDDIR16POINT));
                            		in.putExtra(TAG_WINDSPEEDKMPH, s.get(TAG_WINDSPEEDKMPH));
                            		in.putExtra(TAG_WINDSPEEDMILES, s.get(TAG_WINDSPEEDMILES));
                            		// Getting an image from URL stored in HashMap
                            		Bitmap image = new ImageFromUrl().loadImage(s.get(TAG_VALUEURL));
                            		// putting Parcelable object
                            		in.putExtra(TAG_IMAGE, image);
                            	}
                            }
                            startActivity(in);
                        }
                    });
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
    class ImageFromUrl extends Thread{
    	
		public ImageFromUrl(){}
		
		public Bitmap loadImage(String imageUrl)  {
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
		getMenuInflater().inflate(R.menu.forecast, menu);
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
		case R.id.action_about:
			Intent intent_about = new Intent(this, MenuAbout.class);
			startActivity(intent_about);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
