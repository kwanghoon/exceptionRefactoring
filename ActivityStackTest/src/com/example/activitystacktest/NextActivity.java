package com.example.activitystacktest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NextActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_next);
	}
	
	public void onClick(View v) {
		Log.v("NextActivity", "onClick");
		
		Intent i = new Intent();
		i.setClassName("com.example.activitystacktest", "com.example.activitystacktest.ThirdActivity");
		startActivityForResult(i, 0);
		startActivity(i);

		Intent j = new Intent();
		j.putExtra("return value", "A string from NextActivity");
		setResult(RESULT_OK,j);
		
//		j.putExtra("return value", "A string from NextActivity (snd)");
//		setResult(RESULT_OK,j);
		
		finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v("NextActivity", "onResume");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent i) {
		Log.v("NextActivity", "onActivityResult");
		Log.v("NextActivity", "" + requestCode);
		Log.v("NextActivity", "" + resultCode);
		Log.v("NextActivity", "" + (i==null));
		if (i != null) {
			String s = i.getStringExtra("return value");
			if ( s == null ) {
				Log.v("NextActivity", "null");
				TextView tv = (TextView)findViewById(R.id.textView1);
				tv.setText("Return: " + "null");
			}
			else {
				Log.v("NextActivity", s);
				TextView tv = (TextView)findViewById(R.id.textView1);
				tv.setText("Return: " + s);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("NextActivity", "onDestroy");
	}
}
