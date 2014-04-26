package edu.msu.heiderse.project3;


import android.app.Activity;
import android.app.Fragment;
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


public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
		service= new myService();
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	
	public void StartService(View view)
	{
		Intent i = new Intent(context, myService.class);
		context.startService(i);
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
	
	// Helper function for connecting to service
	  private void connectToService() {
	    // Calling startService() first prevents it from being killed on unbind()
	    startService(new Intent(this, myService.class));

	    // Now connect to itmyService
	    serviceConnection = new myServiceConnection();

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
