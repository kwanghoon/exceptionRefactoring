package com.nhn.android.maps;

import java.io.Serializable;

import android.app.Catch;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public class ExceptionNMapActivity extends NMapActivity {
	public final static String exception = "exception";
	public final static int RESULT_EXCEPTION = 99999;
	
	private Intent theIntent;
	private Catch catcher;
	
	public class UncaughtException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public Throwable t;
		public UncaughtException(Throwable t) { this.t = t; }
	}
	
	/// 
	final public void Throw(Throwable exn) {  // Issue: Should be public because ExceptionTextView may invoke this method!	
		Intent intent = new Intent();
		intent.putExtra(exception, exn);
			
		setResult(RESULT_EXCEPTION, intent);
		finish();
	}
	
	///
	final public void StartActivityForResult(Intent intent, int requestCode) {
		intent.putExtra(exception, (Serializable)null);
		this.catcher = null;
				
		super.startActivityForResult(intent, requestCode);
	}
	
	final public void StartActivity(Intent intent) {
		intent.putExtra(exception, (Serializable)null);
		this.catcher = null;
		
		super.startActivityForResult(intent, 0); // 0 is a dummy request code.
	}
	
	final public void Try_StartActivityForResult(Intent intent, int requestCode, Catch catcher) {
		intent.putExtra(exception, (Serializable)null);
		this.catcher = catcher;
		
		super.startActivityForResult(intent, requestCode);
	}
	
	final public void Try_StartActivity(Intent intent, Catch catcher) {
		intent.putExtra(exception, (Serializable)null);
		this.catcher = catcher;
		
		super.startActivityForResult(intent, 0); // 0 is a dummy request code.
	}
	
	///
	protected void Catch(Throwable t, int requestCode) throws Throwable {
		throw t;
	}

