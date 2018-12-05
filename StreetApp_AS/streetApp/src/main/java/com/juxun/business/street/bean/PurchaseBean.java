package com.juxun.business.street.bean;


import java.io.Serializable;
import java.util.List;

public class PurchaseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 首页商品列表
	 */
	private int id;// 上架商品id
	private double priceHigh;// 最高价
	private String commodityICon;// 商品图片
	private String commodityName;// 商品名称
	private double priceLow;// 最低价

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPriceHigh() {
		return priceHigh;
	}
	public void setPriceHigh(double priceHigh) {
		this.priceHigh = priceHigh;
	}
	public String getCommodityICon() {
		return commodityICon;
	}
	public void setCommodityICon(String commodityICon) {
		this.commodityICon = commodityICon;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public double getPriceLow() {
		return priceLow;
	}
	public void setPriceLow(double priceLow) {
		this.priceLow = priceLow;
	}


}
