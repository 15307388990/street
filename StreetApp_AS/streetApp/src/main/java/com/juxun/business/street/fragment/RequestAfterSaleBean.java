package com.juxun.business.street.fragment;

import java.io.Serializable;

public class RequestAfterSaleBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /*
     * "id": "integer,订单商品主键id",\ "commodity_icon": "string,商品图片名称",
     * "commodity_name": "string,商品名称", "unit_price": "integer,商品单价",
     * "purchase_quantity": "integer,购买数量", "refund_commodity_num":
     * "integer,已退款数量", "supplier_order_id": "integer,订单主键id"
     */
    private int id;
    private String commodity_icon;
    private String commodity_name;
    private int unit_price;
    private int purchase_quantity;
    private int refund_commodity_num;
    private int supplier_order_id;
    private double beanMoney; // 每个条目操作的退款总额
    private int refund_goods; // 每个条目操作的商品数量
    private int supplier_order_commodity_id;

    public int getSupplier_order_commodity_id() {
        return supplier_order_commodity_id;
    }

    public void setSupplier_order_commodity_id(int supplier_order_commodity_id) {
        this.supplier_order_commodity_id = supplier_order_commodity_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommodity_icon() {
        return commodity_icon;
    }

    public void setCommodity_icon(String commodity_icon) {
        this.commodity_icon = commodity_icon;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public int getPurchase_quantity() {
        return purchase_quantity;
    }

    public void setPurchase_quantity(int purchase_quantity) {
        this.purchase_quantity = purchase_quantity;
    }

    public int getRefund_commodity_num() {
        return refund_commodity_num;
    }

    public void setRefund_commodity_num(int refund_commodity_num) {
        this.refund_commodity_num = refund_commodity_num;
    }

    public int getSupplier_order_id() {
        return supplier_order_id;
    }

    public void setSupplier_order_id(int supplier_order_id) {
        this.supplier_order_id = supplier_order_id;
    }

    public int getRefund_goods() {
        return refund_goods;
    }

    public void setRefund_goods(int refund_goods) {
        this.refund_goods = refund_goods;
    }

    public double getBeanMoney() {
        return beanMoney;
    }

    public void setBeanMoney(double beanMoney) {
        this.beanMoney = beanMoney;
    }

}
