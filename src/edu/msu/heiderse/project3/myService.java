package edu.msu.heiderse.project3;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


@SuppressLint("ShowToast")
public class myService extends Service {
	private int timer = 3000;
	

	public class LocalBinder extends Binder 
	{
		public myService getService()
		{
			return myService.this;
		}
		
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
	super.onCreate();
 
		// Initialise UI elements
		handler = new Handler();
	
	
		
		// Get the location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

	}

	/*
	//call this to start the service
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
    	    	registerListeners();
    	doSomethingOnService();
        return START_STICKY;
    }
*/
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		running=true;
		
		registerListeners();
		
		
		doSomething();
	}
	
	//call this to kill the service
    @Override
    public void onDestroy() {
    	
    	unregisterListeners();

        super.onDestroy();
      

    }
   
    
    private int testFlag = 0;
	private String closestPlace;
	private double closestDistance;
	private final String closest = "CLOSEST";
    //Enter code to check location and create notifications
    public void doSomething()  {
    	// load closest Target: name and distance
		HashMap<String, Double> closestTarget = getClosestDistance();
		for(String s:closestTarget.keySet()){
			closestPlace = s;
		}
		
		for(Double d:closestTarget.values()){
			closestDistance = d;
		}
		
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(closest, closestPlace);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		

		
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(this.getString(R.string.app_name));
		
		builder.setAutoCancel(true);
		builder.setContentIntent(pendingIntent);
		
		builder.setContentText( String.valueOf(closestDistance));
		NotificationManager mNotificationManager =
					(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		if(closestDistance>100 && testFlag ==1){
			testFlag =0;
		}
		
		if(closestDistance<100 &&testFlag ==0){
				mNotificationManager.notify(0, builder.build());
				testFlag = 1;
		  }
		

		timer = (int) closestDistance*30;

		//mNotificationManager.notify(0, builder.build());
		
		
		

		  /*
		if(testFlag ==0){
			mNotificationManager.notify(0, builder.build());
			testFlag=1;
		}*/
		
    	Log.i("service","did Something");
    	
    	
    	
    	Log.i("CURRENT LAT", String.valueOf(latitude));
    	Log.i("CURRENT LONG", String.valueOf(longitude));
    	Log.i("TO SPARTY", String.valueOf(toSparty));
    	Log.i("TO BRESLIN", String.valueOf(toBreslin));
    	Log.i("TO BEAUMONT", String.valueOf(toBeaumont));
    	Log.i("CLOSEST DISTANCE", String.valueOf(getClosestDistance()));
    	
    	
    	if(running)
    	{
	    	handler.postDelayed(new Runnable() 
	    	{
	    		  @Override
	    		  public void run() 
	    		  {
	    			  doSomething();
	    		  }
	    	}, timer);
    	}
    }
    
    public void StopEverything()
    {
    	running=false;
    }
    
    private final LocalBinder mBinder = new LocalBinder();
    protected Handler handler;
  
    private static boolean running=false;
	private final static double BeaumontLatitude = 42.731746;
	private final static double BeaumontLongitude = -84.4826998;
	
	private final static double SpartyLatitude = 42.7302552;
	private final static double SpartyLongitude = -84.4889347;
	
	private final static double BreslinLatitude = 42.7271658;
	private final static double BreslinLongitude = -84.4901256;
	
	
	private LocationManager locationManager = null;
	
	// current location
	private double latitude;
	private double longitude;
	
	
	// Distances to ...
	private double toSparty;
	private double toBeaumont;
	private double toBreslin;
	
	private boolean valid = false;
	public boolean getValid() {
		return valid;
	}
	
	
	private class ActiveListener implements LocationListener {

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			registerListeners();
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}        
    };
    
    private ActiveListener activeListener = new ActiveListener();	
    
    
    
    public void updateUI() {
    	// update UI....
    }
    
    private void onLocation(Location location) {
    	if(location == null) {
            return;
        }
        
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        valid = true;

        updateUI();
    }
    
    private void registerListeners() {
    	unregisterListeners();
    	
    	 // Create a Criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        
        String bestAvailable = locationManager.getBestProvider(criteria, true);
        
        if(bestAvailable != null) {
            locationManager.requestLocationUpdates(bestAvailable, 500, 1, activeListener);
            //TextView viewProvider = (TextView)findViewById(R.id.textProvider);
            //viewProvider.setText(bestAvailable);            
            Location location = locationManager.getLastKnownLocation(bestAvailable);
            onLocation(location);
        }
    }
    
    
    private void unregisterListeners() {
    	locationManager.removeUpdates(activeListener);
        
    }
    
    // get distance from current location to (lat, lon)
    //
    public double getDistanceTo(double lat, double lon) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        
        String bestAvailable = locationManager.getBestProvider(criteria, true);
        
        if(bestAvailable != null) {
            locationManager.requestLocationUpdates(bestAvailable, 500, 1, activeListener);
            //TextView viewProvider = (TextView)findViewById(R.id.textProvider);
            //viewProvider.setText(bestAvailable);            
            Location location = locationManager.getLastKnownLocation(bestAvailable);
            onLocation(location);
        }
        
    	float[] results = new float[5];
    	Location.distanceBetween(latitude, longitude, lat, lon, results);
    	return results[0];
    }
    
    // Get the <name_of_location : distance> of the closest campus location.
    // Also sets the three distance variables.
    //
    
    public HashMap<String, Double> getClosestDistance() {
    	List<Double> list = Arrays.asList(	toSparty = getDistanceTo(SpartyLatitude, SpartyLongitude), 
    										toBeaumont = getDistanceTo(BeaumontLatitude, BeaumontLongitude), 
    										toBreslin = getDistanceTo(BreslinLatitude, BreslinLongitude));
    	
    	
    	final double dClosest = Collections.min(list);
    	final String loc; 
    	if (dClosest == toSparty) {
    		loc = "Sparty";
    	} else if (dClosest == toBeaumont) {
    		loc = "Beaumont";
    	} else {
    		loc = "Breslin";
    	}
    	
    	HashMap<String, Double> h = new HashMap<String, Double>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
            put(loc, dClosest);
        }};
    	    	
    	return h;
    }
    	
}
