package com.hasude.whatsthatad.exceptions;

public class CorrectAnswerException extends Exception {
	
	public CorrectAnswerException(){
		super("Correct answer is not a possible answer");
	}
}
