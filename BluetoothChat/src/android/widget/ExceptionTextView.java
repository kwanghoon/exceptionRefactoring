package android.widget;

import android.annotation.SuppressLint;
import android.app.ExceptionActivity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class ExceptionTextView extends TextView {

	private ExceptionActivity context; // Issue: Context를 ExceptionActivity로 규정하면 onEditorAction 메써드에서 throw를 하지 못하는 문제를 해결할 수 있음
	
	public ExceptionTextView(ExceptionActivity context) { 
		super(context);
		this.context = context;
	}
	public ExceptionTextView(ExceptionActivity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public ExceptionTextView(ExceptionActivity context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	@SuppressLint("NewApi") // Issue: due to Version mismatch
	public static class ExceptionOnEditorActionListener implements TextView.OnEditorActionListener { // Issue: 원래는 interface이나 Adapter 클래스로 구현하고 있음. 이런 이유로 최소의 변경을 위해서는 static class 선언 필요.
		private ExceptionActivity context;
		
		public ExceptionOnEditorActionListener(ExceptionActivity context) {
			this.context = context;
		}
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			try {
				this.OnEditorAction(v, actionId, event);
			} catch(Throwable exn) {
				// Issue: ExceptionTextView.this.context instanceof ExceptionActivity is always true! 
				ExceptionActivity ea = this.context;
				ea.Throw( exn ); // Issue: one can throw exn because context *is* ExceptionActivity!
			}
			return false;
		}
		
		public boolean OnEditorAction(TextView v, int actionId, KeyEvent event) throws Throwable {
			return false;
		}
	}
}
