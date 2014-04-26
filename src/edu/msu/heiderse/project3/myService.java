package edu.msu.heiderse.project3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class myService extends Service {

	public myService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	 @SuppressWarnings("deprecation")
	@Override
	    public void onStart(Intent intent, int startId) {
	        // TODO Auto-generated method stub
	        super.onStart(intent, startId);
	    }
	    @Override
	    public void onDestroy() {
	        // TODO Auto-generated method stub
	        super.onDestroy();
	    }

}
