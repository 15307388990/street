package com.juxun.business.street.bean;

import java.io.Serializable;

public class StoreStaBean implements Serializable {
    /**
     * "id":4, "createDate":1509431551000, "deleted":false, "agency_id":397,
     * "activity_id":11, "denomination_id":15, "member_id":3,
     * "member_phone":"133", "type":0, "tran_no":"dsa", "money":1, "balance":1
     * recharge_money": 20000,
     * "give_money": 4000
     */

    private int id;
    private long create_date;
    private boolean deleted;
    private int agency_id;
    private int activity_id;
    private int denomination_id;
    private int member_id;
    private String member_phone;
    private int type; // 0充值，1消费
    private String tran_no; // 单号
    private int money; // 充值或消费了多少
    private int balance; // 最后的余额
    private int recharge_money;    //充值
    private int give_money;    //送

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getDenomination_id() {
        return denomination_id;
    }

    public void setDenomination_id(int denomination_id) {
        this.denomination_id = denomination_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_phone() {
        return member_phone;
    }

    public void setMember_phone(String member_phone) {
        this.member_phone = member_phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTran_no() {
        return tran_no;
    }

    public void setTran_no(String tran_no) {
        this.tran_no = tran_no;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getRecharge_money() {
        return recharge_money;
    }

    public void setRecharge_money(int recharge_money) {
        this.recharge_money = recharge_money;
    }

    public int getGive_money() {
        return give_money;
    }

    public void setGive_money(int give_money) {
        this.give_money = give_money;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

}
