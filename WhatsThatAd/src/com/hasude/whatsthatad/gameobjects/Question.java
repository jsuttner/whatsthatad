package com.hasude.whatsthatad.gameobjects;

import java.io.Serializable;
import java.util.Locale;

import android.net.Uri;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;

public class Question implements Serializable{

	private static final long serialVersionUID = -8449456826201147919L;
	private int id;
	private String adCensored;
	private String adUncensored;
	private String correctAnswer;

	public Question(int id, String censored, String uncensored,
			String correctAnswer) throws CorrectAnswerException {
		this.id = id;
		this.adCensored = censored;
		this.adUncensored = uncensored;
		this.correctAnswer = correctAnswer;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public String getAdUncensored() {
		return adUncensored;
	}

	public String getAdCensored() {
		return adCensored;
	}

	public Uri getAdUncensoredAsUri() {
		return Uri.parse("" + adUncensored);
	}

	public Uri getAdCensoredAsUri() {
		return Uri.parse("" + adCensored);
	}

	public int getID() {
		return id;
	}

	public boolean isAnswerCorrect(String stringMsg) {
		Locale l = new Locale("en");
		return (stringMsg.toLowerCase(l).equals(correctAnswer.toLowerCase(l)));
	}

}
