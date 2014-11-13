package com.hasude.whatsthatad;

import java.util.ArrayList;
import java.util.List;

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

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.gameobjects.QuestionSinglePlayer;

public class SingleMenuActivity extends FragmentActivity implements
LoaderCallbacks<Cursor>{
	
	ViewPager viewPager;
	SwipeAdapter swipeAdapter;
	List<QuestionSinglePlayer> questionList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		
		// Initialize LoaderManager
		getLoaderManager().initLoader(0, null, this);
		
		viewPager = (ViewPager) findViewById(R.id.singlePager);
		swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), 3, questionList);
		viewPager.setAdapter(swipeAdapter);
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
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		Log.d("DB", "OnLoadFinished aufgerufen");

		questionList = new ArrayList<QuestionSinglePlayer>();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Try to create new SinglePlayerQuestion and add it to list
				try {
					questionList.add(new QuestionSinglePlayer(cursor.getInt(0), cursor.getString(1), cursor
							.getString(2), cursor.getString(3), cursor.getString(4)));
				} catch (CorrectAnswerException e) {
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}
		// closing connection
		
		for(QuestionSinglePlayer q : questionList){
			Log.d("DB", "ID: " + q.getAdCensored());
		}
		
		cursor.close();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
	}
}