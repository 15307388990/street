package com.juxun.business.street.bean;

import java.io.Serializable;

public class TheDetailBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String member_phone;//
	private int type;// 0充值 1消费
	private String tran_no;// 单号
	private int money;// 充值或消费了多少
	private int balance;// 最后的余额"
	private long create_date;// 时间
	private long createDate;// 时间
	private int recharge_money;
	private int give_money;

	public long getCreate_date() {
		return create_date;
	}
	public long getCreateDate() {
		return createDate;
	}

	public void setCreate_date(long create_date) {
		this.create_date = create_date;
	}
	public String getMember_phone() {
		return member_phone;
	}

	public void setMember_phone(String member_phone) {
		this.member_phone = member_phone;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTran_no() {
		return tran_no;
	}

	public void setTran_no(String tran_no) {
		this.tran_no = tran_no;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getRecharge_money() {
		return recharge_money;
	}

	public void setRecharge_money(int recharge_money) {
		this.recharge_money = recharge_money;
	}

	public int getGive_money() {
		return give_money;
	}

	public void setGive_money(int give_money) {
		this.give_money = give_money;
	}
}
