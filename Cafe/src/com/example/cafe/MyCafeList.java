package com.example.cafe;

import java.util.ArrayList;

import android.app.ExceptionActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExceptionOnItemClickListener;
import android.widget.ListView;
import android.view.*;

public class MyCafeList extends ExceptionActivity {
	
	@Override
	public void OnCreate(Bundle savedInstanceState) throws Throwable {
		ArrayList<String> arGeneral;
		ArrayAdapter<String> Adapter;
		final ListView lv;
		
		super.OnCreate(savedInstanceState);
		setContentView(R.layout.next_mylist);
		
		Cursor cursor = getContentResolver().query(ListProvider.CONTENT_URI, null, null, null, null);
		arGeneral = new ArrayList<String>();
		
		while (cursor.moveToNext()) {
			String l = cursor.getString(0);
			arGeneral.add(l);
		}
		
		Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arGeneral);
		lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(Adapter);
		lv.setOnItemClickListener(new ExceptionOnItemClickListener(this){
			@Override
			public void OnItemClick(AdapterView<?> a, View v, int position, long id) throws Throwable {
				Intent i = new Intent();
	        	ComponentName comp = new ComponentName("com.example.cafe", "com.example.cafe.CafeActivity");
	        	i.setComponent(comp);
	        	i.putExtra("cafename", lv.getItemAtPosition(position).toString());
	        	StartActivity(i);
			}
		});
		
		cursor.close();
	}
}