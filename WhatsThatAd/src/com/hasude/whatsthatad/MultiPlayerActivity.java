package com.hasude.whatsthatad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MultiPlayerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mutli_player);

		// get buttons
		Button player1 = (Button) findViewById(R.id.MultiBtnPlayer1);
		Button player2 = (Button) findViewById(R.id.MultiBtnPlayer2);

		ImageView image = (ImageView) findViewById(R.id.MultiImageView);

		Button answer1 = (Button) findViewById(R.id.MultiBtnAnswer1);
		Button answer2 = (Button) findViewById(R.id.MultiBtnAnswer2);
		Button answer3 = (Button) findViewById(R.id.MultiBtnAnswer3);
		Button answer4 = (Button) findViewById(R.id.MultiBtnAnswer4);

		// handlers for buttons
		player1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// start single player menu
				Intent i = new Intent(getApplicationContext(),
						SingleMenuActivity.class);
				startActivity(i);
			}
		});
	}
}
