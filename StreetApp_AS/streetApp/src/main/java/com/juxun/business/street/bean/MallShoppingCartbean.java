/**
 * 
 */
package com.juxun.business.street.bean;

import java.util.Date;

/**
 * 
 * 类名称：PaidActivity 类描述： 实收总金额 首页 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class MallShoppingCartbean {
	private String name;// 商品名称
	private int goods_count;// 商品数量
	private double total_price;// 总售价
	private double cost;// 商品单件成本
	private double zongjj;// 总进价
	private double zongly;// 总利润
	

	public double getZongjj() {
		zongjj = cost * goods_count;
		return zongjj;
	}

	public void setZongjj(double zongjj) {
		this.zongjj = zongjj;
	}

	public double getZongly() {
		zongly = total_price - (cost * goods_count);
		return zongly;
	}

	public void setZongly(double zongly) {
		this.zongly = zongly;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getGoods_count() {
		return goods_count;
	}

	public void setGoods_count(int goods_count) {
		this.goods_count = goods_count;
	}

}
