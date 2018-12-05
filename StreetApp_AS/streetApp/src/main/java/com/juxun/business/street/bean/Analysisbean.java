/**
 * 
 */
package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 类名称：PaidActivity 类描述： 实收总金额 首页 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class Analysisbean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double order_price;// 营业额
	private double order_cost;// 订单成本
	private double order_profit;// 订单利润
	private Date order_date;// 时间
	private String order_id;// 订单id

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public double getOrder_price() {
		return order_price;
	}

	public void setOrder_price(double order_price) {
		this.order_price = order_price;
	}

	public double getOrder_cost() {
		return order_cost;
	}

	public void setOrder_cost(double order_cost) {
		this.order_cost = order_cost;
	}

	public double getOrder_profit() {
		return order_profit;
	}

	public void setOrder_profit(double order_profit) {
		this.order_profit = order_profit;
	}

	public Date getOrder_date() {
		return order_date;
	}

	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}

}
