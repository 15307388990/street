package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.Date;

/***
 * 提现记录表
 * 
 * @author hongliu
 * 
 */
public class FinanceWithdrawBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;// id
	private int withdraw_price;// 提现金额
	private String withdraw_access;// 提现账号
	private String withdraw_name;// 提现账号对应的姓名
	private int withdraw_type; // 提现是方式 0 支付宝 1、银行卡
	private String withdraw_type_info;// 当提现方式为银行卡时显示开户行
	private int withdraw_stauts;//  1； 提现中 2；提现成功 -1提现失败",
	private String withdraw_leave_message;// 提现人留言
	private String withdraw_info;// 平台备注
	private String withdraw_number;// 转账流水号
	private int agency_id;// 机构id
	private String withdraw_date;//提现申请时间
	private String withdraw_end_date;//提现完成时间
	private int admin_id;//确认完成提现的操作人

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWithdraw_price() {
		return withdraw_price;
	}

	public void setWithdraw_price(int withdraw_price) {
		this.withdraw_price = withdraw_price;
	}

	public String getWithdraw_access() {
		return withdraw_access;
	}

	public void setWithdraw_access(String withdraw_access) {
		this.withdraw_access = withdraw_access;
	}

	public String getWithdraw_name() {
		return withdraw_name;
	}

	public void setWithdraw_name(String withdraw_name) {
		this.withdraw_name = withdraw_name;
	}

	public int getWithdraw_type() {
		return withdraw_type;
	}

	public void setWithdraw_type(int withdraw_type) {
		this.withdraw_type = withdraw_type;
	}

	public String getWithdraw_type_info() {
		return withdraw_type_info;
	}

	public void setWithdraw_type_info(String withdraw_type_info) {
		this.withdraw_type_info = withdraw_type_info;
	}

	public int getWithdraw_stauts() {
		return withdraw_stauts;
	}

	public void setWithdraw_stauts(int withdraw_stauts) {
		this.withdraw_stauts = withdraw_stauts;
	}

	public String getWithdraw_leave_message() {
		return withdraw_leave_message;
	}

	public void setWithdraw_leave_message(String withdraw_leave_message) {
		this.withdraw_leave_message = withdraw_leave_message;
	}

	public String getWithdraw_info() {
		return withdraw_info;
	}

	public void setWithdraw_info(String withdraw_info) {
		this.withdraw_info = withdraw_info;
	}

	public String getWithdraw_number() {
		return withdraw_number;
	}

	public void setWithdraw_number(String withdraw_number) {
		this.withdraw_number = withdraw_number;
	}

	public int getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(int agency_id) {
		this.agency_id = agency_id;
	}


	public int getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(int admin_id) {
		this.admin_id = admin_id;
	}

	public String getWithdraw_date() {
		return withdraw_date;
	}

	public void setWithdraw_date(String withdraw_date) {
		this.withdraw_date = withdraw_date;
	}

	public String getWithdraw_end_date() {
		return withdraw_end_date;
	}

	public void setWithdraw_end_date(String withdraw_end_date) {
		this.withdraw_end_date = withdraw_end_date;
	}

}
