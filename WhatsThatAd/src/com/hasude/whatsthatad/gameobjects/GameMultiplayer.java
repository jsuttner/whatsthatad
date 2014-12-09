package com.hasude.whatsthatad.gameobjects;

import java.io.Serializable;

import com.hasude.whatsthatad.MultiPlayerActivity;

public class GameMultiplayer implements Serializable {

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

		actQuestion = 0;
	}

	public QuestionMultiPlayer nextQuestion() {
		return questions[++actQuestion];
	}

	public String getWinner() {
		if(points[0]> points[1])
			return names[0];
		else if(points[1]> points[0])
			return names[1];
		else 
			return "Both";
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

	public void setActivity(MultiPlayerActivity a) {
		activity = a;
	}

	public void setQuestions(QuestionMultiPlayer[] q) {
		questions = q;
	}
}
