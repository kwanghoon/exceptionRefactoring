package com.example.cafe;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class ListProvider extends ContentProvider {

	static final String CONTENT_URI_STR = "content://ListProvider/word";
	static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STR);
	static final int ALLWORD = 1;
	static final int ONEWORD = 2;
	static final UriMatcher Matcher;
	static {
		Matcher = new UriMatcher(UriMatcher.NO_MATCH);
		Matcher.addURI("ListProvider", "word", ALLWORD);
		Matcher.addURI("ListProvider", "word/*", ONEWORD);
	}

	SQLiteDatabase sqlDB;

	@Override
	public boolean onCreate() {
		ListHelper helper = new ListHelper(getContext(), null, null, 1);
		sqlDB = helper.getWritableDatabase();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		if (Matcher.match(uri) == ALLWORD)
			return "vnd.listcontentprovider.cursor.item/word";
		if (Matcher.match(uri) == ONEWORD)
			return "vnd.listcontentprovider.cursor.dir/word";
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String sql;

		sql = "SELECT " + ListHelper.columnName1 + " FROM " + ListHelper.name;

		if (Matcher.match(uri) == ONEWORD)
			sql += " WHERE " + ListHelper.columnName1 + " = '"
					+ uri.getPathSegments().get(1) + "'";

		Cursor cursor = sqlDB.rawQuery(sql, null);

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long row = sqlDB.insert(ListHelper.name, null, values);
		if (row > 0) {
			Uri notiuri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(notiuri, null);
			return notiuri;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (Matcher.match(uri)) {
		case ALLWORD:
			count = sqlDB.delete(ListHelper.name, selection, selectionArgs);
			break;
		case ONEWORD:
			String where;
			where = ListHelper.columnName1 + " = '"
				+ uri.getPathSegments().get(1) + "'";
			if (TextUtils.isEmpty(selection) == false)
				where += " AND " + selection;
			count = sqlDB.delete(ListHelper.name, where, selectionArgs);
			break;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (Matcher.match(uri)) {
		case ALLWORD:
			count = sqlDB.update(ListHelper.name, values, selection,
					selectionArgs);
			break;
		case ONEWORD:
			String where;
			where = ListHelper.columnName1 + " = '"
					+ uri.getPathSegments().get(1) + "'";
			if (TextUtils.isEmpty(selection) == false)
				where += " AND " + selection;
			count = sqlDB.update(ListHelper.name, values, where,
					selectionArgs);
			break;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}