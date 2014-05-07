package com.feedback.vo;

import java.util.Date;

public class CampaignTemplateVO {

	public Integer customerCampaignId;

	public String compName;

	public String compAddress;

	public String campName;

	public String startDate;

	public String endDate;

	public String description;

	public Integer getCustomerCampaignId() {
		return customerCampaignId;
	}

	public void setCustomerCampaignId(Integer customerCampaignId) {
		this.customerCampaignId = customerCampaignId;
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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCompAddress() {
		return compAddress;
	}

	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

}
