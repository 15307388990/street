/**
 * 
 */
package com.juxun.business.street.bean;

/**
 * 
 * 类名称：PaidActivity 类描述： 实收总金额 首页 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class AgencyFinancebean {
	private int id;// id
	private double agency_id;// 机构id
	private String withdrawal_name;// 提款人姓名
	private String withdrawal_alipay;// 支付宝账号 银行卡号和支付宝号2者至少填写一个否则会导致提款失败
	private String withdrawal_card;// 银行卡卡号
	private String withdrawal_bank_name;// 当提款账号开户行

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(double agency_id) {
		this.agency_id = agency_id;
	}

	public String getWithdrawal_name() {
		return withdrawal_name;
	}

	public void setWithdrawal_name(String withdrawal_name) {
		this.withdrawal_name = withdrawal_name;
	}

	public String getWithdrawal_alipay() {
		return withdrawal_alipay;
	}

	public void setWithdrawal_alipay(String withdrawal_alipay) {
		this.withdrawal_alipay = withdrawal_alipay;
	}

	public String getWithdrawal_card() {
		return withdrawal_card;
	}

	public void setWithdrawal_card(String withdrawal_card) {
		this.withdrawal_card = withdrawal_card;
	}

	public String getWithdrawal_bank_name() {
		return withdrawal_bank_name;
	}

	public void setWithdrawal_bank_name(String withdrawal_bank_name) {
		this.withdrawal_bank_name = withdrawal_bank_name;
	}
}
