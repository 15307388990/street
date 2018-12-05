package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 
 * @author Lifucheng
 * 团购订单bean
 *
 */
public class QuanTeamBuyOrderBean implements Serializable{
	private long order_id;//订单id
	private int order_num;//订单数量
	private String order_price;//订单价格
	private String order_name;//订单名称
	private String order_icon;//订单图片
	private int order_state;//订单状态：1，成功；2，付款失败；3，未付款；4，已退款
	private int pay_type;//支付方式 1：支付宝 2微信支付 3银联支付 4线下支付宝支付 5线下微信支付
	private String order_creat_date;
	private int stauts;
	private String msg;
	public QuanTeamBuyOrderBean(){}
	public QuanTeamBuyOrderBean(long order_id, int order_num,
			String order_price, String order_name, String order_icon,
			int order_state, int pay_type, String order_creat_date, int stauts,
			String msg) {
		super();
		this.order_id = order_id;
		this.order_num = order_num;
		this.order_price = order_price;
		this.order_name = order_name;
		this.order_icon = order_icon;
		this.order_state = order_state;
		this.pay_type = pay_type;
		this.order_creat_date = order_creat_date;
		this.stauts = stauts;
		this.msg = msg;
	}
	public long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}
	public int getOrder_num() {
		return order_num;
	}
	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}
	public String getOrder_price() {
		return order_price;
	}
	public void setOrder_price(String order_price) {
		this.order_price = order_price;
	}
	public String getOrder_name() {
		return order_name;
	}
	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}
	public String getOrder_icon() {
		return order_icon;
	}
	public void setOrder_icon(String order_icon) {
		this.order_icon = order_icon;
	}
	public int getOrder_state() {
		return order_state;
	}
	public void setOrder_state(int order_state) {
		this.order_state = order_state;
	}
	public int getPay_type() {
		return pay_type;
	}
	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}
	public String getOrder_creat_date() {
		return order_creat_date;
	}
	public void setOrder_creat_date(String order_creat_date) {
		this.order_creat_date = order_creat_date;
	}
	public int getStauts() {
		return stauts;
	}
	public void setStauts(int stauts) {
		this.stauts = stauts;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
