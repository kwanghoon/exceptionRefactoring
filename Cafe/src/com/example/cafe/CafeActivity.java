package com.example.cafe;

import android.app.ExceptionActivity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ExceptionView.ExceptionOnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.TextView;

public class CafeActivity extends ExceptionActivity {

	ScrollView view;
	ImageView image;
	TextView name, addr, num, menu;
	RatingBar star;
	Button register, opinion, unregister;
	Intent i;
	float starAvg = 0; // ī���� ��պ���
	
	@Override
	public void OnCreate(Bundle savedInstanceState) throws Throwable {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.activity_cafe);
		
		i = getIntent();
		
		// ī���� ��պ��� �����ϱ�
		Cursor cursor = getContentResolver().query(OpinionProvider.CONTENT_URI, null, null, null, null);
		float result = 0;
		int count = 0;
		while (cursor.moveToNext()) {
			String cafe = cursor.getString(0);
	    	String star = cursor.getString(1);
	    	if(cafe.equals(i.getStringExtra("cafename"))){
	    		result += Float.parseFloat(star);
	    		count++;
	    	}
		}
		starAvg = result / count;
		
		
		name = (TextView)findViewById(R.id.textView1);
		name.setText(i.getStringExtra("cafename"));
		
		view = (ScrollView)findViewById(R.id.scrollView1);
		
		chooseCafe();
		
		num.setOnClickListener(new ExceptionOnClickListener(this){
			public void OnClick(View v) throws Throwable {
				Intent dial = new Intent(Intent.ACTION_CALL);
				dial.setData(Uri.parse("tel:" + num.getText()));
				startActivity(dial);
			}
		});
		
		register = (Button)findViewById(R.id.button1);
		register.setOnClickListener(new ExceptionOnClickListener(this){
			public void OnClick(View v) throws Throwable {
				Toast.makeText(CafeActivity.this, i.getStringExtra("cafename") + "��(��) ��ϵǾ����ϴ�", Toast.LENGTH_LONG).show();
				
				// ����Ʈ ���ι��̴��� ī���̸� �߰�
	        	ContentValues row = new ContentValues();
	        	row.put("tex", i.getStringExtra("cafename"));
	        	getContentResolver().insert(ListProvider.CONTENT_URI, row);
			}
		});
		
		unregister = (Button)findViewById(R.id.button3);
		unregister.setOnClickListener(new ExceptionOnClickListener(this){
			public void OnClick(View v) throws Throwable {
				Toast.makeText(CafeActivity.this, i.getStringExtra("cafename") + "��(��) ��ī�信�� �����Ǿ����ϴ�", Toast.LENGTH_LONG).show();
				
				// ����Ʈ ���ι��̴��� ī���̸� ��ġ�ϴ� ����Ʈ ����
				getContentResolver().delete(Uri.parse(ListProvider.CONTENT_URI + "/" + i.getStringExtra("cafename")), null, null);
			}
		});
		
		opinion = (Button)findViewById(R.id.button2);
		opinion.setOnClickListener(new ExceptionOnClickListener(this){
			public void OnClick(View v) throws Throwable {
				Intent in = new Intent();
	        	ComponentName comp = new ComponentName("com.example.cafe", "com.example.cafe.OpinionActivity");
	        	in.setComponent(comp);
	        	in.putExtra("cafename", name.getText());
	        	StartActivity(in);
			}
		});
	}
	
	public void chooseCafe(){
		image = (ImageView)findViewById(R.id.imageView1);
		addr = (TextView)findViewById(R.id.textView2);
		num = (TextView)findViewById(R.id.textView3);
		menu = (TextView)findViewById(R.id.textView4);
		star = (RatingBar)findViewById(R.id.ratingBar1);
		star.setStepSize((float)0.5);
		star.setIsIndicator(true);
		
		String sel = (String) name.getText();
		Log.v("name", sel);
		if(sel.equals("Grazie")){
			star.setRating(starAvg);
			image.setImageResource(R.drawable.grazie);
			addr.setText("������ ���ֽ� ����� ������ 234 �������б� ����ķ�۽� �����ö��� 1��");
			num.setText("000-000-0000");
			num.setTextColor(Color.parseColor("#FF8000"));
			menu.setText("�Ƹ޸�ī�� 3000��\nī��� 3500��\n");
		}
		else if(sel.equals("ī�亣��")){
			star.setRating(starAvg);
			image.setImageResource(R.drawable.beret);
			addr.setText("������ ���ֽ� ����� ������ 1924-4");
			num.setText("070-4101-5593");
			num.setTextColor(Color.parseColor("#FF8000"));
			menu.setText("�Ƹ޸�ī�� 2500��\n ī��� 3000��\n ī���ī 3000��\n");
		}
		else if(sel.equals("�ѿ��")){
			star.setRating(starAvg);
			image.setImageResource(R.drawable.ic_launcher);
			addr.setText("������ ���ֽ� ����� ������ 234 �������б� ����ķ�۽� �л�ȸ�� 1��");
			num.setText("070-4101-5593");
			num.setTextColor(Color.parseColor("#FF8000"));
			menu.setText("�Ƹ޸�ī�� 1500��\n ī��� 2000��\n ī���ī 2000��\n �ٴҶ�� 2000��\n ������ 2000��\n ī���� 2000��\n "
					+ "���̽� �Ƹ޸�ī�� 1500��\n ���̽� ī��� 2000��\n ���̽� ī���ī 2000��\n ���̽� �ٴҶ�� 2000��\n ���̽����� 2000��\n ���̽� ī���� 2000��\n ");
		}
		else if(sel.equals("Mazi")){
			star.setRating(starAvg);
			image.setImageResource(R.drawable.mazi);
			addr.setText("������ ���ֽ� ����� ������ 1209-3");
			num.setText("033-765-7667");
			num.setTextColor(Color.parseColor("#FF8000"));
			menu.setText("�Ƹ޸�ī�� 2500��\n ī��� 3000��\n ī���ī 3000��\n");	
		}
		
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) throws Throwable {
		if ( event.getAction() == KeyEvent.ACTION_DOWN){
			if(keyCode == KeyEvent.KEYCODE_BACK){
				// K. Choi
//				Intent in = new Intent();
//	        	ComponentName comp = new ComponentName("com.example.cafe", "com.example.cafe.MainActivity");
//	        	in.setComponent(comp);
//	        	StartActivity(in);
				// throw new ExnJumpTo("", ""); // Jump to MainActivity
				throw new ExnJumpTo("com.example.cafe", "com.example.cafe.MyCafeList"); // Jump to MyCafeList!
			}
		}
		return super.OnKeyDown(keyCode, event);
	}
}
