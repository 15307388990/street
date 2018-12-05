/**
 *
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * @version 账户列表 实体
 */
public class CarshAccountBean implements Serializable {
    /**
     * "agency_id": "string, 机构id",
     * "account_type": "string,账号类型 1支付宝，2银行卡",
     * "id": "string",
     * "account_name": "string,持卡人真实姓名",
     * "account_card": "string,持卡人卡号/支付宝账号",
     * "account_bank": "string, 开户行 为银行卡时必填",
     * "account_bank_branch": "string, 开户行的支行",
     * "account_bank_branch_code": "string,支行行号",
     * "account_bank_address": "string,开户行地址"
     */
    private static final long serialVersionUID = 1L;

    private int agency_id;
    private int account_type;   //1
    private int id;
    private String account_name;
    private String account_card;
    private String account_bank;
    private String account_bank_branch;
    private String account_bank_branch_code;
    private String account_bank_address;

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getAccount_bank_address() {
        return account_bank_address;
    }

    public void setAccount_bank_address(String account_bank_address) {
        this.account_bank_address = account_bank_address;
    }
}
