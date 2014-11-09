package com.hasude.whatsthatad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MultiMenuActivity extends Activity{
	
	EditText player1Edit;
	EditText player2Edit; 
	
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
					Intent i = new Intent(getApplicationContext(), MultiPlayerActivity.class);
					i.putExtra("player1Name", player1Edit.getText().toString());
					i.putExtra("player2Name", player2Edit.getText().toString());
					startActivity(i);
				} else {
					// tell the user to fill both editables
					Toast.makeText(getApplicationContext(), "Please enter both names.", Toast.LENGTH_SHORT).show();;
				}
			}
		});
		
	}
	
	public boolean isStartPossible() {
		Log.d("WTA", player1Edit.getText().toString());
		if(player1Edit.getText().toString().equals("") || player2Edit.getText().toString().equals("")) {
			return false;
		} else return true;
	}

}
