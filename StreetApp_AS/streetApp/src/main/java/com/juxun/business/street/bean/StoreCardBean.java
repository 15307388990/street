package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.List;

public class StoreCardBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 储值卡Bean
	 */
	private int id;
	private String createDate;
	private boolean deleted;
	private int activity_id;
	private int recharge_money;
	private int give_money;
	private int selledCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
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

	public int getSelledCount() {
		return selledCount;
	}

	public void setSelledCount(int selledCount) {
		this.selledCount = selledCount;
	}

}
