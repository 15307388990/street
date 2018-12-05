package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 财务结算表
 *
 * @author hongliu
 */

public class SettleListBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id : 134
     * create_date : 1527320842000
     * deleted : false
     * settle_stauts : 0
     * settle_type : 1
     * settle_info :
     * order_id : 1527320828905426118
     * order_pay_id : 1527320829068151
     * out_order_id : 4200000115201805265776126023
     * settle_price : 3
     * settle_gold_price : 0
     * settle_redenvelopes_price : 1
     * settle_delivery_price : 1
     * settle_pay_price : 2
     * settle_order_price : 2
     * settle_coupon_price : 0
     * settle_date : 1527320842000
     * settle_end_date : null
     * agency_id : 2791
     * pay_type : 1
     * pay_source : 2
     * order_date : 1527320829000
     * appid : wx860aeeae21ff2a57
     * agency_name : 彪彪湘味小厨
     * spec : [{"commission":0.0,"commodityCost":1,"commodityICon":"478CC1A8-7972-4374-A45C-C74C7FD46673,A4FE4FDB-9747-4E07-A1B0-310EB99FF679","commodityName":"饭","first_channelId":359,"goodsCount":1,"id":44276,"price":2,"secode_channelId":0,"serviceCommission":0.0,"specIds":"","specNames":"","total_price":0.02}]
     */

    private int id;
    private long create_date;
    private boolean deleted;
    private int settle_stauts;
    private int settle_type;
    private String settle_info;
    private String order_id;
    private String order_pay_id;
    private String out_order_id;
    private int settle_price;
    private int settle_gold_price;
    private int settle_redenvelopes_price;
    private int settle_delivery_price;
    private int settle_pay_price;
    private int settle_order_price;
    private int settle_coupon_price;
    private long settle_date;
    private Object settle_end_date;
    private int agency_id;
    private int pay_type;
    private int pay_source;
    private long order_date;
    private String appid;
    private String agency_name;
    private String spec;

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

    public int getSettle_stauts() {
        return settle_stauts;
    }

    public void setSettle_stauts(int settle_stauts) {
        this.settle_stauts = settle_stauts;
    }

    public int getSettle_type() {
        return settle_type;
    }

    public void setSettle_type(int settle_type) {
        this.settle_type = settle_type;
    }

    public String getSettle_info() {
        return settle_info;
    }

    public void setSettle_info(String settle_info) {
        this.settle_info = settle_info;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_pay_id() {
        return order_pay_id;
    }

    public void setOrder_pay_id(String order_pay_id) {
        this.order_pay_id = order_pay_id;
    }

    public String getOut_order_id() {
        return out_order_id;
    }

    public void setOut_order_id(String out_order_id) {
        this.out_order_id = out_order_id;
    }

    public int getSettle_price() {
        return settle_price;
    }

    public void setSettle_price(int settle_price) {
        this.settle_price = settle_price;
    }

    public int getSettle_gold_price() {
        return settle_gold_price;
    }

    public void setSettle_gold_price(int settle_gold_price) {
        this.settle_gold_price = settle_gold_price;
    }

    public int getSettle_redenvelopes_price() {
        return settle_redenvelopes_price;
    }

    public void setSettle_redenvelopes_price(int settle_redenvelopes_price) {
        this.settle_redenvelopes_price = settle_redenvelopes_price;
    }

    public int getSettle_delivery_price() {
        return settle_delivery_price;
    }

    public void setSettle_delivery_price(int settle_delivery_price) {
        this.settle_delivery_price = settle_delivery_price;
    }

    public int getSettle_pay_price() {
        return settle_pay_price;
    }

    public void setSettle_pay_price(int settle_pay_price) {
        this.settle_pay_price = settle_pay_price;
    }

    public int getSettle_order_price() {
        return settle_order_price;
    }

    public void setSettle_order_price(int settle_order_price) {
        this.settle_order_price = settle_order_price;
    }

    public int getSettle_coupon_price() {
        return settle_coupon_price;
    }

    public void setSettle_coupon_price(int settle_coupon_price) {
        this.settle_coupon_price = settle_coupon_price;
    }

    public long getSettle_date() {
        return settle_date;
    }

    public void setSettle_date(long settle_date) {
        this.settle_date = settle_date;
    }

    public Object getSettle_end_date() {
        return settle_end_date;
    }

    public void setSettle_end_date(Object settle_end_date) {
        this.settle_end_date = settle_end_date;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getPay_source() {
        return pay_source;
    }

    public void setPay_source(int pay_source) {
        this.pay_source = pay_source;
    }

    public long getOrder_date() {
        return order_date;
    }

    public void setOrder_date(long order_date) {
        this.order_date = order_date;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
