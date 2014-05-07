package com.feedback.vo;

public class QuestionVO extends BaseVO {

	public Integer questionId;

	public String question;

	public Integer type;

	public String lable1;

	public String lable2;

	public String lable3;

	public String lable4;

	public Integer rating1;

	public Integer rating2;

	public Integer rating3;

	public Integer rating4;

	public String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLable1() {
		return lable1;
	}

	public void setLable1(String lable1) {
		this.lable1 = lable1;
	}

	public String getLable2() {
		return lable2;
	}

	public void setLable2(String lable2) {
		this.lable2 = lable2;
	}

	public String getLable3() {
		return lable3;
	}

	public void setLable3(String lable3) {
		this.lable3 = lable3;
	}

	public String getLable4() {
		return lable4;
	}

	public void setLable4(String lable4) {
		this.lable4 = lable4;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRating1() {
		return rating1;
	}

	public void setRating1(Integer rating1) {
		this.rating1 = rating1;
	}

	public Integer getRating2() {
		return rating2;
	}

	public void setRating2(Integer rating2) {
		this.rating2 = rating2;
	}

	public Integer getRating3() {
		return rating3;
	}

	public void setRating3(Integer rating3) {
		this.rating3 = rating3;
	}

	public Integer getRating4() {
		return rating4;
	}

	public void setRating4(Integer rating4) {
		this.rating4 = rating4;
	}
}
