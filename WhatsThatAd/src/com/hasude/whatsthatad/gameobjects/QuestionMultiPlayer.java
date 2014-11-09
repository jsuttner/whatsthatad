package com.hasude.whatsthatad.gameobjects;

import android.graphics.Bitmap;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;

public class QuestionMultiPlayer extends Question {
	
	private String[] answers;
	
	public QuestionMultiPlayer(Bitmap censored, Bitmap uncensored, String correctAnswer,
			String[] answers)
			throws CorrectAnswerException, WrongNumberOfAnswersException {
		super(censored, uncensored, correctAnswer);
		// TODO Auto-generated constructor stub
		
		// verify
		if(answers.length != 4)
			throw new WrongNumberOfAnswersException();
		boolean found = false;
		for (int i = 0; i < answers.length; i++) {
			if(answers[i].equals(correctAnswer))
				found = true;
		}
		if(!found)
			throw new CorrectAnswerException();
		this.answers = answers;
	}
	
	public String[] getAnswers() {
		return answers;
	}

	public String getAnswer(int i) {
		return answers[i];
	}

}
