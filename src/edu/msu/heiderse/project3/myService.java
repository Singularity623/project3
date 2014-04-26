package edu.msu.heiderse.project3;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class myService extends Service {

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
	mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


	}

	//call this to start the service
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
		Toast.makeText(this, "Service has started", Toast.LENGTH_SHORT).show();
		doSomethingOnService();
        return START_STICKY;
    }
	
	//call this to kill the service
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
    	mToast = null;
        super.onDestroy();
      

    }
   
    //Enter code to check location and create notifications
    public void doSomethingOnService()  {
    
    }
    
    
    
    private final LocalBinder mBinder = new LocalBinder();
    protected Handler handler;
    protected Toast mToast;
  

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
    	float[] results = new float[1];
    	Location.distanceBetween(latitude, longitude, lat, lon, results);
    	return results[0];    	
    }
    
    // Get the distance of the closest campus location.
    // Also sets the three distance variables.
    //
    public double getClosestDistance() {
    	List<Double> list = Arrays.asList(	toSparty = getDistanceTo(SpartyLatitude, SpartyLongitude), 
    										toBeaumont = getDistanceTo(BeaumontLatitude, BeaumontLongitude), 
    										toBreslin = getDistanceTo(BreslinLatitude, BreslinLongitude));
    	  	
    	return Collections.min(list); 
    }
    
	
}
