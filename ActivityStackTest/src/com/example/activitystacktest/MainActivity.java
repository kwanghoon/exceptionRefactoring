package com.example.activitystacktest;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
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
	
	public void onClick(View v) {
		Log.v("MainActivity", "onClick");
		Intent i = new Intent();

		//startActivity(i);
//		i.setClassName("com.example.activitystacktest", "com.example.activitystacktest.ThirdActivity");
//		startActivityForResult(i, 0);
		
		i.setClassName("com.example.activitystacktest", "com.example.activitystacktest.NextActivity");
		startActivityForResult(i, 0);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v("MainActivity", "onResume");
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent i) {
		Log.v("MainActivity", "onActivityResult");
		Log.v("MainActivity", "" + requestCode);
		Log.v("MainActivity", "" + resultCode);
		Log.v("MainActivity", "" + (i==null));
		if (i != null ) {
			String s = i.getStringExtra("return value");
			if ( s == null ) {
				Log.v("MainActivity", "null");
				TextView tv = (TextView)findViewById(R.id.textView1);
				tv.setText("Return: " + "null");
			}
			else {
				Log.v("MainActivity", s);
				TextView tv = (TextView)findViewById(R.id.textView1);
				tv.setText("Return: " + s);
			}
		}
		
		
	}

}
