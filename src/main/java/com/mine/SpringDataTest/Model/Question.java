package com.mine.SpringDataTest.Model;

public class Question implements java.io.Serializable {
	
	int questionId;
	
	String question;
	
	public Question() {
	}

	public Question(int questionId, String question) {
		super();
		this.questionId = questionId;
		this.question = question;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	
		
}
