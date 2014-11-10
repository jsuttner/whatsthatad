package com.hasude.whatsthatad;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;
import com.hasude.whatsthatad.gameobjects.QuestionMultiPlayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MultiPlayerActivity extends Activity {

	public static final String DEBUG_TAG = "Multiplayer";

	private QuestionMultiPlayer question;

	// UI
	private ImageView image;

	private Button[] pBtns = new Button[2];

	private static final int[] P_COLORS = new int[] { Color.argb(255, 98, 196, 98), Color.BLUE };

	private Button[] answers;

	// Listeners
	private OnClickListener playerBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			MultiPlayerActivity.this.playerRespond(v.getId());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mutli_player);

		// get buttons
		image = (ImageView) findViewById(R.id.MultiImageView);

		pBtns[0] = (Button) findViewById(R.id.MultiBtnPlayer1);
		pBtns[1] = (Button) findViewById(R.id.MultiBtnPlayer2);

		answers = new Button[4];
		answers[0] = (Button) findViewById(R.id.MultiBtnAnswer1);
		answers[1] = (Button) findViewById(R.id.MultiBtnAnswer2);
		answers[2] = (Button) findViewById(R.id.MultiBtnAnswer3);
		answers[3] = (Button) findViewById(R.id.MultiBtnAnswer4);

		// handlers for buttons
		pBtns[0].setOnClickListener(playerBtnListener);
		pBtns[1].setOnClickListener(playerBtnListener);

		question = getQuestion();

		newQuestion(question);

	}

	private QuestionMultiPlayer getQuestion() {
		String censored = null;
		String uncensored = null;
		
		try {
			QuestionMultiPlayer q = new QuestionMultiPlayer(0, censored,
					uncensored, "Adidas", new String[] { "Nike", "Adidas",
							"Puma", "Reebock" });
			return q;
		} catch (CorrectAnswerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongNumberOfAnswersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void playerRespond(int playerId) {

		for (Button b : pBtns)
			b.setOnClickListener(null);

		int p = 3;
		for (int i = 0; i < 2; i++) {
			if (playerId == pBtns[i].getId()) {
				Log.d(DEBUG_TAG, "Button Spieler " + i);
				p = i;
			}
		}
		
		final int player = p;
		OnClickListener answerOptionsListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				answerGiven(player, (String) b.getText());
			}
		};
		for (Button b : answers) {
			b.setBackgroundColor(P_COLORS[player]);
			b.setOnClickListener(answerOptionsListener);
		}

	}

	protected void answerGiven(int player, String answer) {
		if(question.isAnswerCorrect(answer)){
			Toast.makeText(getApplicationContext(), "Korrekte Antwort", Toast.LENGTH_LONG);
			
		} else{
			Toast.makeText(getApplicationContext(), "Falsche Antwort", Toast.LENGTH_LONG);
		}
		
		image.setImageURI(question.getAdUncensoredAsUri());
		
	}

	protected void newQuestion(QuestionMultiPlayer q) {
		image.setImageURI(q.getAdCensoredAsUri());
		for (int i = 0; i < 4; i++) {
			answers[i].setText(q.getAnswer(i));
		}
		for (int i = 0; i < 2; i++)
			pBtns[i].setBackgroundColor(P_COLORS[i]);
	}
}
