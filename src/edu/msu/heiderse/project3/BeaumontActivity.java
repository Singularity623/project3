package edu.msu.heiderse.project3;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BeaumontActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beaumont);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beaumont, menu);
		return true;
	}

}
