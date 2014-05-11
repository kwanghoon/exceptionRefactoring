package android.widget;

import android.annotation.SuppressLint;
import android.app.ExceptionActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class ExceptionEditText extends EditText {
	ExceptionActivity context;
	
	public ExceptionEditText(ExceptionActivity context) {
		super(context);
		this.context = context;
	}
	public ExceptionEditText(ExceptionActivity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public ExceptionEditText(ExceptionActivity context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		try {
			this.OnDraw(canvas);
		} catch(Throwable exn) {
			context.Throw(exn);
		}
	}
	
	protected void OnDraw(Canvas canvas) throws Throwable {
		super.onDraw(canvas);
	}

}
