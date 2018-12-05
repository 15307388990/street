package com.juxun.business.street.fragment;

import java.io.Serializable;

public class ToRefundReasonBean implements Serializable {

    private int supplier_order_commodity_id;
    private int refund_commodity_num;
    private String commodity_icon;
    private int unit_price;
    private  String commodity_name;

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public int getSupplier_order_commodity_id() {
        return supplier_order_commodity_id;
    }

    public void setSupplier_order_commodity_id(int supplier_order_commodity_id) {
        this.supplier_order_commodity_id = supplier_order_commodity_id;
    }

    public int getRefund_commodity_num() {
        return refund_commodity_num;
    }

    public void setRefund_commodity_num(int refund_commodity_num) {
        this.refund_commodity_num = refund_commodity_num;
    }

    public String getCommodity_icon() {
        return commodity_icon;
    }

    public void setCommodity_icon(String commodity_icon) {
        this.commodity_icon = commodity_icon;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }
}
