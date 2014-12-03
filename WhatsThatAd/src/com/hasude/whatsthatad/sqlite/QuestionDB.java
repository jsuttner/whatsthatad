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
	public static final int DATABASE_VERSION = 1;
	// Database Name
	public static final String DATABASE_NAME = "QuestionDB";
	// Labels table name
	public static final String TABLE_QUESTIONS = "questions";
	// Labels Table Columns names
	public static final String FIELD_id = "id";
	public static final String FIELD_urlCensored = "urlCensored";
	public static final String FIELD_urlUncensored= "urlUncensored";
	public static final String FIELD_answer = "answer";
	public static final String FIELD_question = "question";
	public static final String FIELD_answer1 = "answer1";
	public static final String FIELD_answer2 = "answer2";
	public static final String FIELD_answer3 = "answer3";
	public static final String FIELD_answer4 = "answer4";
	public static final String FIELD_type = "type";
	public static final String FIELD_solved = "solved";

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
				  FIELD_question + " TEXT," + FIELD_answer1 + " TEXT," + FIELD_answer2 + " TEXT," + FIELD_answer3 + " TEXT,"
				  + FIELD_answer4 + " TEXT," + FIELD_type + " INTEGER,"
				  + FIELD_solved + " INTEGER"
				  + ")";
		db.execSQL(CREATE_CATEGORIES_TABLE);;
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

	SQLiteDatabase openDatabase(boolean isWritable) {
		if (isWritable) {
			db = this.getWritableDatabase();
		} else {
			db = this.getReadableDatabase();
		}
		if (db == null) {
			onDbDoesNotExist();
		}
		return db;
	}

	/**
	 * Inserting new label into labels table
	 * */
	public long insertQuestion (ContentValues values) {

		long rowID = 0;
		
		SQLiteDatabase db = openDatabase(true);
		if (db != null) {
			// Inserting Row
			rowID = db.insert(TABLE_QUESTIONS, null, values);
			//db.close(); // Closing database connection
		}
		return rowID;
	}

	/**
	 * Delete all questions
	 */
	public int deleteQuestions() {
		int rowsAffected = 0;	
		SQLiteDatabase db = openDatabase(true);
		if (db != null) {
			rowsAffected = db.delete(TABLE_QUESTIONS, null, null);
			//db.close(); // Closing database connection
		}
		return rowsAffected;
	}
	
	/**
	 * TODO: not needed right now
	 * */
	public List<QuestionSinglePlayer> getAllQuestions(boolean isSingle) {
		List<QuestionSinglePlayer> labels = new ArrayList<QuestionSinglePlayer>();
		// Select All Query
		String type = isSingle ? "0" : "1";
		String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + FIELD_type + " = " + type ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Try to create new SinglePlayerQuestion and add it to list
				try {
					labels.add(new QuestionSinglePlayer(cursor.getInt(0), cursor.getString(1), cursor
							.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(10)));
				} catch (CorrectAnswerException e) {
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}
		// returning labels
		return labels;
	}

	public Cursor getQuestions(String selection) {
		Log.d("DB", "GetQuestions called");
		
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + FIELD_type + " = " + selection, null);
		return c;
	}
	
	public int updateQuestion(int id){
		String strFilter = FIELD_id + "=" + id;
		ContentValues args = new ContentValues();
		args.put(FIELD_solved, "1");
		int effected = db.update(TABLE_QUESTIONS, args, strFilter, null);
		return effected;
	}

}
