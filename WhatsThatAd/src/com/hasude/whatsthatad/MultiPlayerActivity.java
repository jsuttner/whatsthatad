package com.hasude.whatsthatad;

import com.hasude.whatsthatad.gameobjects.QuestionMultiPlayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MultiPlayerActivity extends Activity {

	public static final String DEBUG_TAG = "Multiplayer";

	private ImageView image;

	private Button player1;
	private Button player2;
	
	private Color colP1;
	private Color colP2;

	private Button[] answers;

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

		player1 = (Button) findViewById(R.id.MultiBtnPlayer1);
		player2 = (Button) findViewById(R.id.MultiBtnPlayer2);

		answers = new Button[4];
		answers[0] = (Button) findViewById(R.id.MultiBtnAnswer1);
		answers[1] = (Button) findViewById(R.id.MultiBtnAnswer2);
		answers[2] = (Button) findViewById(R.id.MultiBtnAnswer3);
		answers[3] = (Button) findViewById(R.id.MultiBtnAnswer4);

		// handlers for buttons
		player1.setOnClickListener(playerBtnListener);
		player2.setOnClickListener(playerBtnListener);

	}

	protected void playerRespond(int playerId) {
		// TODO:
		int player = 3;
		if (playerId == player1.getId()) {
			Log.d(DEBUG_TAG, "Button Spieler 1");
			player = 0;
		} else if (playerId == player2.getId()) {
			Log.d(DEBUG_TAG, "Button Spieler 2");
			player = 1;
		}
		
	}

	protected void newQuestion(QuestionMultiPlayer q) {
		image.setImageBitmap(q.getAdCensored());
		for (int i = 0; i < 4; i++) {
			answers[i].setText(q.getAnswer(i));
		}
	}
}
