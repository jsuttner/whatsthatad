package com.hasude.whatsthatad;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SingleMenuActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		// set Progressbar
		ProgressBar progress = (ProgressBar)findViewById(R.id.singleProgressbar);
        Drawable barStyle = getResources().getDrawable(R.drawable.single_progressbar);
        progress.setProgressDrawable(barStyle);
        progress.setProgress(50);
	}
}