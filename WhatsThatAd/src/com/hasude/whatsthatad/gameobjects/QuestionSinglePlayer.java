package com.hasude.whatsthatad.gameobjects;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;

public class QuestionSinglePlayer extends Question {
	
	private static final long serialVersionUID = -3184672673027920823L;
	private String question;
	private boolean solved;

	public QuestionSinglePlayer(int id, String censored, String uncensored,
			String correctAnswer, String question, int solved) throws CorrectAnswerException {
		super(id, censored, uncensored, correctAnswer);
		this.question = question;
		if(solved == 0) 
			this.solved = false;
		else
			this.solved = true;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public boolean getSolved() {
		return solved;
	}
}
