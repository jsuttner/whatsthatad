package com.hasude.whatsthatad;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.hasude.whatsthatad.sqlite.QuestionDB;

public class SingleMenuActivity extends FragmentActivity implements
LoaderCallbacks<Cursor>{
	
	ViewPager viewPager;
	SwipeAdapter swipeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		
		viewPager = (ViewPager) findViewById(R.id.singlePager);
		swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), 3, viewPager);
		viewPager.setAdapter(swipeAdapter);
		
		// Initialize LoaderManager
		getLoaderManager().initLoader(0, null, this);
		
		// Test Inserts
		testInserts();
	}
	
	private void testInserts() {
		// Creating LocationInsertTask
		ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "../test/testCensored.png" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "../test/testUncensored.png");
        contentValues.put(QuestionDB.FIELD_answer, "testanswer");
        contentValues.put(QuestionDB.FIELD_question, "testquestion?");
        
		QuestionInsertTask insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
		
	}

	private class QuestionInsertTask extends AsyncTask<ContentValues, Void, Void>{
		@Override
		protected Void doInBackground(ContentValues... contentValues) {    
            getContentResolver().insert(QuestionContentProvider.CONTENT_URI, contentValues[0]);			
			return null;
		}		
	}
	
	private class QuestionDeleteTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
            getContentResolver().delete(QuestionContentProvider.CONTENT_URI, null, null);			
			return null;
		}		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// URI to the Content Provider locationsContentProvider
		Log.d("DB", "onCreateLoader aufgerufen");
		Uri contentUri = QuestionContentProvider.CONTENT_URI;
		return new CursorLoader(this, contentUri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		Log.d("DB", "OnLoadFinished aufgerufen");
		int questionCount = 0;

		// Number of locations available in the SQLite database table
		if (arg1 != null) {
			Log.d("DB", "Args != null");
			questionCount = arg1.getCount();
			// Move the current record pointer to the first row of the table
			arg1.moveToFirst();
		} else {
			questionCount = 0;
		}
		for (int i = 0; i < questionCount; i++) {

			// Do stuff
			System.out.println("Question " + arg1.getString(3));
			
			arg1.moveToNext();
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
	}
}