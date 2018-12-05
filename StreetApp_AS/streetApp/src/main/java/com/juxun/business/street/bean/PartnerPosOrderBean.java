package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备授权表
 * */
public class PartnerPosOrderBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String order_id;// 订单id
	private double order_price = 0.00;// 订单金额 单位元
	private int order_state = UNPAY;// 订单状态
	private String order_openid;// 订单支付的用户id 微信为openid 支付宝为支付宝账号
	private int order_pay_type = WXPAY;// 订单支付的类型 支付宝 微信 盒子支付
	private String order_nonce;// 微信订单的32位随机数
	private String createDate;// 订单创建日期
	private String order_end_date;// 支付完成时间
	private int order_agency;// 创建订单的合伙人机构id
	private int order_admin;// 创建订单的管理员id
	private String order_payorder_id;// 第三方订单id（支付宝或者微信订单号）

	// /------支付方式
	public static final int ALIPAY = 1;// 支付宝
	public static final int WXPAY = 2;// 微信支付
	public static final int UNIONPAY = 3;// 银联支付
	public static final int HEZHI = 4;// 盒子支付
	// ----订单状态
	public static final int SUCCESS = 1;// 付款成功
	public static final int ERROR = 2;// 付款失败
	public static final int UNPAY = 3;// 未付款
	public static final int REFUND = 4;// 退款

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getOrder_nonce() {
		return order_nonce;
	}

	public void setOrder_nonce(String order_nonce) {
		this.order_nonce = order_nonce;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getOrder_end_date() {
		return order_end_date;
	}

	public void setOrder_end_date(String order_end_date) {
		this.order_end_date = order_end_date;
	}

	public int getOrder_agency() {
		return order_agency;
	}

	public void setOrder_agency(int order_agency) {
		this.order_agency = order_agency;
	}

	public int getOrder_admin() {
		return order_admin;
	}

	public void setOrder_admin(int order_admin) {
		this.order_admin = order_admin;
	}

	public String getOrder_payorder_id() {
		return order_payorder_id;
	}

	public void setOrder_payorder_id(String order_payorder_id) {
		this.order_payorder_id = order_payorder_id;
	}

}
