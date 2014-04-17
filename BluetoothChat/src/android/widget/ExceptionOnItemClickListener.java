package android.widget;

import android.app.ExceptionActivity;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class ExceptionOnItemClickListener implements OnItemClickListener {

	ExceptionActivity context;
	public ExceptionOnItemClickListener(ExceptionActivity context) {
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
		try {
			this.OnItemClick(av, v, arg2, arg3);
		} catch(Throwable exn) {
			// Issue: ExceptionTextView.this.context instanceof ExceptionActivity is always true! 
			context.Throw( exn ); // Issue: one can throw exn because context *is* ExceptionActivity!
		}
	}
	
	public void OnItemClick(AdapterView<?> av, View v, int arg2, long arg3) throws Throwable{
	}

}
