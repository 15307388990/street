package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 充值记录bean
 */
public class TopUpBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * "id": 47,
     * "create_date": 1516605387000,
     * "deleted": false,
     * "recharge_price": 1,
     * "largess_price": 0,
     * "discount": 0.0,
     * "pay_price": 1,
     * "recharge_type": 0,
     * "recharge_time": null,
     * "recharge_remark": null,
     * "order_num": "1516605386979535055",
     * "out_order_num": null,
     * "agency_id": 13,
     * "recharge_state": 0,
     * "admin_account": "863222679@qq.com",
     * "recharge_activity_id": 0,
     * "pay_type": 2,
     * "recharge_source": 1,
     * "nonceStr": "wLEF7qb32mg2T4OHLIezStYriC5HEM"
     */

    private int id;
    private long create_date;
    private boolean deleted;
    private int recharge_price; //冲多少
    private int largess_price;  //送多少
    private double discount;
    private int pay_price;  //支付金额
    private int recharge_type;
    private long recharge_time;
    private String recharge_remark;
    private String order_num;
    private String out_order_num;
    private int agency_id;
    private int recharge_state;
    private String admin_account;
    private int recharge_activity_id;
    private int pay_type;
    private int recharge_source;
    private String nonceStr;

    private int recharge_souce; //1 2 余额充值 3采购订单退款 4采购订单支付

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

    public int getRecharge_price() {
        return recharge_price;
    }

    public void setRecharge_price(int recharge_price) {
        this.recharge_price = recharge_price;
    }

    public int getLargess_price() {
        return largess_price;
    }

    public void setLargess_price(int largess_price) {
        this.largess_price = largess_price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getPay_price() {
        return pay_price;
    }

    public void setPay_price(int pay_price) {
        this.pay_price = pay_price;
    }

    public int getRecharge_type() {
        return recharge_type;
    }

    public void setRecharge_type(int recharge_type) {
        this.recharge_type = recharge_type;
    }

    public long getRecharge_time() {
        return recharge_time;
    }

    public void setRecharge_time(long recharge_time) {
        this.recharge_time = recharge_time;
    }

    public String getRecharge_remark() {
        return recharge_remark;
    }

    public void setRecharge_remark(String recharge_remark) {
        this.recharge_remark = recharge_remark;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getOut_order_num() {
        return out_order_num;
    }

    public void setOut_order_num(String out_order_num) {
        this.out_order_num = out_order_num;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getRecharge_state() {
        return recharge_state;
    }

    public void setRecharge_state(int recharge_state) {
        this.recharge_state = recharge_state;
    }

    public String getAdmin_account() {
        return admin_account;
    }

    public void setAdmin_account(String admin_account) {
        this.admin_account = admin_account;
    }

    public int getRecharge_activity_id() {
        return recharge_activity_id;
    }

    public void setRecharge_activity_id(int recharge_activity_id) {
        this.recharge_activity_id = recharge_activity_id;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getRecharge_source() {
        return recharge_source;
    }

    public void setRecharge_source(int recharge_source) {
        this.recharge_source = recharge_source;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public int getRecharge_souce() {
        return recharge_souce;
    }

    public void setRecharge_souce(int recharge_souce) {
        this.recharge_souce = recharge_souce;
    }
}