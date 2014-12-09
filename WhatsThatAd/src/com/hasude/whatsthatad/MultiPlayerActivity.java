package com.hasude.whatsthatad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.util.Log;
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

	private static final int[] P_COLORS = new int[] { R.color.player1,
			R.color.player2 };

	private GameMultiplayer game;

	protected boolean waitingForNextRound = false;

	// UI
	private ImageView image;

	private static final int[] P_BUTTONS = new int[] {
			R.drawable.multi_answer_button_p1,
			R.drawable.multi_answer_button_p2 };

	private Button[] answers;

	private TextView information;

	private Button[] pBtns = new Button[2];

	private OnClickListener playerBtnListener;

	// Animation:
	private Animation animHide;
	private Animation animShow;
	final Handler menuHandler = new Handler();
	boolean menu_visible = false;
	Runnable menu_hide_thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mutli_player);

		// get views
		
		image = (ImageView) findViewById(R.id.MultiImageView);

		pBtns[0] = (Button) findViewById(R.id.MultiBtnPlayer1);
		pBtns[1] = (Button) findViewById(R.id.MultiBtnPlayer2);

		answers = new Button[4];
		answers[0] = (Button) findViewById(R.id.MultiBtnAnswer1);
		answers[1] = (Button) findViewById(R.id.MultiBtnAnswer2);
		answers[2] = (Button) findViewById(R.id.MultiBtnAnswer3);
		answers[3] = (Button) findViewById(R.id.MultiBtnAnswer4);

		information = (TextView) findViewById(R.id.MultiTxtInformation);

		// init Animations
		animHide = AnimationUtils.loadAnimation(this,
				R.drawable.information_hide);
		animShow = AnimationUtils.loadAnimation(this,
				R.drawable.information_show);

		menu_hide_thread = new Runnable() {
			@Override
			public void run() {
				hideInformation();
			}

		};
		
		// Listener
		
		playerBtnListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				MultiPlayerActivity.this.playerRespond(v.getId());
			}
		};
		pBtns[0].setOnClickListener(playerBtnListener);
		pBtns[1].setOnClickListener(playerBtnListener);
		
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(DEBUG_TAG, "OnClickListener");
				if (waitingForNextRound == true) {
					waitingForNextRound = false;
					if (game.getQuestionNumber() < 10) {
						initGame(game.nextQuestion());
					} else
						finish();
				}
			}

		});

		// get gameobject
		
		Intent i = getIntent();
		game = (GameMultiplayer) i.getSerializableExtra("game");
		game.setActivity(this);

		initGame(game.getActQuestion());

	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	/**
	 * Is called when Player hits one of the 4 answer buttons.
	 */
	protected void answerGiven(int player, String answer) {

		setAnswerButtonsEnabled(0, false);

		if (game.getActQuestion().isAnswerCorrect(answer)) {
			game.increasePlayerPoints(player, 1);

			if (game.getQuestionNumber() < 10)
				showInformation(getResources().getString(
						R.string.multi_info_correct_answer));
			else
				showInformation(game.getWinner() + " "
						+ getResources().getString(R.string.multi_info_win));

			image.setImageURI(game.getActQuestion().getAdUncensoredAsUri());

			waitingForNextRound = true;

		} else {
			game.increasePlayerPoints(player, -1);
			showInformation(getResources().getString(
					R.string.multi_info_wrong_answer));
			setPlayerButtonsEnabled(true);
		}

	}

	/**
	 * Player wants to answer the question.
	 * @param playerId
	 */
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

	/**
	 * Initializes game for the question q
	 * @param q
	 */
	protected void initGame(QuestionMultiPlayer q) {
		// handlers for buttons

		showInformation("Question " + game.getQuestionNumber() + "/10");

		image.setImageURI(q.getAdCensoredAsUri());
		for (int i = 0; i < 4; i++) {
			answers[i].setText(q.getAnswer(i));
		}
		setAnswerButtonsEnabled(0, false);
		onPointsChanged();
		setPlayerButtonsEnabled(true);
	}

	private void setPlayerButtonsEnabled(boolean enable) {
		for (Button b : pBtns)
			b.setClickable(enable);
	}

	/**
	 * Sets colors and clickable option for the 4 answer buttons.
	 * @param player: When enabled=true buttons will be colored in player color
	 * @param enabled
	 */
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
				b.setBackgroundResource(P_BUTTONS[player]);
				b.setOnClickListener(answerOptionsListener);
				b.setClickable(true);
			}
		} else {
			for (Button b : answers) {
				b.setBackgroundColor(Color.GRAY);
				b.setBackgroundResource(R.drawable.back);
				b.setOnClickListener(null);
				b.setClickable(false);
			}

		}

	}

	/**
	 * Will be called by GameMultiplayer.
	 * Keeps UI score up to date.
	 */
	public void onPointsChanged() {
		for (int i = 0; i < 2; i++)
			pBtns[i].setText("" + game.getPlayerPoints(i));
	}
	
// Animation:
	private void hideInformation() {
		menu_visible = false;
		animHide.reset();
		information.startAnimation(animHide);
		information.setVisibility(View.GONE);
	}

	private void displayMenu() {
		menu_visible = true;
		animShow.reset();
		information.clearAnimation();
		information.startAnimation(animShow);
		information.setVisibility(View.VISIBLE);
	}

	/**
	 * Will show Information for 5 Seconds with text.
	 * @param text
	 * @return
	 */
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