//	private void sendToTheCallerActivity(Throwable exn)  {
//		// 시작 액티비티가 아니면 (호출 액티비티가 있다면) 예외를 전파
//		if ( theIntent.hasExtra(exception) ) {
//			Throw( exn );
//		}
//		// 시작 액티비티이면 더 이상 예외를 전파시킬 대상이 없으므로 예외 발생 후 비정상 종료
//		else { // activityStackSize <= 0 
//			UncaughtException uncaught_exn = new UncaughtException(exn);
//			StackTraceElement[] stearr = exn.getStackTrace();
//			for (int i=0; i<stearr.length; i++) {
//				Log.e("ExceptionActivity", stearr[i].toString());
//			}
//			throw uncaught_exn;
//		}
//	}
	
	/// Activity Return
	@Override
	final protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_EXCEPTION) {
    		Throwable exn = (Throwable)data.getSerializableExtra(exception);
    		
    		// Add this object to the stack trace of the exception thrown
    		StackTraceElement elem = new StackTraceElement(this.getClass().toString(), "*unknown*", "*unknown*", 0);
    		StackTraceElement[] trace = exn.getStackTrace();
    		StackTraceElement[] newTrace = new StackTraceElement[trace.length+1];
    		
    		int i;
    		for(i=0; i<trace.length; i++)
    			newTrace[i] = trace[i];
    		newTrace[i] = elem;
    		
    		exn.setStackTrace(newTrace);
    		
    		// Catch
    		try {
//    			// catcher가 등록되어 있으면 handler를 그 다음에 호출
//    			// 등록되어 있지 않으면 Activity.Catch()를 호출
//    			if (catcher != null) {
//    				// catcher.handle()을 호출한 결과 true이면 예외처리가 되었고
//        			// false이면 Activity.Catch()를 호출
//    				if (catcher.handle(exn) == false) this.Catch(exn, requestCode);
//    				else /* 리턴값이 true이면 catcher가 예외를 처리함 */ ;
//    			}
//    			else
//    				this.Catch(exn, requestCode);
    			
    			// catcher가 등록되어 있지 않으면 Activity.Catch()를 호출
    			if (catcher == null) this.Catch(exn, requestCode);
    			else {
    				// 등록된 catcher.handler()에서 예외 처리를 못하면 Activity.Catch()를 호출
    				if ( ! catcher.handle(exn) ) this.Catch(exn, requestCode);
    			}
    				
    			// this.Catch(exn, requestCode);
    		} catch(Throwable unhandled_exn) {
    			//this.sendToTheCallerActivity(unhandled_exn);
    			
    			// 시작 액티비티가 아니면 (호출 액티비티가 있다면) 예외를 전파
    			if ( theIntent.hasExtra(exception) ) {
    				Throw( unhandled_exn );
    			}
    			// 시작 액티비티이면 더 이상 예외를 전파시킬 대상이 없으므로 예외 발생 후 비정상 종료
    			else { // activityStackSize <= 0 
    				UncaughtException uncaught_exn = new UncaughtException(unhandled_exn);
    				StackTraceElement[] stearr = unhandled_exn.getStackTrace();
    				for (i=0; i<stearr.length; i++) {
    					Log.e("ExceptionActivity", stearr[i].toString());
    				}
    				throw uncaught_exn;
    			}
    		}
    	}
    	else { // (resultCode == RESULT_OK) 
    		try {
    			this.OnActivityResult(requestCode, resultCode, data);
    		} catch(Throwable exn) {    
    			Throw ( exn );
    		}    		
    	}
	}
	
	protected void OnActivityResult(int requestCode, int resultCode, Intent data) throws Throwable {
	}
	
	/// Lifecycle of Activity
	@Override
	final protected void onCreate(Bundle savedInstanceState) {
		theIntent = getIntent();
		
		try {
			this.OnCreate(savedInstanceState);
		} catch(Throwable exn) {
			Throw( exn );
		}
	}
	
	protected void OnCreate(Bundle savedInstanceState)  throws Throwable {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	final protected void onStart() {
		try {
			this.OnStart();
		} catch(Throwable exn) {
			Throw( exn );
		}
	}
	
	protected void OnStart() throws Throwable {
		super.onStart();
	}
	
	@Override
	final protected void onResume() {
		try {
			this.OnResume();
		} catch(Throwable exn) {
			Throw( exn );
		}		
	}
	
	protected void OnResume() throws Throwable {
		super.onResume();
	}
	
	@Override
	final protected void onPause() {
		try {
			this.OnPause();
		} catch(Throwable exn) {
			Throw( exn );
		}		
	}
	
	protected void OnPause() throws Throwable {
		super.onPause();
	}	
	
	@Override
	final protected void onStop() {
		try {
			this.OnStop();
		} catch(Throwable exn) {
			Throw( exn );
		}		
	}
	
	protected void OnStop() throws Throwable {
		super.onStop();
	}
	
	@Override
	final protected void onDestroy() {
		try {
			this.OnDestroy();
		} catch(Throwable exn) {
			Throw( exn );
		}		
	}
	
	protected void OnDestroy() throws Throwable {
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			this.OnSaveInstanceState(outState);
		} catch(Throwable exn) {
			this.Throw(exn);
		}
	}
	
	protected void OnSaveInstanceState(Bundle outState) {
	}
	
	/// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			return this.OnCreateOptionsMenu(menu);
		} catch(Throwable exn) {
			Throw( exn );
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean OnCreateOptionsMenu(Menu menu) throws Throwable {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		try {
			return this.OnOptionsItemSelected(item);
		} catch(Throwable exn) {
			Throw( exn );
		}		
		return super.onOptionsItemSelected(item);
	}
	
	public boolean OnOptionsItemSelected(MenuItem item) throws Throwable {
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
		try {
			return this.OnPrepareOptionsMenu(menu);
		} catch(Throwable exn) {
			Throw( exn );
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	public boolean OnPrepareOptionsMenu(Menu menu)  throws Throwable  {
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		try {
			this.OnCreateContextMenu(menu, view, menuInfo);
		} catch(Throwable exn) {
			Throw( exn );
		}
	}
	
	public void OnCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) throws Throwable {
	}

	 @Override
	 public boolean onContextItemSelected(MenuItem item) {
		 try {
			 return this.OnContextItemSelected(item);
		 } catch(Throwable exn) {
			 Throw(exn);
		 }
		 return super.onContextItemSelected(item);
	 }
	 
	 public boolean OnContextItemSelected(MenuItem item)  throws Throwable  {
		 return super.onContextItemSelected(item);
	 }
	 
	 ///
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event){
		 try {
			 return this.OnKeyDown(keyCode, event);
		 } catch(Throwable exn) {
			 Throw(exn);
		 }
		 return super.onKeyDown(keyCode, event);
	 }
	 
	 public boolean OnKeyDown(int keyCode, KeyEvent event) throws Throwable {
		 return super.onKeyDown(keyCode, event);
	 }
}
