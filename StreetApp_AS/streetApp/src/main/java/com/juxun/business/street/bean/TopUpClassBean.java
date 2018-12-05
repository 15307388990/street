package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 充值活动类型
 */
public class TopUpClassBean implements Serializable {
    private static final long serialVersionUID = 1L;


    private int id;// 主键
    private long create_date;
    private boolean deleted;
    private int recharge_price; //充值金额
    private int recharge_activity_id;
    private int largess_price;  //赠送金额
    private double discount;
    private int item_type;  //充值项目类型 \t * 0、默认充值项目\t  \t * 1.充值送现金 \t * 2.充值折扣 \t * 3.充值送红包",

    private int redpacket_price;// 红包面值

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

    public int getRecharge_activity_id() {
        return recharge_activity_id;
    }

    public void setRecharge_activity_id(int recharge_activity_id) {
        this.recharge_activity_id = recharge_activity_id;
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

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public int getRedpacket_price() {
        return redpacket_price;
    }

    public void setRedpacket_price(int redpacket_price) {
        this.redpacket_price = redpacket_price;
    }
}
