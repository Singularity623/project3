package edu.msu.heiderse.project3;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import android.util.Log;
import android.support.v4.app.NotificationCompat;
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
	


	}

	/*
	//call this to start the service
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
    	doSomethingOnService();
        return START_STICKY;
    }
*/
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		doSomething(10);
	}
	
	//call this to kill the service
    @Override
    public void onDestroy() {

        super.onDestroy();
      

    }
   
    
    private int testFlag = 0;
    //Enter code to check location and create notifications
    public void doSomething(final int i)  {
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(this.getString(R.string.app_name));
		builder.setContentText("Nothing Important!");
		builder.setAutoCancel(true);
		builder.setContentIntent(pendingIntent);
		
		final NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		  
		if(testFlag ==0){
			mNotificationManager.notify(0, builder.build());
			testFlag=1;
		}
		
    	Log.i("service","did Something");
    	if(i>0)
    	{
	    	handler.postDelayed(new Runnable() 
	    	{
	    		  @Override
	    		  public void run() 
	    		  {
	    			  doSomething(i-1);
	    	    }
	    	}, 1000);
    	}
    }
    
    
    
    private final LocalBinder mBinder = new LocalBinder();
    protected Handler handler;
  

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
