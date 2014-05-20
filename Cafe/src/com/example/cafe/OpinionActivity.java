package com.example.cafe;

import java.util.ArrayList;

import android.app.ExceptionActivity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ExceptionView.ExceptionOnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

// 등록버튼 누르면 리스트뷰 업데이트

public class OpinionActivity extends ExceptionActivity {

	float starPoint;

	Intent i;
	
	Button opiWrite;
	
	// 다이얼로그 안에서 쓰이는 변수
	RatingBar point;
	EditText opi;
	Button button;
		
	@Override
	public void OnCreate(Bundle savedInstanceState) throws Throwable {
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.activity_opinion);
		
		i = getIntent();
		
		/* 리스트뷰 */
		ArrayList<String> arGeneral;
		ArrayAdapter<String> Adapter;
		final ListView lv;
		
		Cursor cursor = getContentResolver().query(OpinionProvider.CONTENT_URI, null, null, null, null);
		arGeneral = new ArrayList<String>();
		String result = "";
		
		while (cursor.moveToNext()) {
			String cafe = cursor.getString(0);
	    	String star = cursor.getString(1);
	    	String opi = cursor.getString(2);
	    	if(cafe.equals(i.getStringExtra("cafename"))){

		    	result = ("별점 : " + star + "/ 한줄의견 : " + opi + "\n");
				arGeneral.add(result);
	    	}
		}
		
		Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arGeneral);
		lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(Adapter);
	    cursor.close();
		
	    /* 등록버튼, 커스텀다이얼로그 창 */
		opiWrite = (Button)findViewById(R.id.button1);
		
		opiWrite.setOnClickListener(new ExceptionOnClickListener(this){
			public void OnClick(View v){
				// CustomDialog
				Context mContext = OpinionActivity.this;
				AlertDialog.Builder builder;
				final AlertDialog dialog;
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.dialog_opinion, (ViewGroup)findViewById(R.id.dialog_layout));
				
				// 별점입력
				point = (RatingBar)layout.findViewById(R.id.ratingBar1);
				point.setStepSize((float)0.5);
				point.setIsIndicator(false);
				point.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){
					@Override
					public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
						starPoint = rating;
					}
				});
				// 의견입력
				opi = (EditText)layout.findViewById(R.id.editText1);
				
				// dialog 생성
				builder = new AlertDialog.Builder(mContext);
				builder.setView(layout);
				dialog = builder.create();
				dialog.setTitle("의견등록");
				
				// 등록버튼
				button = (Button)layout.findViewById(R.id.button1);
				button.setOnClickListener(new ExceptionOnClickListener(OpinionActivity.this){
					@Override
					public void OnClick(View v) {
						Toast.makeText(OpinionActivity.this, "의견이 등록되었습니다.", Toast.LENGTH_LONG).show();

						// 컨텐트 프로바이더에 추가
			        	ContentValues row = new ContentValues();
			        	row.put("cafe", i.getStringExtra("cafename"));
			        	row.put("star", Float.toString(starPoint));
			        	row.put("opinion", opi.getText().toString());
			        	getContentResolver().insert(OpinionProvider.CONTENT_URI, row);
			        	
						dialog.dismiss();
						// K. Choi
//						Intent in = new Intent();
//			        	ComponentName comp = new ComponentName("com.example.cafe", "com.example.cafe.OpinionActivity");
//			        	in.setComponent(comp);
//			        	in.putExtra("cafename", i.getStringExtra("cafename"));
//			        	StartActivity(in);
					}
				});
				
				// dialog 띄우기
				dialog.show();
			}
		});
	}
	
	@Override
	public boolean OnKeyDown(int keyCode, KeyEvent event) throws Throwable {
		if ( event.getAction() == KeyEvent.ACTION_DOWN){
			if(keyCode == KeyEvent.KEYCODE_BACK){
				Intent in = new Intent();
	        	ComponentName comp = new ComponentName("com.example.cafe", "com.example.cafe.CafeActivity");
	        	in.setComponent(comp);
	        	in.putExtra("cafename", i.getStringExtra("cafename"));
	        	// K. Choi
	        	//StartActivity(in);
	        	finish();
			}
		}
		return super.OnKeyDown(keyCode, event);
	}

}
