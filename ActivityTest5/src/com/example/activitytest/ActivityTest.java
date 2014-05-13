package com.example.activitytest;

import android.app.AlertDialog;
import android.app.ExceptionActivity;
import android.app.Catch;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;
import android.view.View;

public class ActivityTest extends ExceptionActivity 
							implements OnItemSelectedListener {
    /** Called when the activity is first created. */
	private static final int REQUEST_CODE_PARAM = 1;

    @Override
    protected void OnCreate(Bundle savedInstanceState) throws Throwable {
    	super.OnCreate(savedInstanceState); 
        setContentView(R.layout.main);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, operators);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner)findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(this);
    }
    
    private String[] operators = { "+", "-", "*", "/", 
    		"throw IndexOutOfBoundsException", "throw NullPointerException" };
    private int opIndex = 0;
    
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	opIndex = pos; 
    }
    public void onNothingSelected(AdapterView<?> parent) {
    	// do nothing
    }
    
    public void onClick(View v) {
    	try {
	    	TextView tv = (TextView)findViewById(R.id.editText1);
	    	String s = tv.getText().toString();
	    	int operand1 = Integer.parseInt(s);
	    	
	    	tv = (TextView)findViewById(R.id.editText2);
	    	s = tv.getText().toString();
	    	int operand2 = Integer.parseInt(s);
	    		
	    	Intent i = new Intent();
	    		
	    	ComponentName comp =
				new ComponentName("com.example.activitytest",
						"com.example.activitytest.NextActivity");
	    	i.setComponent(comp);
	    		
	    	Bundle b = new Bundle();
	    	b.putString("operator", operators[opIndex]);
	    	b.putInt("left operand", operand1);
	    	b.putInt("right operand", operand2);
	    		
	    	i.putExtras(b);
	    	
	    	Try_StartActivityForResult(i, REQUEST_CODE_PARAM, new Catch() {
	
				@Override
				public boolean handle(Throwable exn) throws Throwable {
		    		if (exn instanceof IndexOutOfBoundsException) {
		    			// 사용자가 지정한 예외 처리 코드: 로그 메시지 출력과 다이얼로그를 띄움
		    			Log.i("ActivityTest", "IndexOutOfBoundsException is raised...");
					
		    			AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTest.this);
		    			builder.setMessage("IndexOutOfBoundsException is raised...")
		    				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    						public void onClick(DialogInterface dialog, int id) {
		    						}
	    					});
	    				builder.create().show();
	    				return true;
		    		} 
		    		
					return false;
				}
	    		
	    	});
    	}
    	catch(Throwable exn) {
    		Throw (exn);
    	}
    }
    
    @Override
    protected void OnActivityResult(int requestCode, int resultCode, Intent data) {
   	
   		if (requestCode == REQUEST_CODE_PARAM) {
   			if (resultCode == RESULT_OK) {
   				int result = data.getIntExtra("result", 0);
   				TextView tv = (TextView)findViewById(R.id.textView1);
   				tv.setText("Result=" + result);
   			}
   		}
    	
    }
    
    protected void Catch(Throwable exn, int requestCode) throws Throwable {

    	if (requestCode == REQUEST_CODE_PARAM) {
    		if (exn instanceof NullPointerException) {
    			// 사용자가 지정한 예외 처리 코드: 로그 메시지 출력과 다이얼로그를 띄움
    			Log.i("ActivityTest", "NullPointerException is raised...");
			
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setMessage("NullPointerException is raised...")
    				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) {
    						}
    				});
    			builder.create().show();
			
    		} else {
    			throw exn; // rethrow it!
    			//super.catchExn(exn, requestCode, resultCode, data);
    		}
    	}
		
	}
    
}

