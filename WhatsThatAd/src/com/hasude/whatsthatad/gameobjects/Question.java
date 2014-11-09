package com.hasude.whatsthatad.gameobjects;

import java.util.Locale;

import android.net.Uri;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;

public class Question {
	private int id;
	private Uri adCensored;
	private Uri adUncensored;
	private String correctAnswer;

	public Question(int id, String censored, String uncensored,
			String correctAnswer) throws CorrectAnswerException {
		this.id = id;
		this.adCensored = Uri.parse(censored);
		this.adUncensored = Uri.parse(uncensored);
		this.correctAnswer = correctAnswer;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public Uri getAdUncensored() {
		return adUncensored;
	}

	public Uri getAdCensored() {
		return adCensored;
	}

	public int getID() {
		return id;
	}

	public boolean isAnswerCorrect(String stringMsg) {
		Locale l = new Locale("en");
		return (stringMsg.toLowerCase(l).equals(correctAnswer.toLowerCase(l)));
	}

}
