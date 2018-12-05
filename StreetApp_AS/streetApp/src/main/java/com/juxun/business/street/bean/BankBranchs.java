/**
 *
 */
package com.juxun.business.street.bean;

/**
 * 银行支行实体类
 */
public class BankBranchs {
    /**
     * "id": 33093,
     * "create_date": 1515413209000,
     * "deleted": false,
     * "parent_bank_no": "104100000004",
     * "parent_bank_name": "中国银行",
     * "branch_bank_no": "104223021341",
     * "branch_bank_name": "中国银行股份有限公司鞍山分行解放路支行",
     * "city_code": "2230",
     * "branch_bank_address": ""
     */

    int id;
    long create_date;
    boolean deleted;
    String parent_bank_no;
    String parent_bank_name;
    String branch_bank_no;
    String branch_bank_name;
    String city_code;
    String branch_bank_address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getParent_bank_no() {
        return parent_bank_no;
    }

    public void setParent_bank_no(String parent_bank_no) {
        this.parent_bank_no = parent_bank_no;
    }

    public String getParent_bank_name() {
        return parent_bank_name;
    }

    public void setParent_bank_name(String parent_bank_name) {
        this.parent_bank_name = parent_bank_name;
    }

    public String getBranch_bank_no() {
        return branch_bank_no;
    }

    public void setBranch_bank_no(String branch_bank_no) {
        this.branch_bank_no = branch_bank_no;
    }

    public String getBranch_bank_name() {
        return branch_bank_name;
    }

    public void setBranch_bank_name(String branch_bank_name) {
        this.branch_bank_name = branch_bank_name;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getBranch_bank_address() {
        return branch_bank_address;
    }

    public void setBranch_bank_address(String branch_bank_address) {
        this.branch_bank_address = branch_bank_address;
    }
}
