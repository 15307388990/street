package com.juxun.business.street.widget.wheel;

import java.util.List;

public class ProvinceModel {
	private String name;
	private List<CityModel2> cityList;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel2> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel2> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel2> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
