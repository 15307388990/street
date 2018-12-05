package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author pos 订单 bean 
 *
 */
public class PosOrderBean implements Serializable{
	
	private String order_id;//订单ID
	private double order_price;//订单总额
	private int order_state;//订单支付状态  1：付款成功 2：付款失败 3：未付款
	private String order_openid;//微信openid或者为支付宝支付账号
	private int order_pay_type;//订单支付方式1：支付宝  2:微信 3：银联支付
	private String order_creat_date;//订单创建日期
	private String order_end_date;//订单付款时间
	private int order_shop;//创建订单的商户id
	private String order_payorder_id;//支付宝或微信的订单（无需显示）
	private String order_nonce;//订单随机数
	
	public PosOrderBean() {
		super();
		
	}

	public PosOrderBean(String order_id, double order_price, int order_state,
			String order_openid, int order_pay_type, String order_creat_date,
			String order_end_date, int order_shop, String order_payorder_id,
			String order_nonce) {
		super();
		this.order_id = order_id;
		this.order_price = order_price;
		this.order_state = order_state;
		this.order_openid = order_openid;
		this.order_pay_type = order_pay_type;
		this.order_creat_date = order_creat_date;
		this.order_end_date = order_end_date;
		this.order_shop = order_shop;
		this.order_payorder_id = order_payorder_id;
		this.order_nonce = order_nonce;
	}

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

	public String getOrder_nonce() {
		return order_nonce;
	}

	public void setOrder_nonce(String order_nonce) {
		this.order_nonce = order_nonce;
	}

	
	

}
