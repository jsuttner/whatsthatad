package com.hasude.whatsthatad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class QuestionUploadActivity extends Activity{
	
	EditText quesInput;
	EditText correctAnsInput;
	EditText answer1Input;
	EditText answer2Input;
	EditText answer3Input;
	EditText answer4Input;
	RadioGroup typeGroup;
	
	int questionType = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_uploader);
		
		quesInput = (EditText) findViewById(R.id.upload_question_input);
		correctAnsInput = (EditText) findViewById(R.id.upload_correct_input);
		answer1Input = (EditText) findViewById(R.id.upload_answer1_input);
		answer2Input = (EditText) findViewById(R.id.upload_answer2_input);
		answer3Input = (EditText) findViewById(R.id.upload_answer3_input);
		answer4Input = (EditText) findViewById(R.id.upload_answer4_input);
		
		// get intent extra
//		Intent intent = getIntent();
//		Bitmap cencored = (Bitmap) intent.getParcelableExtra("cencored");
//		Bitmap uncencored = (Bitmap) intent.getParcelableExtra("uncencored");
		
		typeGroup = (RadioGroup) findViewById(R.id.typeGroup);	
		typeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.single_radio:
						questionType = 0;
						
						// set answer poss invisible
						answer1Input.setVisibility(View.INVISIBLE);
						answer2Input.setVisibility(View.INVISIBLE);
						answer3Input.setVisibility(View.INVISIBLE);
						answer4Input.setVisibility(View.INVISIBLE);
						break;
					case R.id.multi_radio:
						questionType = 1;
						
						// set answer poss visible
						answer1Input.setVisibility(View.VISIBLE);
						answer2Input.setVisibility(View.VISIBLE);
						answer3Input.setVisibility(View.VISIBLE);
						answer4Input.setVisibility(View.VISIBLE);
						break;
					default:
						// TODO; throw exception
				}
				
			}
		});
	}
	
	public void saveBtnListener(View v) {
		// check correctness
		
		// TODO; save in Database
		
		// start editor activity
		Intent i = new Intent(getApplicationContext(), EditorActivity.class);
		startActivity(i);
		finish();
	}

}
