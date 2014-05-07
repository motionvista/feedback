package com.feedback.vo;

public class DeviceVO extends BaseVO {

	public Integer id;

	public Integer compId;

	public Integer branchId;

	public String deviceId;

	public Integer status;

	public String companyName;

	public String branchName;

	public String getStatusStr() {
		String s = "";
		if (TypeVO.REGISTER.getId().equals(getStatus())) {
			s = TypeVO.REGISTER.getName();
		} else if (TypeVO.UNREGISTER.getId().equals(getStatus())) {
			s = TypeVO.UNREGISTER.getName();
		}
		return s;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
