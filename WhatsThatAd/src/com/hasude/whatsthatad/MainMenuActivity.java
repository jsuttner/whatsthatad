package com.hasude.whatsthatad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuActivity extends Activity {

	// Button Animation
	Animation animHide;
	Animation animShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		animHide = AnimationUtils.loadAnimation(this, R.drawable.button_hide);
		animShow = AnimationUtils.loadAnimation(this, R.drawable.button_show);

		// get buttons for Animation
		Button singleplayerBtn = (Button) findViewById(R.id.MenuBtnSingleplayer);
		Button multiplayerBtn = (Button) findViewById(R.id.MenuBtnMultiplayer);
		Button addAdBtn = (Button) findViewById(R.id.MenuBtnAddAnAdd);
		Button optionsBtn = (Button) findViewById(R.id.MenuBtnOptions);
		Button exitBtn = (Button) findViewById(R.id.MenuBtnExit);
		ImageView logo = (ImageView) findViewById(R.id.MenuLogo);

		displayLogo(logo);
		displayBtn(singleplayerBtn);
		displayBtn(multiplayerBtn);
		displayBtn(addAdBtn);
		displayBtn(optionsBtn);
		displayBtn(exitBtn);

		// handlers for buttons
		singleplayerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickAnimation(v);
				// start single player menu
				Intent i = new Intent(getApplicationContext(),
						SingleMenuActivity.class);
				startActivity(i);
			}
		});
		multiplayerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickAnimation(v);
				// starts muli player menu
				Intent i = new Intent(getApplicationContext(),
						MultiMenuActivity.class);
				startActivity(i);

			}
		});
		addAdBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickAnimation(v);
				Intent i = new Intent(getApplicationContext(),
						EditorActivity.class);
				startActivity(i);
			}
		});
		optionsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: start options

			}
		});
		exitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: ask if user really wants to leave, if yes terminate app
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainMenuActivity.this);

				// Add the buttons
				builder.setPositiveButton(R.string.leave_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});
				builder.setNeutralButton(R.string.leave_cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// return;
							}
						});
				builder.setMessage(R.string.leave_message);

				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// get buttons for animation
		Button singleplayerBtn = (Button) findViewById(R.id.MenuBtnSingleplayer);
		Button multiplayerBtn = (Button) findViewById(R.id.MenuBtnMultiplayer);
		Button addAdBtn = (Button) findViewById(R.id.MenuBtnAddAnAdd);
		Button optionsBtn = (Button) findViewById(R.id.MenuBtnOptions);
		Button exitBtn = (Button) findViewById(R.id.MenuBtnExit);
		ImageView logo = (ImageView) findViewById(R.id.MenuLogo);

		displayLogo(logo);
		displayBtn(singleplayerBtn);
		displayBtn(multiplayerBtn);
		displayBtn(addAdBtn);
		displayBtn(optionsBtn);
		displayBtn(exitBtn);
	}

	public void onClickAnimation(View v) {
		hideButton(v);
	}

	/**
	 * Starts button disappear animation
	 * @param v
	 */
	private void hideButton(View v) {
		animHide.reset();
		v.startAnimation(animHide);
		v.setVisibility(View.INVISIBLE);
	}

	/**
	 * Starts logo animation
	 * @param v
	 */
	private void displayLogo(View v) {
		Animation logo = AnimationUtils.loadAnimation(this,
				R.drawable.logo_hide);
		logo.reset();
		v.clearAnimation();
		v.startAnimation(logo);
		v.setVisibility(View.VISIBLE);
	}

	/**
	 * Starts button show animation
	 * @param v
	 */
	private void displayBtn(View v) {
		animShow.reset();
		v.clearAnimation();
		v.startAnimation(animShow);
		v.setVisibility(View.VISIBLE);
	}
}
