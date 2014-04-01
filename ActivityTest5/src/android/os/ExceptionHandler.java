package android.os;

import android.app.ExceptionActivity;

public class ExceptionHandler extends Handler {
	private ExceptionActivity ea;
	public ExceptionHandler(ExceptionActivity ea) {
		this.ea = ea;
	}
	@Override
    public void handleMessage(Message msg) {
		try {
			this.HandleMessage(msg);
		} catch(Throwable exn) {
			ea.Throw( exn ); // Issue: This makes us to introduce the private member field ea and the constructor with an ExceptionActivity argument!
		}
	}
	
	public void HandleMessage(Message msg) throws Throwable {
	}
}
