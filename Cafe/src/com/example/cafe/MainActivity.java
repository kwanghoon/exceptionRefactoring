package com.example.cafe;

import android.os.Bundle;
import android.app.ExceptionActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.ExceptionView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ExceptionActivity {

	@Override
	protected void OnCreate(Bundle savedInstanceState) throws Throwable {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new ExceptionView.ExceptionOnClickListener(this) {
			@Override
			public void OnClick(View v) {
	        	StartActivity(new Intent(MainActivity.this, MapActivity.class));
			}
		});
		
		final Button button2 = (Button)findViewById(R.id.button2);
		button2.setOnClickListener(new ExceptionView.ExceptionOnClickListener(this) {
			@Override
			public void OnClick(View v) {
				StartActivity(new Intent(MainActivity.this, MyCafeList.class));
			}
		});
	}

	@Override
	public boolean OnCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) throws Throwable {
		if ( event.getAction() == KeyEvent.ACTION_DOWN){
			if(keyCode == KeyEvent.KEYCODE_BACK){
				finish();
			}
		}
		return super.OnKeyDown(keyCode, event);
	}
	
	// K. Choi
	@Override
	protected void Catch(Throwable t, int requestCode) throws Throwable {
		if ( t instanceof ExnJumpTo) {
			ExnJumpTo exn = (ExnJumpTo)t;
			
			if (exn.getPackage().equals("") && exn.getClazz().equals(""))
				/* Jump to here, and so do nothing */ ;
			else {
				Intent i = new Intent();
				i.setComponent(new ComponentName(exn.getPackage(), exn.getClazz()));
				StartActivity(i);
				Log.v("MainActivity", "Package: " + exn.getPackage() + ", Class: " + exn.getClazz());
			}
		}
	}
}