package com.hasude.whatsthatad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.gameobjects.QuestionSinglePlayer;
import com.hasude.whatsthatad.sqlite.QuestionDB;

public class SingleMenuActivity extends FragmentActivity implements
LoaderCallbacks<Cursor>{
	
	private ViewPager viewPager;
	private SwipeAdapter swipeAdapter;
	public List<QuestionSinglePlayer> questionList;
	private Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		
		// If Question is solved Database will be updated
		i = getIntent();
		// get extras and update Database
		int questionID = (int) i.getIntExtra("question", 0);
		System.out.println("ID: "+ questionID);
		if(questionID != 0){
			ContentValues cV = new ContentValues();
			cV.put(QuestionDB.FIELD_id, questionID);
			cV.put(QuestionDB.FIELD_solved, true);
	        QuestionUpdateTask questionUpdateTask = new QuestionUpdateTask();
	        questionUpdateTask.execute(cV);
	        //System.out.println("Update durchgeführt");
		}
		
		// Initialize LoaderManager
		getLoaderManager().initLoader(0, null, this);
		
//		QuestionDeleteTask d = new QuestionDeleteTask();
//		d.execute();
	}

	// Task to add Question in database
	private class QuestionInsertTask extends AsyncTask<ContentValues, Void, Void>{
		boolean last = false;
		
		public QuestionInsertTask(boolean isLast) {
			super();
			last = isLast;
		}
		
		@Override
		protected Void doInBackground(ContentValues... contentValues) {    
            getContentResolver().insert(QuestionContentProvider.CONTENT_URI, contentValues[0]);			
			if(last) {
				finish();
				Intent i = new Intent(getApplicationContext(), SingleMenuActivity.class);
				startActivity(i);
			}
            return null;
		}		
	}
	
	// Task to delete Question in database
	private class QuestionDeleteTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
            getContentResolver().delete(QuestionContentProvider.CONTENT_URI, null, null);			
			return null;
		}		
	}
	
	// Task to update Question in database
	private class QuestionUpdateTask extends AsyncTask<ContentValues, Void, Void>{
		@Override
		protected Void doInBackground(ContentValues... contentValues) {
            getContentResolver().update(QuestionContentProvider.CONTENT_URI, contentValues[0], "", new String[0]);			
			return null;
		}		
	}

	// Get Uri for Loader<Curser>
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// URI to the Content Provider locationsContentProvider
		Log.d("DB", "onCreateLoader aufgerufen");
		Uri contentUri = QuestionContentProvider.CONTENT_URI;
		return new CursorLoader(this, contentUri, null, "0", null, null);
	}

	// Load all Singleplayer questions from database and provide it to activity
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		Log.d("DB", "OnLoadFinished aufgerufen");

		if(cursor.getCount() <= 0) {
			testInserts();
			return;
		}
		questionList = new ArrayList<QuestionSinglePlayer>();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			Log.d("DB", "ID: ");
			do {
				// Try to create new SinglePlayerQuestion and add it to list
				try {
					questionList.add(new QuestionSinglePlayer(cursor.getInt(0), cursor.getString(1), cursor
							.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(10)));
				} catch (CorrectAnswerException e) {
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}
		// closing connection
		
		for(QuestionSinglePlayer q : questionList){
			Log.d("DB", "ID: " + q.getSolved());
		}
		
		cursor.close();
		
		// Load Levelviews with data from Database
		loadPageViewer();
	}
	
	private boolean hasTableData(SQLiteDatabase db) {
		String mySQL= "SELECT count(*) as Total FROM " + QuestionDB.TABLE_QUESTIONS;
		Cursor c = db.rawQuery(mySQL, null);
		if(c.getCount() > 0) {
			
		}
		return false;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {	
	}
	
	/**
	 * Check if the database exist
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
	    File dbFile = context.getDatabasePath(dbName);
	    return dbFile.exists();
	}
	
	// Load Levelviews
	private void loadPageViewer() {
		// Swipe Views for Levels
		viewPager = (ViewPager) findViewById(R.id.singlePager);
		swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), 3, this);
		viewPager.setAdapter(swipeAdapter);
	}
	
	// Testdatasets for App
	private void testInserts() {
		// Creating LocationInsertTask
		// Adidas - 1
		ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "adidas_censored");
        contentValues.put(QuestionDB.FIELD_urlUncensored, "adidas_uncensored");
        contentValues.put(QuestionDB.FIELD_answer, "Adidas");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 1");
        QuestionInsertTask insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Nike - 2
		contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "nike" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "nike");
        contentValues.put(QuestionDB.FIELD_answer, "Nike");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 2");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Ikea - 3
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "ikea" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "ikea");
        contentValues.put(QuestionDB.FIELD_answer, "Ikea");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 3");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // McDonals_1 - 4
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mc_1" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mc_1");
        contentValues.put(QuestionDB.FIELD_answer, "Mc Donald's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 4");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // McDonals_2 - 5
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mc_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mc_2");
        contentValues.put(QuestionDB.FIELD_answer, "Mc Donald's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 5");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Heinz - 6
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "heinz" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "heinz");
        contentValues.put(QuestionDB.FIELD_answer, "Heinz");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 6");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Faber - 7
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "faber" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "faber");
        contentValues.put(QuestionDB.FIELD_answer, "Faber Castell");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 7");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Chupa Chups - 8
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "chupper" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "chupper");
        contentValues.put(QuestionDB.FIELD_answer, "Chupa Chups");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 8");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Snicker - 9
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "snickers" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "snickers");
        contentValues.put(QuestionDB.FIELD_answer, "Snicker");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 9");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Subway - 10
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "subway" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "subway");
        contentValues.put(QuestionDB.FIELD_answer, "Subway");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 10");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		
		// Davidhoff - 11
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "davidhoff" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "davidhoff");
        contentValues.put(QuestionDB.FIELD_answer, "Davidhoff");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 11");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Emporio Armani - 12
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "emporio" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "emporio");
        contentValues.put(QuestionDB.FIELD_answer, "Emporio Armani");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 12");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Mercedes - 13
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 13");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Gucci - 14
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "gucci" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "gucci");
        contentValues.put(QuestionDB.FIELD_answer, "Gucci");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 14");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Olympikus - 15
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "olympikus" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "olympikus");
        contentValues.put(QuestionDB.FIELD_answer, "Olympikus");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 15");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
    	// Old Spice - 16
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "old_spice" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "old_spice");
        contentValues.put(QuestionDB.FIELD_answer, "Old Spice");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 16");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Levis - 17
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "levis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "levis");
        contentValues.put(QuestionDB.FIELD_answer, "Levis");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 17");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Lexus - 18
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "lexus" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "lexus");
        contentValues.put(QuestionDB.FIELD_answer, "Lexus");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 18");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Mercedes_2 - 19
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes_2");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 19");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Canon - 20
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "canon" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "canon");
        contentValues.put(QuestionDB.FIELD_answer, "Canon");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 20");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Avis - 21
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "avis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "avis");
        contentValues.put(QuestionDB.FIELD_answer, "Avis");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 21");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // B&B Hotels - 22
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "bandb" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "bandb");
        contentValues.put(QuestionDB.FIELD_answer, "B&B Hotels");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 22");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Doritos - 23
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "doritos" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "doritos");
        contentValues.put(QuestionDB.FIELD_answer, "Doritos");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 23");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Lenor - 24
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "lenor" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "lenor");
        contentValues.put(QuestionDB.FIELD_answer, "Lenor");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 24");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Levi's - 25
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "levis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "levis");
        contentValues.put(QuestionDB.FIELD_answer, "Levi's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 25");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Mercedes - 26
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes_3" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes_3");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 26");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Nissan - 27
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "nissan" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "nissan");
        contentValues.put(QuestionDB.FIELD_answer, "Nissan");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 27");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Panasonic - 28
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "panasonic" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "panasonic");
        contentValues.put(QuestionDB.FIELD_answer, "Panasonic");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 28");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Pepsi - 29
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "pepsi_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "pepsi_2");
        contentValues.put(QuestionDB.FIELD_answer, "Pepsi");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 29");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Skoda - 30
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "skoda" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "skoda");
        contentValues.put(QuestionDB.FIELD_answer, "Skoda");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 30");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Stiehl - 31
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "stihl" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "stihl");
        contentValues.put(QuestionDB.FIELD_answer, "Stiehl");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 31");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Vanish - 32
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "vanish" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "vanish");
        contentValues.put(QuestionDB.FIELD_answer, "Vanish");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 32");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Volvic - 33
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "volvic" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "volvic");
        contentValues.put(QuestionDB.FIELD_answer, "Volvic");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 33");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // VW - 34
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "vw" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "vw");
        contentValues.put(QuestionDB.FIELD_answer, "VW");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 34");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Wikipedia - 35
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "wikipedia" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "wikipedia");
        contentValues.put(QuestionDB.FIELD_answer, "Wikipedia");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 35");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // WWF - 36
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "wwf" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "wwf");
        contentValues.put(QuestionDB.FIELD_answer, "WWF");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 0);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 36");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        
		
		// Multiplayer
		// Adidas - 1
		contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "adidas_censored" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "adidas_uncensored");
        contentValues.put(QuestionDB.FIELD_answer, "Adidas");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Nike");
        contentValues.put(QuestionDB.FIELD_answer2, "K-Swiss");
        contentValues.put(QuestionDB.FIELD_answer3, "Adidas");
        contentValues.put(QuestionDB.FIELD_answer4, "Puma");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 1");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
        // Nike - 2
		contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "nike" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "nike");
        contentValues.put(QuestionDB.FIELD_answer, "Nike");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Puma");
        contentValues.put(QuestionDB.FIELD_answer2, "Nike");
        contentValues.put(QuestionDB.FIELD_answer3, "Spalding");
        contentValues.put(QuestionDB.FIELD_answer4, "Kickz");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 2");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
	    // Ikea - 3
	    contentValues = new ContentValues();
	    contentValues.put(QuestionDB.FIELD_urlCensored, "ikea" );
	    contentValues.put(QuestionDB.FIELD_urlUncensored, "ikea");
	    contentValues.put(QuestionDB.FIELD_answer, "Ikea");
	    contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
	    contentValues.put(QuestionDB.FIELD_answer1, "Vans");
        contentValues.put(QuestionDB.FIELD_answer2, "Walmart");
        contentValues.put(QuestionDB.FIELD_answer3, "Target");
        contentValues.put(QuestionDB.FIELD_answer4, "Ikea");
	    contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
	    Log.d("DB", "Inserting test data 3");
	    insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
        // McDonals_1 - 4
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mc_1" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mc_1");
        contentValues.put(QuestionDB.FIELD_answer, "Mc Donald's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Burger King");
        contentValues.put(QuestionDB.FIELD_answer2, "In'n Out Burger");
        contentValues.put(QuestionDB.FIELD_answer3, "Mc Donald's");
        contentValues.put(QuestionDB.FIELD_answer4, "Jack in the Box");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 4");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
        // McDonals_2 - 5
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mc_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mc_2");
        contentValues.put(QuestionDB.FIELD_answer, "Mc Donald's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Trader Joes");
        contentValues.put(QuestionDB.FIELD_answer2, "Subway");
        contentValues.put(QuestionDB.FIELD_answer3, "Panda Express");
        contentValues.put(QuestionDB.FIELD_answer4, "Mc Donald's");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 5");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
        // Heinz - 6
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "heinz" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "heinz");
        contentValues.put(QuestionDB.FIELD_answer, "Heinz");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Heinz");
        contentValues.put(QuestionDB.FIELD_answer2, "Hooters");
        contentValues.put(QuestionDB.FIELD_answer3, "Jim Block");
        contentValues.put(QuestionDB.FIELD_answer4, "Subway");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 6");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
        // Faber - 7
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "faber" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "faber");
        contentValues.put(QuestionDB.FIELD_answer, "Faber Castell");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Ikea");
        contentValues.put(QuestionDB.FIELD_answer2, "Faber Castell");
        contentValues.put(QuestionDB.FIELD_answer3, "Fire Department");
        contentValues.put(QuestionDB.FIELD_answer4, "Hot Wheels");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 7");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
        // Chupa Chups - 8
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "chupper" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "chupper");
        contentValues.put(QuestionDB.FIELD_answer, "Chupa Chups");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Obi");
        contentValues.put(QuestionDB.FIELD_answer2, "Street Cleaners");
        contentValues.put(QuestionDB.FIELD_answer3, "Chupa Chups");
        contentValues.put(QuestionDB.FIELD_answer4, "LA Zoo");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 8");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
        // Snicker - 9
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "snicker" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "snicker");
        contentValues.put(QuestionDB.FIELD_answer, "Snickers");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "ESPN");
        contentValues.put(QuestionDB.FIELD_answer2, "Snickers");
        contentValues.put(QuestionDB.FIELD_answer3, "Everlast");
        contentValues.put(QuestionDB.FIELD_answer4, "NBATV");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 9");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		        
        // Subway - 10
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "subway" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "subway");
        contentValues.put(QuestionDB.FIELD_answer, "Subway");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Ralph's");
        contentValues.put(QuestionDB.FIELD_answer2, "Vons");
        contentValues.put(QuestionDB.FIELD_answer3, "Trader Joes");
        contentValues.put(QuestionDB.FIELD_answer4, "Subway");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 10");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
		
		// Davidhoff - 11
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "davidhoff" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "davidhoff");
        contentValues.put(QuestionDB.FIELD_answer, "Davidhoff");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Davidhoff");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 11");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Emporio Armani - 12
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "emporio" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "emporio");
        contentValues.put(QuestionDB.FIELD_answer, "Emporio Armani");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Emporio Armani");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 12");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Mercedes - 13
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Mercedes");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 13");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Gucci - 14
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "gucci" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "gucci");
        contentValues.put(QuestionDB.FIELD_answer, "Gucci");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Gucci");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 14");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Olympikus - 15
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "olympikus" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "olympikus");
        contentValues.put(QuestionDB.FIELD_answer, "Olympikus");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Olympikus");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 15");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
    	// Old Spice - 16
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "old_spice" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "old_spice");
        contentValues.put(QuestionDB.FIELD_answer, "Old Spice");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Old Spice");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 16");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Levis - 17
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "levis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "levis");
        contentValues.put(QuestionDB.FIELD_answer, "Levis");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Levis");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 17");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Lexus - 18
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "lexus" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "lexus");
        contentValues.put(QuestionDB.FIELD_answer, "Lexus");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Lexus");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 18");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Mercedes_2 - 19
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes_2");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Mercedes");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 19");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Canon - 20
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "canon" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "canon");
        contentValues.put(QuestionDB.FIELD_answer, "Canon");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Canon");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 20");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Avis - 21
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "avis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "avis");
        contentValues.put(QuestionDB.FIELD_answer, "Avis");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Avis");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 21");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // B&B Hotels - 22
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "bandb" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "bandb");
        contentValues.put(QuestionDB.FIELD_answer, "B&B Hotels");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "B&B Hotels");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 22");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Doritos - 23
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "doritos" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "doritos");
        contentValues.put(QuestionDB.FIELD_answer, "Doritos");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Doritos");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 23");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Lenor - 24
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "lenor" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "lenor");
        contentValues.put(QuestionDB.FIELD_answer, "Lenor");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Lenor");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 24");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Levi's - 25
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "levis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "levis");
        contentValues.put(QuestionDB.FIELD_answer, "Levi's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Levi's");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 25");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Mercedes - 26
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes_3" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes_3");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Mercedes");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 26");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Nissan - 27
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "nissan" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "nissan");
        contentValues.put(QuestionDB.FIELD_answer, "Nissan");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Nissan");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 27");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Panasonic - 28
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "panasonic" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "panasonic");
        contentValues.put(QuestionDB.FIELD_answer, "Panasonic");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Panasonic");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 28");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Pepsi - 29
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "pepsi_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "pepsi_2");
        contentValues.put(QuestionDB.FIELD_answer, "Pepsi");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Pepsi");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 29");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Skoda - 30
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "skoda" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "skoda");
        contentValues.put(QuestionDB.FIELD_answer, "Skoda");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Skoda");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 30");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Stiehl - 31
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "stiehl" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "stiehl");
        contentValues.put(QuestionDB.FIELD_answer, "Stiehl");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Stiehl");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 31");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Vanish - 32
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "vanish" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "vanish");
        contentValues.put(QuestionDB.FIELD_answer, "Vanish");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Vanish");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 32");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Volvic - 33
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "volvic" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "volvic");
        contentValues.put(QuestionDB.FIELD_answer, "Volvic");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Volvic");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 33");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // VW - 34
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "vw" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "vw");
        contentValues.put(QuestionDB.FIELD_answer, "VW");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "VW");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 34");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // Wikipedia - 35
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "wikipedia" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "wikipedia");
        contentValues.put(QuestionDB.FIELD_answer, "Wikipedia");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "Wikipedia");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 35");
        insertTask = new QuestionInsertTask(false);
		insertTask.execute(contentValues);
        
        // WWF - 36
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "wwf" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "wwf");
        contentValues.put(QuestionDB.FIELD_answer, "WWF");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_answer1, "WWF");
        contentValues.put(QuestionDB.FIELD_answer2, "");
        contentValues.put(QuestionDB.FIELD_answer3, "");
        contentValues.put(QuestionDB.FIELD_answer4, "");
        contentValues.put(QuestionDB.FIELD_type, 1);
        contentValues.put(QuestionDB.FIELD_solved, 0);
        Log.d("DB", "Inserting test data 36");
        insertTask = new QuestionInsertTask(true);
		insertTask.execute(contentValues);
	}
}