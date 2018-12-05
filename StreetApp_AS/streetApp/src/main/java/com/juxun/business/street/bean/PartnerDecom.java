package com.juxun.business.street.bean;

/**
 * 合伙人银行卡信息
 */
public class PartnerDecom {

	private String account_name;// 持卡人真实姓名
	private String account_card; // 持卡人卡号/支付宝账号
	private String account_bank; // 开户行 为银行卡时必填
	private String bank_phone; // 银行预留手机号
	private String account_bank_branch; // 支行名称
	private String account_bank_branch_code; // 支行，行号

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getAccount_card() {
		return account_card;
	}

	public void setAccount_card(String account_card) {
		this.account_card = account_card;
	}

	public String getAccount_bank() {
		return account_bank;
	}

	public void setAccount_bank(String account_bank) {
		this.account_bank = account_bank;
	}

	public String getBank_phone() {
		return bank_phone;
	}

	public void setBank_phone(String bank_phone) {
		this.bank_phone = bank_phone;
	}

	public String getAccount_bank_branch() {
		return account_bank_branch;
	}

	public void setAccount_bank_branch(String account_bank_branch) {
		this.account_bank_branch = account_bank_branch;
	}

	public String getAccount_bank_branch_code() {
		return account_bank_branch_code;
	}

	public void setAccount_bank_branch_code(String account_bank_branch_code) {
		this.account_bank_branch_code = account_bank_branch_code;
	}


}
