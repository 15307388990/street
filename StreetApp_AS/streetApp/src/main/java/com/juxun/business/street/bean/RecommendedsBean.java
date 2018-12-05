package com.juxun.business.street.bean;

import java.io.Serializable;

public class RecommendedsBean implements Serializable{
	private String member_phone;// 电话
	private long create_date;// 邀请时间

	public String getMember_phone() {
		return member_phone;
	}

	public void setMember_phone(String member_phone) {
		this.member_phone = member_phone;
	}

	public long getCreate_date() {
		return create_date;
	}

	public void setCreate_date(long create_date) {
		this.create_date = create_date;
	}
}
