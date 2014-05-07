package com.feedback.vo;

import java.util.Date;

public class CampaignVO extends BaseVO {

	public Integer campId;

	public Integer compId;

	public Integer branchId;

	public String campName;

	public Date startDate;

	public Date endDate;

	public String description;

	public Integer approve;

	public Integer emailTemplate;

	public Integer smsTemplate;

	public String compName;

	public String branchName;

	public String getStatusStr() {
		String s = "";
		if (TypeVO.ACTIVE.getId().equals(getApprove())) {
			s = TypeVO.ACTIVE.getName();
		} else if (TypeVO.INACTIVE.getId().equals(getApprove())) {
			s = TypeVO.INACTIVE.getName();
		}
		return s;
	}

	public Integer getApprove() {
		return approve;
	}

	public void setApprove(Integer approve) {
		this.approve = approve;
	}

	public Integer getCampId() {
		return campId;
	}

	public void setCampId(Integer campId) {
		this.campId = campId;
	}

	public String getCampName() {
		return campName;
	}

	public void setCampName(String campName) {
		this.campName = campName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public Integer getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(Integer emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public Integer getSmsTemplate() {
		return smsTemplate;
	}

	public void setSmsTemplate(Integer smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

}
