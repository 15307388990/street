package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * Created by wood121 on 2018/1/8.
 * 供应链商品列表对象、详情对象
 */

public class SupplierGoodsBean implements Serializable {
    /**
     * "id": 1,
     * "create_date": 1513238087000,
     * "deleted": false,
     * "commodity_name": "苹果",
     * "spec_type": 1,
     * "spec_ids": "",
     * "spec_value_ids": "",
     * "spec": "",
     * "first_level_channel_id": 1,
     * "second_level_channel_id": 3,
     * "commodity_cost": 35,
     * "price_low": 40,
     * "price_high": 0,
     * "code_type": 0,
     * "code": "cc123",
     * "commodity_icon": "511dda38-4b17-44e3-86c0-5f5bf2a3a42c",
     * "commodity_text": "511dda38-4b17-44e3-86c0-5f5bf2a3a42c",
     * "commodity_desc": "511dda38-4b17-44e3-86c0-5f5bf2a3a42c",
     * "agency_id": 3,
     * "unit_name": "个",
     * "update_date": null,
     * "supplier_id": 1,
     * "retail_price": 10,
     * "commodity_inventory": 100,
     * "commodity_sales": 2,
     * "vitual_sales": 1,
     * "commodity_state": 2,
     * "shelves_status": 4
     */

    private int commodity_sales;

    private int id;
    private long create_date;
    private boolean deleted;
    private String commodity_name;
    private int spec_type;
    private String spec_ids;
    private String spec_value_ids;
    private String spec;
    private int first_level_channel_id;
    private int second_level_channel_id;
    private String commodity_cost;
    private int price_low;
    private int price_high;
    private int code_type;  //string,0有条码1无条码
    private String code;    //string,条码
    private String commodity_icon;  //string, 图片集，以逗号隔开
    private String commodity_text;  //string,富文本
    private String commodity_desc;  //string,描述
    private String agency_id;
    private String unit_name;
    private long update_date;
    private int supplier_id;
    private int retail_price;
    private int commodity_inventory;
    private int vitual_sales;   //string,虚拟销量
    private int commodity_state;    //string,商品状态 1.审核中 2.审核通过 3.审核失败 4.强制下架"
    private int shelves_status;
    private String supplier_name;
    private String agency_name;
    private String first_channel_name;
    private String second_channel_name;

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

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public int getSpec_type() {
        return spec_type;
    }

    public void setSpec_type(int spec_type) {
        this.spec_type = spec_type;
    }

    public String getSpec_ids() {
        return spec_ids;
    }

    public void setSpec_ids(String spec_ids) {
        this.spec_ids = spec_ids;
    }

    public String getSpec_value_ids() {
        return spec_value_ids;
    }

    public void setSpec_value_ids(String spec_value_ids) {
        this.spec_value_ids = spec_value_ids;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public int getFirst_level_channel_id() {
        return first_level_channel_id;
    }

    public void setFirst_level_channel_id(int first_level_channel_id) {
        this.first_level_channel_id = first_level_channel_id;
    }

    public int getSecond_level_channel_id() {
        return second_level_channel_id;
    }

    public void setSecond_level_channel_id(int second_level_channel_id) {
        this.second_level_channel_id = second_level_channel_id;
    }

    public String getCommodity_cost() {
        return commodity_cost;
    }

    public void setCommodity_cost(String commodity_cost) {
        this.commodity_cost = commodity_cost;
    }

    public int getPrice_low() {
        return price_low;
    }

    public void setPrice_low(int price_low) {
        this.price_low = price_low;
    }

    public int getPrice_high() {
        return price_high;
    }

    public void setPrice_high(int price_high) {
        this.price_high = price_high;
    }

    public int getCode_type() {
        return code_type;
    }

    public void setCode_type(int code_type) {
        this.code_type = code_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCommodity_icon() {
        return commodity_icon;
    }

    public void setCommodity_icon(String commodity_icon) {
        this.commodity_icon = commodity_icon;
    }

    public String getCommodity_text() {
        return commodity_text;
    }

    public void setCommodity_text(String commodity_text) {
        this.commodity_text = commodity_text;
    }

    public String getCommodity_desc() {
        return commodity_desc;
    }

    public void setCommodity_desc(String commodity_desc) {
        this.commodity_desc = commodity_desc;
    }

    public String getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(String agency_id) {
        this.agency_id = agency_id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public long getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(long update_date) {
        this.update_date = update_date;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(int retail_price) {
        this.retail_price = retail_price;
    }

    public int getCommodity_inventory() {
        return commodity_inventory;
    }

    public void setCommodity_inventory(int commodity_inventory) {
        this.commodity_inventory = commodity_inventory;
    }

    public int getVitual_sales() {
        return vitual_sales;
    }

    public void setVitual_sales(int vitual_sales) {
        this.vitual_sales = vitual_sales;
    }

    public int getCommodity_state() {
        return commodity_state;
    }

    public void setCommodity_state(int commodity_state) {
        this.commodity_state = commodity_state;
    }

    public int getShelves_status() {
        return shelves_status;
    }

    public void setShelves_status(int shelves_status) {
        this.shelves_status = shelves_status;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getFirst_channel_name() {
        return first_channel_name;
    }

    public void setFirst_channel_name(String first_channel_name) {
        this.first_channel_name = first_channel_name;
    }

    public String getSecond_channel_name() {
        return second_channel_name;
    }

    public void setSecond_channel_name(String second_channel_name) {
        this.second_channel_name = second_channel_name;
    }

    public int getCommodity_sales() {
        return commodity_sales;
    }

    public void setCommodity_sales(int commodity_sales) {
        this.commodity_sales = commodity_sales;
    }
}
