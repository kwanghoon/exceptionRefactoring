package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.app.ExceptionActivity;

public abstract class ExceptionAdapterView extends AdapterView {
	ExceptionActivity context;
	public ExceptionAdapterView(ExceptionActivity context) {
		super(context);
		context = context;
	}
	public ExceptionAdapterView(ExceptionActivity context, AttributeSet attrs) {
		super(context);
		context = context;
	}
	public ExceptionAdapterView(ExceptionActivity context, AttributeSet attrs, int defStyle) {
		super(context);
		context = context;
	}
	
	public class ExceptionOnItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			try {
				this.OnItemClick(av, v, arg2, arg3);
			} catch(Throwable exn) {
				// Issue: ExceptionTextView.this.context instanceof ExceptionActivity is always true! 
				ExceptionActivity ea = ExceptionAdapterView.this.context;
				ea.Throw( exn ); // Issue: one can throw exn because context *is* ExceptionActivity!
			}
		}
		
		public void OnItemClick(AdapterView<?> av, View v, int arg2, long arg3) throws Throwable{
		}
		
	}
}
