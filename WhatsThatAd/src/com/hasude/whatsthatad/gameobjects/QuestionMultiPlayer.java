package com.hasude.whatsthatad.gameobjects;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;

public class QuestionMultiPlayer extends Question {

	private static final long serialVersionUID = -8442963180385360498L;
	private String[] answers;

	public QuestionMultiPlayer(int id, String censored, String uncensored,
			String correctAnswer, String[] answers)
			throws CorrectAnswerException, WrongNumberOfAnswersException {
		super(id, censored, uncensored, correctAnswer);
		// TODO Auto-generated constructor stub

		// verify
		if (answers.length != 4)
			throw new WrongNumberOfAnswersException();
		boolean found = false;
		for (int i = 0; i < answers.length; i++) {
			if (answers[i].equals(correctAnswer))
				found = true;
		}
		if (!found)
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
