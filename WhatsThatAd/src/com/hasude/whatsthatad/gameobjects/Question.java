package com.hasude.whatsthatad.gameobjects;

import android.graphics.Bitmap;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;

public class Question {
	private Bitmap adCensored;
	private Bitmap adUncensored;
	private String correctAnswer;

	public Question(Bitmap censored, Bitmap uncensored, String correctAnswer)
			throws CorrectAnswerException {
		this.adCensored = censored;
		this.adUncensored = uncensored;
		this.correctAnswer = correctAnswer;
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

}
