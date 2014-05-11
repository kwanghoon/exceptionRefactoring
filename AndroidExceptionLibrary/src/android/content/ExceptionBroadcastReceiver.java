package android.content;

import android.app.ExceptionActivity;
import android.widget.ExceptionAdapterView;

public abstract class ExceptionBroadcastReceiver extends BroadcastReceiver {
	private ExceptionActivity context;
	public ExceptionBroadcastReceiver(ExceptionActivity ea) {
		this.context = ea;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			this.OnReceive(context, intent);
		} catch(Throwable exn) {
			// Issue: ExceptionTextView.this.context instanceof ExceptionActivity is always true! 
			ExceptionActivity ea = this.context;
			ea.Throw( exn ); // Issue: one can throw exn because context *is* ExceptionActivity!
		}
	}
	
	public void OnReceive(Context context, Intent intent) throws Throwable {
	}
}
