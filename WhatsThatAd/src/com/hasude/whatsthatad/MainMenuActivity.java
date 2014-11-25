package com.hasude.whatsthatad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		// TODO: load Experience for user
		
		// get buttons
		Button singleplayerBtn = (Button) findViewById(R.id.MenuBtnSingleplayer);
		Button multiplayerBtn = (Button) findViewById(R.id.MenuBtnMultiplayer);
		Button addAdBtn = (Button) findViewById(R.id.MenuBtnAddAnAdd);
		Button optionsBtn = (Button) findViewById(R.id.MenuBtnOptions);
		Button exitBtn = (Button) findViewById(R.id.MenuBtnExit);
		
		// handlers for buttons
		singleplayerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// start single player menu
				Intent i = new Intent(getApplicationContext(), SingleMenuActivity.class);
				startActivity(i);
			}
		});
		multiplayerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// starts muli player menu
				Intent i = new Intent(getApplicationContext(), MultiMenuActivity.class);
				startActivity(i);
				
			}
		});
		addAdBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), EditorActivity.class);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
				
				// Add the buttons
				builder.setPositiveButton(R.string.leave_ok, new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		           }
		        });
				builder.setNeutralButton(R.string.leave_cancel, new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int id) {
		               //return;
		           }
		        });
				builder.setMessage(R.string.leave_message);

				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
	}
}
