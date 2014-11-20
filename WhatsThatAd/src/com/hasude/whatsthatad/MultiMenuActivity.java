package com.hasude.whatsthatad;

import java.util.ArrayList;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;
import com.hasude.whatsthatad.gameobjects.GameMultiplayer;
import com.hasude.whatsthatad.gameobjects.QuestionMultiPlayer;
import com.hasude.whatsthatad.gameobjects.QuestionSinglePlayer;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MultiMenuActivity extends Activity implements
		LoaderCallbacks<Cursor> {

	EditText player1Edit;
	EditText player2Edit;
	private ArrayList<QuestionMultiPlayer> questionList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_menu);

		// get fields
		Button startBtn = (Button) findViewById(R.id.MultiplayerBtnStart);
		player1Edit = (EditText) findViewById(R.id.MultiEditPlayer1);
		player2Edit = (EditText) findViewById(R.id.MultiEditPlayer2);

		// set on click listener
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isStartPossible()) {
					// start multi player and tell the names
					QuestionMultiPlayer[] questions = new QuestionMultiPlayer[10];
					for(int i = 0;i < 10; i++){
						//TODO: Random
						questions[i] = questionList.get(i);
					}
					
					GameMultiplayer game = new GameMultiplayer(questions,
							player1Edit.getText().toString(), player2Edit
									.getText().toString());

					Intent i = new Intent(getApplicationContext(),
							MultiPlayerActivity.class);
					i.putExtra("game", game);

					startActivity(i);
				} else {
					// tell the user to fill both editables
					Toast.makeText(getApplicationContext(),
							"Please enter both names.", Toast.LENGTH_SHORT)
							.show();

				}
			}
		});

		questionList = null;

	}

	public boolean isStartPossible() {
		Log.d("WTA", player1Edit.getText().toString());
		if (player1Edit.getText().toString().equals("")
				|| player2Edit.getText().toString().equals("")
				|| questionList == null) {
			return false;
		} else
			return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// URI to the Content Provider locationsContentProvider
		Log.d("DB", "onCreateLoader aufgerufen");
		Uri contentUri = QuestionContentProvider.CONTENT_URI;
		return new CursorLoader(this, contentUri, null, "1", null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		Log.d("DB", "OnLoadFinished aufgerufen");

		questionList = new ArrayList<QuestionMultiPlayer>();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Try to create new SinglePlayerQuestion and add it to list
				try {
					String[] arr = { cursor.getString(5), cursor.getString(6),
							cursor.getString(7), cursor.getString(8) };
					try {
						questionList.add(new QuestionMultiPlayer(cursor
								.getInt(0), cursor.getString(1), cursor
								.getString(2), cursor.getString(3), arr));
					} catch (WrongNumberOfAnswersException e) {

					}
				} catch (CorrectAnswerException e) {
					e.printStackTrace();
				}
			} while (cursor.moveToNext());
		}
		// closing connection

		for (QuestionMultiPlayer q : questionList) {
			Log.d("DB", "ID: " + q.getAdCensored());
		}

		cursor.close();

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub

	}

}
