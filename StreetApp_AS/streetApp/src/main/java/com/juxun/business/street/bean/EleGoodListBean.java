package com.juxun.business.street.bean;

import java.io.Serializable;

public class EleGoodListBean implements Serializable{
	
//	private int id;/主键ID
	private int msg_id;//商品ID
	private String displaySpecOrCategory;//规格
	private String price;//单价
	private String total_price;//总价
	private int goods_count;//数量
	
	private String cover;//商品封面
	private String name;//商品名称
	public EleGoodListBean() {
		super();
	}
	public EleGoodListBean(int msg_id, String displaySpecOrCategory,
			String price, String total_price, int goods_count, String cover,
			String name) {
		super();
		this.msg_id = msg_id;
		this.displaySpecOrCategory = displaySpecOrCategory;
		this.price = price;
		this.total_price = total_price;
		this.goods_count = goods_count;
		this.cover = cover;
		this.name = name;
	}
	public int getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(int msg_id) {
		this.msg_id = msg_id;
	}
	public String getDisplaySpecOrCategory() {
		return displaySpecOrCategory;
	}
	public void setDisplaySpecOrCategory(String displaySpecOrCategory) {
		this.displaySpecOrCategory = displaySpecOrCategory;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	public int getGoods_count() {
		return goods_count;
	}
	public void setGoods_count(int goods_count) {
		this.goods_count = goods_count;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
