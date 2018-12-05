package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * Created by wood121 on 2018/1/19.
 */

public class ConfirmOrderBean implements Serializable {
    /**
     * "id": 4,
     * "create_date": 1516354404564,
     * "deleted": false,
     * "pay_id": null,
     * "order_num": "1516354404564837422",
     * "provider_agency_id": 3,
     * "partner_agency_id": 13,
     * "delivery_name": "",
     * "express_num": "",
     * "consignee_name": "呵呵呵",
     * "consignee_phone": "15815579107",
     * "address": "36号",
     * "commodity_json": "[{\"code\":\"cc123\",\"first_level_channel_id\":1,\"totalPrice\":40,\"second_level_channel_id\":3,\"commodity_icon\":\"511dda38-4b17-44e3-86c0-5f5bf2a3a42c\",\"id\":1,\"price_high\":40,\"supplier_id\":1,\"price_low\":40,\"msg_count\":1,\"commodity_name\":\"苹果\"}]",
     * "total_price": 40,
     * "draw_redpacket_id": 0,
     * "redpacket_price": 0,
     * "delivery_type": 2,
     * "real_delivery_date": null,
     * "complete_date": null,
     * "close_date": null,
     * "order_state": 1,
     * "abolish_type": null,
     * "abolish": null,
     * "order_explain": null
     */

    private int id;
    private long create_date;
    private boolean deleted;
    private int pay_id;
    private String order_num;
    private int provider_agency_id;
    private int partner_agency_id;
    private String delivery_name;
    private int express_num;
    private String consignee_name;
    private String consignee_phone;
    private String address;
    private String commodity_json;
    private int total_price;
    private int draw_redpacket_id;
    private int redpacket_price;
    private int delivery_type;
    private String real_delivery_date;
    private String complete_date;
    private String close_date;
    private int order_state;
    private int abolish_type;
    private String abolish;
    private String order_explain;

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

    public int getPay_id() {
        return pay_id;
    }

    public void setPay_id(int pay_id) {
        this.pay_id = pay_id;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public int getProvider_agency_id() {
        return provider_agency_id;
    }

    public void setProvider_agency_id(int provider_agency_id) {
        this.provider_agency_id = provider_agency_id;
    }

    public int getPartner_agency_id() {
        return partner_agency_id;
    }

    public void setPartner_agency_id(int partner_agency_id) {
        this.partner_agency_id = partner_agency_id;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public int getExpress_num() {
        return express_num;
    }

    public void setExpress_num(int express_num) {
        this.express_num = express_num;
    }

    public String getConsignee_name() {
        return consignee_name;
    }

    public void setConsignee_name(String consignee_name) {
        this.consignee_name = consignee_name;
    }

    public String getConsignee_phone() {
        return consignee_phone;
    }

    public void setConsignee_phone(String consignee_phone) {
        this.consignee_phone = consignee_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommodity_json() {
        return commodity_json;
    }

    public void setCommodity_json(String commodity_json) {
        this.commodity_json = commodity_json;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getDraw_redpacket_id() {
        return draw_redpacket_id;
    }

    public void setDraw_redpacket_id(int draw_redpacket_id) {
        this.draw_redpacket_id = draw_redpacket_id;
    }

    public int getRedpacket_price() {
        return redpacket_price;
    }

    public void setRedpacket_price(int redpacket_price) {
        this.redpacket_price = redpacket_price;
    }

    public int getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(int delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getReal_delivery_date() {
        return real_delivery_date;
    }

    public void setReal_delivery_date(String real_delivery_date) {
        this.real_delivery_date = real_delivery_date;
    }

    public String getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(String complete_date) {
        this.complete_date = complete_date;
    }

    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(String close_date) {
        this.close_date = close_date;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public int getAbolish_type() {
        return abolish_type;
    }

    public void setAbolish_type(int abolish_type) {
        this.abolish_type = abolish_type;
    }

    public String getAbolish() {
        return abolish;
    }

    public void setAbolish(String abolish) {
        this.abolish = abolish;
    }

    public String getOrder_explain() {
        return order_explain;
    }

    public void setOrder_explain(String order_explain) {
        this.order_explain = order_explain;
    }
}
