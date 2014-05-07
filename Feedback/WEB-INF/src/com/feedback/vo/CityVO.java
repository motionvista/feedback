package com.feedback.vo;

public class CityVO extends BaseVO {
	public Integer cityId;

	public String name;

	public Integer stateId;

	public Integer countryId;

	public String countryName;

	public String stateName;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;

	}

	public String getName() {
		return name;

	}

	public void setName(String name) {
		this.name = name;

	}

	public Integer getStateId() {
		return stateId;

	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;

	}

}
