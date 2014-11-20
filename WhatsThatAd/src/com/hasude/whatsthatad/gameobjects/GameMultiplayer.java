package com.hasude.whatsthatad.gameobjects;

import java.io.Serializable;

import com.hasude.whatsthatad.MultiPlayerActivity;
import com.hasude.whatsthatad.R;
import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.exceptions.WrongNumberOfAnswersException;

public class GameMultiplayer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4184454371956161763L;
	private QuestionMultiPlayer[] questions;
	private int actQuestion;
	private int[] points;
	private String[] names;
	private MultiPlayerActivity activity;

	public GameMultiplayer(QuestionMultiPlayer[] q, String... playerNames) {
		questions = q;
		int amountPlayers = playerNames.length;
		points = new int[amountPlayers];
		this.names = new String[amountPlayers];
		for (int i = 0; i < amountPlayers; i++) {
			points[i] = 0;
			this.names[i] = playerNames[i];
		}
		// get Questions
		questions = new QuestionMultiPlayer[10];
		for (int i = 0; i < 10; i++)
			questions[i] = newQuestion();

		actQuestion = 0;
	}

	private QuestionMultiPlayer newQuestion() {
		String censored = "android.resource://com.hasude.whatsthatad/"
				+ R.drawable.adidas_censored;
		String uncensored = "android.resource://com.hasude.whatsthatad/"
				+ R.drawable.adidas_uncensored;

		try {
			QuestionMultiPlayer q = new QuestionMultiPlayer(0, censored,
					uncensored, "Adidas", new String[] { "Nike", "Adidas",
							"Puma", "Reebock" });
			return q;
		} catch (CorrectAnswerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongNumberOfAnswersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public QuestionMultiPlayer nextQuestion() {
		return questions[++actQuestion];
	}

	public String getPlayerName(int i) {
		return names[i];
	}

	public int getPlayerPoints(int i) {
		return points[i];
	}

	public QuestionMultiPlayer getActQuestion() {
		return questions[actQuestion];
	}

	public void increasePlayerPoints(int player, int points) {
		this.points[player] += points;
		activity.onPointsChanged();
	}

	public int getQuestionNumber() {
		return actQuestion + 1;
	}
	
	public void setActivity(MultiPlayerActivity a){
		activity = a;
	}
	
	public void setQuestions(QuestionMultiPlayer[] q){
		questions = q;
	}
}
