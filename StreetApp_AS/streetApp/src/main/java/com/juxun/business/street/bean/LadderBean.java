package com.juxun.business.street.bean;


import java.io.Serializable;
import java.util.List;

public class LadderBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * ladderPrice": "double,阶梯价格", // "ladderCount": "integer,阶梯数量"
	 */
	private double ladderPrice;// 阶梯价格
	private int ladderCount;// 阶梯数量

	public double getLadderPrice() {
		return ladderPrice;
	}

	public void setLadderPrice(double ladderPrice) {
		this.ladderPrice = ladderPrice;
	}

	public int getLadderCount() {
		return ladderCount;
	}

	public void setLadderCount(int ladderCount) {
		this.ladderCount = ladderCount;
	}

}
