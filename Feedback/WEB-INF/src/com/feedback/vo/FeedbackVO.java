package com.feedback.vo;

public class FeedbackVO extends BaseVO {

	public Integer feedbackId;

	public Integer custId;

	public Integer compId;

	public Integer branchId;

	public String comment;

	public Integer feedbackDuration;

	public Integer flag;

	public Integer potentialEescalation;

	public FeedbackQuestionAnsVO[] questionAns;

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public FeedbackQuestionAnsVO[] getQuestionAns() {
		return questionAns;
	}

	public void setQuestionAns(FeedbackQuestionAnsVO[] questionAns) {
		this.questionAns = questionAns;
	}

	public Integer getFeedbackDuration() {
		return feedbackDuration;
	}

	public void setFeedbackDuration(Integer feedbackDuration) {
		this.feedbackDuration = feedbackDuration;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getPotentialEescalation() {
		return potentialEescalation;
	}

	public void setPotentialEescalation(Integer potentialEescalation) {
		this.potentialEescalation = potentialEescalation;
	}

}
