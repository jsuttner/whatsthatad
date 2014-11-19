package com.hasude.whatsthatad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hasude.whatsthatad.gameobjects.GameMultiplayer;
import com.hasude.whatsthatad.gameobjects.QuestionMultiPlayer;

public class MultiPlayerActivity extends Activity {

	public static final String DEBUG_TAG = "Multiplayer";

	private GameMultiplayer game;

	// UI
	private ImageView image;

	private Button[] pBtns = new Button[2];

	private static final int[] P_COLORS = new int[] {
			Color.argb(255, 98, 196, 98), Color.BLUE };

	private Button[] answers;

	protected boolean waitingForNextRound = false;

	private TextView information;

	private Animation animHide;
	private Animation animShow;

	final Handler menuHandler = new Handler();
	boolean menu_visible = false;
	Runnable menu_hide_thread;

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

		pBtns[0].setOnClickListener(playerBtnListener);
		pBtns[1].setOnClickListener(playerBtnListener);

		animHide = AnimationUtils.loadAnimation(this,
				R.drawable.information_hide);
		animShow = AnimationUtils.loadAnimation(this,
				R.drawable.information_show);

		information = (TextView) findViewById(R.id.MultiTxtInformation);

		menu_hide_thread = new Runnable() {
			public void run() {
				hideInformation();
			}

		};

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(DEBUG_TAG, "OnClickListener");
				if (waitingForNextRound == true) {
					waitingForNextRound = false;
					initGame(game.nextQuestion());
				}
			}

		});

		Intent i = getIntent();
		String p1 = i.getStringExtra("player1Name");
		String p2 = i.getStringExtra("player2Name");

		if (p1 == null || p2 == null) {
			p1 = "Player1";
			p2 = "Player2";
		}

		game = new GameMultiplayer(this, p1, p2);

		initGame(game.getActQuestion());

	}

	protected void answerGiven(int player, String answer) {

		setAnswerButtonsEnabled(0, false);

		if (game.getActQuestion().isAnswerCorrect(answer)) {
			game.increasePlayerPoints(player, 1);

			showInformation(getResources().getString(
					R.string.multi_info_correct_answer));

			image.setImageURI(game.getActQuestion().getAdUncensoredAsUri());
			
			waitingForNextRound = true;

		} else {
			game.increasePlayerPoints(player, -1);
			showInformation(getResources().getString(
					R.string.multi_info_wrong_answer));
			setPlayerButtonsEnabled(true);

		}

	}

	protected void playerRespond(int playerId) {

		setPlayerButtonsEnabled(false);

		int p = 3;
		for (int i = 0; i < 2; i++) {
			if (playerId == pBtns[i].getId()) {
				Log.d(DEBUG_TAG, "Button Spieler " + i);
				p = i;
			}
		}

		showInformation(game.getPlayerName(p) + "\n"
				+ getResources().getString(R.string.multi_info_player_change));

		setAnswerButtonsEnabled(p, true);

	}
	
	protected void initGame(QuestionMultiPlayer q) {
		// handlers for buttons

		showInformation("Question " + game.getQuestionNumber() + "/10");
		
		image.setImageURI(q.getAdCensoredAsUri());
		for (int i = 0; i < 4; i++) {
			answers[i].setText(q.getAnswer(i));
		}
		setAnswerButtonsEnabled(0, false);
		for (int i = 0; i < 2; i++)
			pBtns[i].setBackgroundColor(P_COLORS[i]);
		onPointsChanged();
		setPlayerButtonsEnabled(true);
	}

	private void setPlayerButtonsEnabled(boolean enable) {
		for (Button b : pBtns)
			b.setClickable(enable);
	}

	private void setAnswerButtonsEnabled(final int player, boolean enabled) {
		if (enabled) {

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
				b.setClickable(true);
			}
		} else {
			for (Button b : answers) {
				b.setBackgroundColor(Color.GRAY);
				b.setOnClickListener(null);
				b.setClickable(false);
			}

		}

	}

	public void onPointsChanged() {
		for (int i = 0; i < 2; i++)
			pBtns[i].setText("" + game.getPlayerPoints(i));
	}

	private void hideInformation() {
		menu_visible = false;
		animHide.reset();
		information.startAnimation(animHide);
		information.setVisibility(View.INVISIBLE);
	}

	private void displayMenu() {
		menu_visible = true;
		animShow.reset();
		information.clearAnimation();
		information.startAnimation(animShow);
		information.setVisibility(View.VISIBLE);
	}

	public boolean showInformation(String text) {
		information.setText(text);
		if (!menu_visible) {
			displayMenu();
			menuHandler.postDelayed(menu_hide_thread, 5000);
		} else {
			menuHandler.removeCallbacks(menu_hide_thread);
			displayMenu();
			menuHandler.postDelayed(menu_hide_thread, 5000);
		}
		return true;
	}

}
