package android.view;

import android.app.ExceptionActivity;
import android.util.AttributeSet;

public class ExceptionView extends View {
	private ExceptionActivity context; 
	
	public ExceptionView(ExceptionActivity context) {
		super(context);
		this.context = context;
	}
	public ExceptionView(ExceptionActivity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public ExceptionView(ExceptionActivity context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public static class ExceptionOnClickListener implements View.OnClickListener {
		private ExceptionActivity context;
		public ExceptionOnClickListener(ExceptionActivity context) {
			this.context = context;
		}
		@Override
		public void onClick(View v) {
			try {
				this.OnClick(v);
			} catch(Throwable exn) {
				ExceptionActivity ea = this.context;
				ea.Throw( exn ); 
			}
		}
		public void OnClick(View v) throws Throwable {
		}
	}
	
}
