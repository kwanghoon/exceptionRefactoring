package com.example.cafe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ListHelper extends SQLiteOpenHelper{
	public static final String name = "texList";
	public static final String columnName1 = "tex";
	
	public ListHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + name
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ columnName1 + " TEXT);");
		//Log.i("SQLiteDatabaseActivity", "INSERT INTO " + name + " VALUES(NULL, 'hello')");
		//db.execSQL("INSERT INTO " + name + " VALUES(NULL, 'hello')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + name);
		onCreate(db);
	}
}