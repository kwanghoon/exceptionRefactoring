package com.example.activitytest;

import android.app.ExceptionActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NextActivity extends ExceptionActivity {
	@Override
	protected void OnCreate(Bundle savedInstanceState) throws Throwable {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.nextmain);
	}
	
	public void onClick(View v) {
		try {
			Intent i = getIntent();
			Bundle b = i.getExtras();
			String op = b.getString("operator");
				
			int operand1=0, operand2=0, result=0;
			operand1 = b.getInt("left operand");
			operand2 = b.getInt("right operand");
						
			if (op.compareTo("+") == 0)
				result = operand1 + operand2;
			else if (op.compareTo("-") == 0)
				result = operand1 - operand2;
			else if (op.compareTo("*") == 0)
				result = operand1 * operand2;
			else if(op.compareTo("/") == 0)
				result = operand1 / operand2;
			else if (op.compareTo("throw IndexOutOfBoundsException") == 0) {
				throw new IndexOutOfBoundsException();
			}
			else if (op.compareTo("throw NullPointerException") == 0) {
				throw new NullPointerException();
			}
			else
				/* do nothing */ 
				;
					
			Intent result_i = new Intent();
				
			result_i.putExtra("result", result);
			setResult(RESULT_OK, result_i);
			
			finish();
		}
		catch(Throwable exn) {
			Throw(exn);
		}
		
	}
}