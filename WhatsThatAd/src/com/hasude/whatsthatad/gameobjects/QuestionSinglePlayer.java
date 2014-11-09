package com.hasude.whatsthatad.gameobjects;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;

public class QuestionSinglePlayer extends Question {
	
	String question;

	public QuestionSinglePlayer(int id, String censored, String uncensored,
			String correctAnswer, String question) throws CorrectAnswerException {
		super(id, censored, uncensored, correctAnswer);
		this.question = question;
	}
	
	public String getQuestion() {
		return question;
	}
}
