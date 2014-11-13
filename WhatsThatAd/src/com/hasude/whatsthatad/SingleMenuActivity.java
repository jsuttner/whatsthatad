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
import com.hasude.whatsthatad.sqlite.QuestionDB;

public class SingleMenuActivity extends FragmentActivity implements
LoaderCallbacks<Cursor>{
	
	private ViewPager viewPager;
	private SwipeAdapter swipeAdapter;
	public List<QuestionSinglePlayer> questionList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		
		// Initialize LoaderManager
		getLoaderManager().initLoader(0, null, this);
		
//		QuestionDeleteTask d = new QuestionDeleteTask();
//		d.execute();
//		
//		// Test Inserts
//		testInserts();

		viewPager = (ViewPager) findViewById(R.id.singlePager);
		swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), 3, this);
		viewPager.setAdapter(swipeAdapter);
	}

	private class QuestionInsertTask extends AsyncTask<ContentValues, Void, Void>{
		@Override
		protected Void doInBackground(ContentValues... contentValues) {    
            getContentResolver().insert(QuestionContentProvider.CONTENT_URI, contentValues[0]);			
			//Toast.makeText(getApplicationContext(), "Data has been inserted", Toast.LENGTH_SHORT).show();
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
	
	private void testInserts() {
		// Creating LocationInsertTask
		// Adidas - 1
		ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "adidas_censored" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "adidas_uncensored");
        contentValues.put(QuestionDB.FIELD_answer, "Adidas");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 1");
        QuestionInsertTask insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Nike - 2
		contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "nike" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "nike");
        contentValues.put(QuestionDB.FIELD_answer, "Nike");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 2");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Ikea - 3
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "ikea" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "ikea");
        contentValues.put(QuestionDB.FIELD_answer, "Ikea");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 3");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // McDonals_1 - 4
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mc_1" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mc_1");
        contentValues.put(QuestionDB.FIELD_answer, "Mc Donald's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 4");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // McDonals_2 - 5
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mc_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mc_2");
        contentValues.put(QuestionDB.FIELD_answer, "Mc Donald's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 5");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Heinz - 6
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "heinz" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "heinz");
        contentValues.put(QuestionDB.FIELD_answer, "Heinz");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 6");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Faber - 7
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "faber" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "faber");
        contentValues.put(QuestionDB.FIELD_answer, "Faber Castell");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 7");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Chupa Chups - 8
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "chupper" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "chupper");
        contentValues.put(QuestionDB.FIELD_answer, "Chupa Chups");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 8");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Snicker - 9
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "snicker" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "snicker");
        contentValues.put(QuestionDB.FIELD_answer, "Snicker");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 9");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Subway - 10
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "subway" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "subway");
        contentValues.put(QuestionDB.FIELD_answer, "Subway");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 10");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Davidhoff - 11
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "davidhoff" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "davidhoff");
        contentValues.put(QuestionDB.FIELD_answer, "Davidhoff");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 11");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Emporio Armani - 12
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "emporio" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "emporio");
        contentValues.put(QuestionDB.FIELD_answer, "Emporio Armani");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 12");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Mercedes - 13
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 13");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Gucci - 14
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "gucci" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "gucci");
        contentValues.put(QuestionDB.FIELD_answer, "Gucci");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 14");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Olympikus - 15
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "olympikus" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "olympikus");
        contentValues.put(QuestionDB.FIELD_answer, "Olympikus");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 15");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
    	// Old Spice - 16
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "old_spice" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "old_spice");
        contentValues.put(QuestionDB.FIELD_answer, "Old Spice");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 16");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Levis - 17
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "levis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "levis");
        contentValues.put(QuestionDB.FIELD_answer, "Levis");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 17");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Lexus - 18
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "lexus" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "lexus");
        contentValues.put(QuestionDB.FIELD_answer, "Lexus");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 18");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Mercedes_2 - 19
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes_2");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 19");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Canon - 20
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "canon" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "canon");
        contentValues.put(QuestionDB.FIELD_answer, "Canon");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 20");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Avis - 21
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "avis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "avis");
        contentValues.put(QuestionDB.FIELD_answer, "Avis");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 21");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // B&B Hotels - 22
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "bandb" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "bandb");
        contentValues.put(QuestionDB.FIELD_answer, "B&B Hotels");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 22");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Doritos - 23
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "doritos" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "doritos");
        contentValues.put(QuestionDB.FIELD_answer, "Doritos");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 23");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Lenor - 24
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "lenor" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "lenor");
        contentValues.put(QuestionDB.FIELD_answer, "Lenor");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 24");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Levi's - 25
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "levis" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "levis");
        contentValues.put(QuestionDB.FIELD_answer, "Levi's");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 25");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Mercedes - 26
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "mercedes_3" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "mercedes_3");
        contentValues.put(QuestionDB.FIELD_answer, "Mercedes");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 26");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Nissan - 27
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "nissan" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "nissan");
        contentValues.put(QuestionDB.FIELD_answer, "Nissan");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 27");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Panasonic - 28
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "panasonic" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "panasonic");
        contentValues.put(QuestionDB.FIELD_answer, "Panasonic");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 28");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Pepsi - 29
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "pepsi_2" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "pepsi_2");
        contentValues.put(QuestionDB.FIELD_answer, "Pepsi");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 29");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Skoda - 30
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "skoda" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "skoda");
        contentValues.put(QuestionDB.FIELD_answer, "Skoda");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 30");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Stiehl - 31
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "stiehl" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "stiehl");
        contentValues.put(QuestionDB.FIELD_answer, "Stiehl");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 31");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Vanish - 32
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "vanish" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "vanish");
        contentValues.put(QuestionDB.FIELD_answer, "Vanish");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 32");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Volvic - 33
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "volvic" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "volvic");
        contentValues.put(QuestionDB.FIELD_answer, "Volvic");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 33");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // VW - 34
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "vw" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "vw");
        contentValues.put(QuestionDB.FIELD_answer, "VW");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 34");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // Wikipedia - 35
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "wikipedia" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "wikipedia");
        contentValues.put(QuestionDB.FIELD_answer, "Wikipedia");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 35");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
        
        // WWF - 36
        contentValues = new ContentValues();
        contentValues.put(QuestionDB.FIELD_urlCensored, "wwf" );
        contentValues.put(QuestionDB.FIELD_urlUncensored, "wwf");
        contentValues.put(QuestionDB.FIELD_answer, "WWF");
        contentValues.put(QuestionDB.FIELD_question, "Which brand belongs to this advertisement?");
        contentValues.put(QuestionDB.FIELD_type, 0);
        Log.d("DB", "Inserting test data 36");
        insertTask = new QuestionInsertTask();
		insertTask.execute(contentValues);
	}
}