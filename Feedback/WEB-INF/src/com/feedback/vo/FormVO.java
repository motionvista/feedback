package com.feedback.vo;

import java.util.Date;

public class FormVO extends BaseVO {

	public Integer formId;

	public String name;

	public Integer compId;

	public Integer branchId;

	public Date validFromDate;

	public Date validToDate;

	public Integer comment;

	public Integer audio;

	public Integer video;

	public Integer draw;

	public Integer status;

	public FormQuestionVO[] questions;

	public String compName;

	public String branchName;

	public String getStatusStr() {
		String s = "";
		if (TypeVO.ACTIVE.getId().equals(getStatus())) {
			s = TypeVO.ACTIVE.getName();
		} else if (TypeVO.INACTIVE.getId().equals(getStatus())) {
			s = TypeVO.INACTIVE.getName();
		}
		return s;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public FormQuestionVO[] getQuestions() {
		return questions;
	}

	public void setQuestions(FormQuestionVO[] questiions) {
		this.questions = questiions;
	}

	public Integer getAudio() {
		return audio;
	}

	public void setAudio(Integer audio) {
		this.audio = audio;
	}

	public Integer getComment() {
		return comment;
	}

	public void setComment(Integer comment) {
		this.comment = comment;
	}

	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Integer getFormId() {
		return formId;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}

	public Date getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Date validToDate) {
		this.validToDate = validToDate;
	}

	public Integer getVideo() {
		return video;
	}

	public void setVideo(Integer video) {
		this.video = video;
	}

	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

}
