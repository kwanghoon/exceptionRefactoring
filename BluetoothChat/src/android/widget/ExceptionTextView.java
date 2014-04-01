package android.widget;

import android.annotation.SuppressLint;
import android.app.ExceptionActivity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class ExceptionTextView extends TextView {

	private ExceptionActivity context; // Issue: Context�� ExceptionActivity�� �����ϸ� onEditorAction �޽�忡�� throw�� ���� ���ϴ� ������ �ذ��� �� ����
	
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
	public static class ExceptionOnEditorActionListener implements TextView.OnEditorActionListener { // Issue: ������ interface�̳� Adapter Ŭ������ �����ϰ� ����. �̷� ������ �ּ��� ������ ���ؼ��� static class ���� �ʿ�.
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
