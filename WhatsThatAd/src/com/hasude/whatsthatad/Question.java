package com.hasude.whatsthatad;

import android.graphics.Bitmap;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;

public class Question {
	private String[] answers;
	private Bitmap adCensored;
	private Bitmap adUncensored;
	private String correctAnswer;

	public Question(Bitmap censored, Bitmap uncensored,
			String[] answers, String correctAnswer)
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
