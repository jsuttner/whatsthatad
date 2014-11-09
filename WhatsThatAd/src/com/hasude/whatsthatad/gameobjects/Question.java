package com.hasude.whatsthatad.gameobjects;

import android.graphics.Bitmap;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;

public class Question {
	private Bitmap adCensored;
	private Bitmap adUncensored;
	private String correctAnswer;

	public Question(Bitmap censored, Bitmap uncensored, String correctAnswer)
			throws CorrectAnswerException {
		this.adCensored = censored;
		this.adUncensored = uncensored;
		this.correctAnswer = correctAnswer;

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
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public Bitmap getAdUncensored() {
		return adUncensored;
	}

	public Bitmap getAdCensored() {
		return adCensored;
	}

	public String[] getAnswers() {
		return answers;
	}

	public String getAnswer(int i) {
		return answers[i];
	}

}
