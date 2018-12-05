package com.juxun.business.street.bean;

import java.io.Serializable;

public class StoreCenterBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 "id": "integer,主键id",
	 "name": "string,活动名称",
	 "start_time": "date,开始时间",
	 "end_time": "date,结束时间",
	 "state": "integer,1未开始2进行中3已结束4已暂停",
	 "rechargeTypeCount": "integer,储值种类",
	 "buyCount": "integer,购买张数"
	 */

	private int rechargeTypeCount;
	private long start_time;
	private int buyCount;
	private String name;
	private long end_time;
	private int id;
	private int state;

	public int getRechargeTypeCount() {
		return rechargeTypeCount;
	}

	public void setRechargeTypeCount(int rechargeTypeCount) {
		this.rechargeTypeCount = rechargeTypeCount;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
