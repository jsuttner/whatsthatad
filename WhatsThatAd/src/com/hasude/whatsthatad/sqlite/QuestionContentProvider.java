package com.hasude.whatsthatad.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class QuestionContentProvider extends ContentProvider {

	// Labels table names";
	
	public static final String AUTHORITY = "com.hasude.whatsthatad.QuestionContentProvider";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + QuestionDB.TABLE_QUESTIONS);	
	private static final UriMatcher sUriMatcher;
	
	private static final int LOCATIONS = 1;	
	
	private QuestionDB locationsDB;
	
	static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, QuestionDB.TABLE_QUESTIONS, 1);
    }
	
	@Override
	public boolean onCreate() {
		Log.d("DB", "ContentProvider created");
		locationsDB = new QuestionDB(getContext());
		return true;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return locationsDB.deleteLocations();
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {

		// get values
		if (arg1 != null) {
			locationsDB.insertLabel(arg1);
		}

		return null;
	}

	@Override
	public Cursor query(Uri arg0, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		Log.d("DB", "Query called");
		
        if(sUriMatcher.match(arg0) == LOCATIONS) {
        	return locationsDB.getLocations();
        } else {
        	throw new IllegalArgumentException("Unknown URI " + arg0);
        }
    }

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		return 0;
	}
}
