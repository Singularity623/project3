package edu.msu.heiderse.project3;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
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

	        return START_STICKY;
	    }
		
		
	
		//call this to kill the service
	    @Override
	    public void onDestroy() {
	        // TODO Auto-generated method stub
	    	mToast = null;
	        super.onDestroy();
	      

	    }
	   
	    public void doSomethingOnService()  {
	    
	    }
	    
	    
	    private final LocalBinder mBinder = new LocalBinder();
	    protected Handler handler;
	   protected Toast mToast;
	   private boolean search=false;

		private final static double BelmontLattitude = 42.731746;
		private final static double BelmontLongitude = -84.4826998;
		
		private final static double SpartyLattitude = 42.7302552;
		private final static double SpartyLongitude = -84.4889347;
		
		private final static double BreslinLattitude = 42.7271658;
		private final static double BreslinLongitude = -84.4901256;
}
