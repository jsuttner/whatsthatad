package com.hasude.whatsthatad.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.hasude.whatsthatad.R;
import com.hasude.whatsthatad.SinglePlayerActivity;

public class SingleLevelFragment extends Fragment{
	
	ViewPager pager;
	
	public SingleLevelFragment(ViewPager p){
		super();
		this.pager = p;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View levelView = inflater.inflate(R.layout.activity_single_level, container,false);
		
		// set Progressbar
		ProgressBar progress = (ProgressBar)levelView.findViewById(R.id.singleProgressbar);
        Drawable barStyle = getResources().getDrawable(R.drawable.single_progressbar);
        progress.setProgressDrawable(barStyle);
        progress.setProgress(50);
        
        // Set all Thumbnails, ClickListeners and Tags with DatabaseIDs
        initImageBoxes(levelView);
		
		return levelView;
	}
	
	private void initImageBoxes(View levelView) {

		int level = pager.getCurrentItem();
		TableLayout levelTable = (TableLayout)levelView.findViewById(R.id.singleLevelTable);
		int count = levelTable.getChildCount();
		TableRow tableRow = null;
		for(int i=0; i<count; i++) {
			tableRow = (TableRow)levelTable.getChildAt(i);
		    // Iteration through Images
			int countRow = tableRow.getChildCount();
			ImageView image = null;
			for(int j=0; j<countRow; j++){
				
				// Set ClickListener for Image
				image = (ImageView)tableRow.getChildAt(j);
				
		        // TODO: get Images and IDs from Database
				// Set ID in Tag
				image.setTag("");
				
				image.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						int questionID = Integer.parseInt((String)v.getTag());
						
						// start single player menu
						Intent i = new Intent(getActivity(), SinglePlayerActivity.class);
						i.putExtra("questionID", questionID);
						startActivity(i);
						
					}
				});
			} 
		}
	}
}
