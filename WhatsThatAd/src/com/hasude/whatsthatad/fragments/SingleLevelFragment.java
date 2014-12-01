package com.hasude.whatsthatad.fragments;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.hasude.whatsthatad.R;
import com.hasude.whatsthatad.SingleMenuActivity;
import com.hasude.whatsthatad.SinglePlayerActivity;
import com.hasude.whatsthatad.gameobjects.QuestionSinglePlayer;

public class SingleLevelFragment extends Fragment{
	
	private int level;
	private SingleMenuActivity sma;
	
	public SingleLevelFragment(int lvl, SingleMenuActivity s){
		super();
		this.level = lvl;
		this.sma = s;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View levelView = inflater.inflate(R.layout.activity_single_level, container,false);
		
		// Set Progressbar with Levelinformation
		initProgressbar(levelView);
        
        // Set all Thumbnails, ClickListeners and Tags with DatabaseIDs
        initImageBoxes(levelView);
		
		return levelView;
	}

	private void initProgressbar(View levelView) {
		// set Progressbar
		ProgressBar progress = (ProgressBar)levelView.findViewById(R.id.singleProgressbar);
        Drawable barStyle = getResources().getDrawable(R.drawable.single_progressbar);
        progress.setProgressDrawable(barStyle);
        progress.setProgress(50);
	}

	private void initImageBoxes(View levelView) {
		// Setup Imageboxes
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
				
				// Set Identifier in Tag
				image.setTag("" + (j + (i*3)));
				
				// Set Thumbnail
				Drawable d;
				try {
				    InputStream inputStream = getActivity().getContentResolver().openInputStream(sma.questionList.get(level * 12 + (j + (i*3))).getAdCensoredAsUri());
				    d = Drawable.createFromStream(inputStream, sma.questionList.get(level * 12 + (j + (i*3))).getAdCensoredAsUri().toString() );
				} catch (FileNotFoundException e) {
				    d = getResources().getDrawable(R.drawable.face);
				    System.out.println("Bild: " + sma.questionList.get(level * 12 + (j + (i*3))).getAdCensoredAsUri().getPath());
				}			
				Bitmap bm = convertToBitmap(d, 300, 300);		
				image.setImageBitmap(bm);
				
				// Set Clicklistener
				image.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						int questionID = Integer.parseInt((String)v.getTag());									
						
						List<QuestionSinglePlayer> questionList = sma.questionList;
						
//						// start single player menu
						Intent i = new Intent(getActivity(), SinglePlayerActivity.class);
						i.putExtra("question", questionList.get(level*12+questionID));
						startActivity(i);
						
					}
				});
			} 
		}
	}
	
	public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
	    Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(mutableBitmap);
	    drawable.setBounds(0, 0, widthPixels, heightPixels);
	    drawable.draw(canvas);

	    return mutableBitmap;
	}
}
