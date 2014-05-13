package com.example.activitystacktest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ThirdActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("ThirdActivity", "onCreate");
		setContentView(R.layout.activity_third);
	}
	
	public void onClick(View v) {
		Log.v("ThirdActivity", "onClick");
		Intent i = new Intent();
		i.putExtra("return value", "A string from ThirdActivity");
		setResult(RESULT_OK,i);
		finish();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("ThirdActivity", "onDestroy");
	}
}
