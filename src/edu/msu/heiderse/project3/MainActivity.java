package edu.msu.heiderse.project3;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
	private final static String CLOSEST = "CLOSEST";
	private final static String BESTPROVIDER = "BESTPROVIDER";
	private final static String DISTANCE = "DISTANCE";
	
	private String closestPlace = "";
	private String bestAvailable = "";
	private double closestDistance = 0;
	
	private TextView provider;
	private TextView providerResponse;
	private TextView textTo;
	private ImageView image;
	
	boolean isUISet;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
		isUISet = false;
		service=new myService();
		

		if (savedInstanceState == null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.add(R.id.container, new  FragMain(), "top").commit();
		}
		else
		{

		}
		

		
		// Force the screen to say on and bright
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class FragMain extends Fragment {

		public FragMain() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			return rootView;
		}
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
/*	public static class FragInfo extends Fragment {

		public FragInfo() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
				View rootView = inflater.inflate(R.layout.fragment_information, container,
						false);
				return rootView;
		}
		
	    public void setVisible(int vis){
	    	this.setVisible(vis);
	    }
		
	}*/
	
	
	//start the service
	public void ServiceChange(View view)
	{
		if(!isUISet)
			setUI();
		boolean on = ((ToggleButton) view).isChecked();
		if(!on)
		{
			service=new myService();
			Intent i = new Intent(context, myService.class);
			context.startService(i);
			
			//toggle view fragment on
			updateUI(View.VISIBLE);
		}
		else
		{
			if(service!=null)
			{
				service.StopEverything();
				service.stopSelf();
			}
			//toggle view fragment off
			updateUI(View.INVISIBLE);
		}
		
	}
	
	public void updateUI(int val){
		if(val == View.VISIBLE) { //service is on 
			provider.setVisibility(View.VISIBLE);
			providerResponse.setVisibility(View.VISIBLE);
			providerResponse.setText(service.bestAvailable);
			textTo.setVisibility(View.VISIBLE);
			textTo.setText(String.valueOf(service.closestPlace));
			image.setVisibility(View.VISIBLE);
		}
		else { //service is off
			provider.setVisibility(View.INVISIBLE);
			providerResponse.setVisibility(View.INVISIBLE);
			textTo.setVisibility(View.INVISIBLE);
			image.setVisibility(View.INVISIBLE);
		}
		
	}
	
	public void setUI() {
		provider = (TextView)findViewById(R.id.provider);
		providerResponse = (TextView)findViewById(R.id.textProvider);
		textTo = (TextView)findViewById(R.id.textDistance);
		image = (ImageView)findViewById(R.id.image);
		isUISet = true;
	}
	
	
	 @Override
	  protected void onResume() {
	    super.onResume();
	    
	    connectToService();
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	    if (serviceConnection != null) {
	      unbindService(serviceConnection);
	      serviceConnection = null;
	    }
	  }
	
	  
	  @Override
	  protected void onDestroy() {
		  if(serviceConnection!=null)
			  unbindService(serviceConnection);
		  super.onDestroy();
	   
	  }
	  
	// Helper function for connecting to service
	  private void connectToService() {
	    // Calling startService() first prevents it from being killed on unbind()
	    //startService(new Intent(this, myService.class));

	    // Now connect to itmyService
	    serviceConnection = new myServiceConnection();

	    setUI();
	    
	    boolean result = bindService(
	      new Intent(this, myService.class),
	      serviceConnection,
	      BIND_AUTO_CREATE
	    );
	    if (!result) {
	        throw new RuntimeException("Unable to bind with service.");
	      }
	  }
	  

	  protected class myServiceConnection implements ServiceConnection {
	    @Override
	    public void onServiceConnected(ComponentName className, IBinder binder) {
	 
	      service = ((myService.LocalBinder) binder).getService();
	    }

	    @Override
	    public void onServiceDisconnected(ComponentName className) {
	      service = null;
	    }
	  }
	 
	  private myService service;

	  protected myServiceConnection serviceConnection;
	  private Context context;


}
