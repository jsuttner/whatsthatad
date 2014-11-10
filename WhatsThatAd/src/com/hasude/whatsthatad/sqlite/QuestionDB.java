package com.hasude.whatsthatad.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.gameobjects.QuestionSinglePlayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuestionDB extends SQLiteOpenHelper {
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "QuestionDB";
	// Labels table name
	public static final String TABLE_QUESTIONS = "questions";
	// Labels Table Columns names
	public static final String FIELD_id = "id";
	public static final String FIELD_urlCensored = "urlCensored";
	public static final String FIELD_urlUncensored= "urlUncensored";
	public static final String FIELD_answer = "answer";
	public static final String FIELD_question = "question";

	// Database
	private static SQLiteDatabase db;

	public QuestionDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		openDatabase(true);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Category table create query
		String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
				+ FIELD_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + FIELD_urlCensored
				+ " TEXT," + FIELD_urlUncensored + " TEXT," + FIELD_answer + " TEXT," +
				  FIELD_question + " TEXT)";
		db.execSQL(CREATE_CATEGORIES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
		// Create tables again
		onCreate(db);
	}

	void onDbDoesNotExist() {
		Log.d("DB", "Could not connect to db");
	}

	boolean openDatabase(boolean isWritable) {
		if (isWritable) {
			db = this.getWritableDatabase();
		} else {
			db = this.getReadableDatabase();
		}
		if (db == null) {
			onDbDoesNotExist();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Inserting new label into labels table
	 * */
	public long insertQuestion (ContentValues values) {

		long rowID = 0;
		
		if (openDatabase(true)) {
			// Inserting Row
			rowID = db.insert(TABLE_QUESTIONS, null, values);
			db.close(); // Closing database connection
		}
		return rowID;
	}

	/**
	 * Delete all labels
	 */
	public int deleteQuestions() {
		int rowsAffected = 0;	
		if (openDatabase(true)) {
			rowsAffected = db.delete(TABLE_QUESTIONS, null, null);
			db.close(); // Closing database connection
		}
		return rowsAffected;
	}

	/**
	 * Getting all labels returns list of labels
	 * */
	public List<QuestionSinglePlayer> getAllLabels() {
		List<QuestionSinglePlayer> labels = new ArrayList<QuestionSinglePlayer>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Try to create new SinglePlayerQuestion and add it to list
				try {
					labels.add(new QuestionSinglePlayer(cursor.getInt(0), cursor.getString(1), cursor
							.getString(2), cursor.getString(3), cursor.getString(4)));
				} catch (CorrectAnswerException e) {
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}
		// closing connection
		cursor.close();
		db.close();
		// returning labels
		return labels;
	}

	public Cursor getQuestions() {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor c = db.query(TABLE_QUESTIONS, 
						new String[] { FIELD_id,  FIELD_urlCensored , FIELD_urlUncensored, FIELD_answer, FIELD_question } , 
						null, null, null, null, null);
		
		db.close();
		
		return c;
	}

}
