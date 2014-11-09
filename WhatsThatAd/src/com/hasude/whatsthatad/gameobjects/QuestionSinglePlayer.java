package com.hasude.whatsthatad.gameobjects;

import android.graphics.Bitmap;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;

public class QuestionSinglePlayer extends Question {
	
	String question;

	public QuestionSinglePlayer(Bitmap censored, Bitmap uncensored,
			String correctAnswer, String question) throws CorrectAnswerException {
		super(censored, uncensored, correctAnswer);
		this.question = question;
	}
	
	public String getQuestion() {
		return question;
	}
}
