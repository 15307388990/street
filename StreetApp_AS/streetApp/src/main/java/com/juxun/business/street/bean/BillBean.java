/**
 * 
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**   
 *    
 * 项目名称：Street   
 * 类名称：BillBean   
 * 类描述：   账单bean
 * 创建人：WuJianhua   
 * 创建时间：2015年6月2日 上午11:58:48   
 * 修改人：WuJianhua   
 * 修改时间：2015年6月2日 上午11:58:48   
 * 修改备注：   
 * @version    
 *    
 */
@SuppressWarnings("serial")
public class BillBean implements Serializable{
	/** 当月总额 */
	private double monthCountMoney;
	/** 当前月份*/
	private int month;
	/** 订单ID */
	private long order_id;
	/** 订单金额 */
	private double order_price;
	/** 订单支付状态 1：付款成功 2：付款失败 3：未付款 */
	private int order_state;
	/** 微信openid或者为支付宝支付账号 */
	private String order_openid;
	/** 订单支付方式 1：支付宝  2:微信 3：银联支付 */
	private int order_pay_type;
	/** 订单创建日期 */
	private String order_creat_date;
	/** 订单付款时间 */
	private String order_end_date;
	/** 创建订单的商户id */
	private int order_shop;
	/**支付宝或微信的订单（无需显示）*/
	private String order_payorder_id;
	
	/* 商城订单不一样的参数 */
	private int status;
	private String orderCreateTime;
	private String total_price;
	
	public BillBean() {
		super();
	}
	public BillBean(double monthCountMoney, int month, long order_id,
			double order_price, int order_state, String order_openid,
			int order_pay_type, String order_creat_date, String order_end_date,
			int order_shop, String order_payorder_id) {
		super();
		this.monthCountMoney = monthCountMoney;
		this.month = month;
		this.order_id = order_id;
		this.order_price = order_price;
		this.order_state = order_state;
		this.order_openid = order_openid;
		this.order_pay_type = order_pay_type;
		this.order_creat_date = order_creat_date;
		this.order_end_date = order_end_date;
		this.order_shop = order_shop;
		this.order_payorder_id = order_payorder_id;
	}
	public double getMonthCountMoney() {
		return monthCountMoney;
	}
	public void setMonthCountMoney(double monthCountMoney) {
		this.monthCountMoney = monthCountMoney;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}
	public double getOrder_price() {
		return order_price;
	}
	public void setOrder_price(double order_price) {
		this.order_price = order_price;
	}
	public int getOrder_state() {
		return order_state;
	}
	public void setOrder_state(int order_state) {
		this.order_state = order_state;
	}
	public String getOrder_openid() {
		return order_openid;
	}
	public void setOrder_openid(String order_openid) {
		this.order_openid = order_openid;
	}
	public int getOrder_pay_type() {
		return order_pay_type;
	}
	public void setOrder_pay_type(int order_pay_type) {
		this.order_pay_type = order_pay_type;
	}
	public String getOrder_creat_date() {
		return order_creat_date;
	}
	public void setOrder_creat_date(String order_creat_date) {
		this.order_creat_date = order_creat_date;
	}
	public String getOrder_end_date() {
		return order_end_date;
	}
	public void setOrder_end_date(String order_end_date) {
		this.order_end_date = order_end_date;
	}
	public int getOrder_shop() {
		return order_shop;
	}
	public void setOrder_shop(int order_shop) {
		this.order_shop = order_shop;
	}
	public String getOrder_payorder_id() {
		return order_payorder_id;
	}
	public void setOrder_payorder_id(String order_payorder_id) {
		this.order_payorder_id = order_payorder_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	
}
