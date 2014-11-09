package com.hasude.whatsthatad;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;
import com.hasude.whatsthatad.gameobjects.Question;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SinglePlayerActivity extends Activity{
	
	Question q;
	ImageView questionImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_player);
		
		// get all views
		EditText solutionEdit = (EditText) findViewById(R.id.SingleEditSolution);
		TextView questionTV = (TextView) findViewById(R.id.SingleQuestionTextView);
		questionImageView = (ImageView) findViewById(R.id.SingleQuestionImageView);
		
		// load question and image
		// TODO: for testing only
		String[] answers = {"Nike", "K-Swiss", "Puma", "Adidas"};
		String correctA = "Adidas";
		Bitmap censored = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.adidas_censored);
		Bitmap uncensored = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.adidas_uncensored);
		try {
			q = new Question(censored, uncensored, answers, correctA);
		} catch (CorrectAnswerException e) {
			Log.d("WTA", "Correct answer was not a given answer possibility");
			e.printStackTrace();
		} catch (WrongNumberOfAnswersException e) {
			Log.d("WTA", "Answer possibility amount was not correct");
			e.printStackTrace();
		}
		
		questionTV.setText(loadQuestion());
		loadPicture(q.getAdCensored());
		
		// handler that fires when user finished typing
		solutionEdit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
			            actionId == EditorInfo.IME_ACTION_DONE ||
			            event.getAction() == KeyEvent.ACTION_DOWN &&
			            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			        if (!event.isShiftPressed()) {
			        	// the user is done typing. 
			        	if(q.getCorrectAnswer().equals(v.getText().toString())) {
			        		loadPicture(q.getAdUncensored());
			        		Toast.makeText(getApplicationContext(), "Yay, that was correct!", Toast.LENGTH_SHORT).show();
			        	} else {
			        		Toast.makeText(getApplicationContext(), "Oh no, that was not correct. Try again!", Toast.LENGTH_SHORT).show();
			        	}
			        	return true; // consume.
			        }                
			    }
			    return false; // pass on to other listeners. 
			}
		});
		
	} // onCreate
	
	private void loadPicture(Bitmap bm) {
		questionImageView.setImageBitmap(bm);
	}
	
	private String loadQuestion() {
		// for testing purposes only
		return "Which company launched this commercial?";
	}
	
}
