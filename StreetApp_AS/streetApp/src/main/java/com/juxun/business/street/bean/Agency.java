package com.juxun.business.street.bean;

import java.io.Serializable;

public class Agency implements Serializable {
	/**
	 * 机构
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int failTextType;// 审核失败类型
	private int status;// integer,状态 0未审核1审核成功2失败",
	private String agencyCity;// string,机构服务的城市",
	private String failRemark;// string,审核失败备注",
	private String agencyParent;// string,父级机构",
	private String agencyName;// string,机构名称",
	private String agencyType;// integer,机构类型 总公司HEADQUARTERS(0),

	public int getFailTextType() {
		return failTextType;
	}

	public void setFailTextType(int failTextType) {
		this.failTextType = failTextType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAgencyCity() {
		return agencyCity;
	}

	public void setAgencyCity(String agencyCity) {
		this.agencyCity = agencyCity;
	}

	public String getFailRemark() {
		return failRemark;
	}

	public void setFailRemark(String failRemark) {
		this.failRemark = failRemark;
	}

	public String getAgencyParent() {
		return agencyParent;
	}

	public void setAgencyParent(String agencyParent) {
		this.agencyParent = agencyParent;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgencyType() {
		return agencyType;
	}

	public void setAgencyType(String agencyType) {
		this.agencyType = agencyType;
	}

}
