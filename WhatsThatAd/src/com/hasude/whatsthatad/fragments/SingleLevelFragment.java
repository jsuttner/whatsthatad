package com.hasude.whatsthatad.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hasude.whatsthatad.R;

public class SingleLevelFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View levelView = inflater.inflate(R.layout.activity_single_level, container,false);
		
		// set Progressbar
		ProgressBar progress = (ProgressBar)levelView.findViewById(R.id.singleProgressbar);
        Drawable barStyle = getResources().getDrawable(R.drawable.single_progressbar);
        progress.setProgressDrawable(barStyle);
        progress.setProgress(50);
		
		return levelView;
	}

	
}
