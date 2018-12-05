package com.juxun.business.street.bean;

import java.io.Serializable;

import android.R.integer;

public class UserStatisticsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String member_phone;
	private int recharge_money;
	private int use_money;
	private int balance_money;



	public String getMember_phone() {
		return member_phone;
	}

	public void setMember_phone(String member_phone) {
		this.member_phone = member_phone;
	}

	public int getRecharge_money() {
		return recharge_money;
	}

	public void setRecharge_money(int recharge_money) {
		this.recharge_money = recharge_money;
	}

	public int getUse_money() {
		return use_money;
	}

	public void setUse_money(int use_money) {
		this.use_money = use_money;
	}

	public int getBalance_money() {
		return balance_money;
	}

	public void setBalance_money(int balance_money) {
		this.balance_money = balance_money;
	}
}
