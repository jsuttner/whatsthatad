package com.hasude.whatsthatad;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;

import android.R.string;
import android.graphics.Bitmap;

public class Question {
	private string[] answers;
	private Bitmap adCensored;
	private Bitmap adUncensored;
	private string correctAnswer;

	public Question(Bitmap censored, Bitmap uncensored,
			string[] answers, string correctAnswer)
			throws CorrectAnswerException, WrongNumberOfAnswersException {
		this.adCensored = censored;
		this.adUncensored = uncensored;
		this.answers = answers;
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

	public string getCorrectAnswer() {
		return correctAnswer;
	}

	public Bitmap getAdUncensored() {
		return adUncensored;
	}

	public Bitmap getAdCensored() {
		return adCensored;
	}

	public string[] getAnswers() {
		return answers;
	}

}
