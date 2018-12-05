package com.juxun.business.street.bean;


import java.io.Serializable;


public class ShopingBean implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 订单列表中 商品的信息
     * [{"code":"cc123",
     * "first_level_channel_id":1,
     * "totalPrice":40,
     * "second_level_channel_id":3,
     * "commodity_icon":"511dda38-4b17-44e3-86c0-5f5bf2a3a42c",
     * "id":1,
     * "price_high":40,
     * "supplier_id":1,
     * "price_low":40,
     * "msg_count":1,
     * "commodity_name":"苹果"}]
     */

    /**
     * 购物车列表中的商品信息
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
     * "update_date": 1516259116424,
     * "supplier_id": 1,
     * "retail_price": 10,
     * "commodity_inventory": 100,
     * "commodity_sales": 2,
     * "vitual_sales": 1,
     * "commodity_state": 2,
     * "shelves_status": 4,
     * "supplier_name": null,
     * "agency_name": null,
     * "first_channel_name": null,
     * "second_channel_name": null,
     * "msg_count": 36
     */

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
    private int commodity_cost;
    private int price_low;
    private int price_high;
    private int code_type;
    private String code;
    private String commodity_icon;
    private String commodity_text;
    private String commodity_desc;
    private int agency_id;
    private String unit_name;
    private long update_date;
    private int supplier_id;
    private int retail_price;//建立零售价
    private int commodity_inventory;
    private int commodity_sales;
    private int vitual_sales;
    private int commodity_state;
    private int shelves_status;
    private String supplier_name;
    private String agency_name;
    private String first_channel_name;  //
    private String second_channel_name; //
    private int msg_count;  //销售量
    private double woodtotalPrice; //单个销售总额度，这个计算的是double数值（自己进行了计算）
    private int totalPrice;
    private int refund_commodity_num;
    private String consignee_name;
    private String consignee_phone;
    private String address;
    private Object pay_time;

    public Object getPay_time() {
        return pay_time;
    }

    public void setPay_time(Object pay_time) {
        this.pay_time = pay_time;
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

    public int getRefund_commodity_num() {
        return refund_commodity_num;
    }

    public void setRefund_commodity_num(int refund_commodity_num) {
        this.refund_commodity_num = refund_commodity_num;
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

    public int getCommodity_cost() {
        return commodity_cost;
    }

    public void setCommodity_cost(int commodity_cost) {
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

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
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

    public int getCommodity_sales() {
        return commodity_sales;
    }

    public void setCommodity_sales(int commodity_sales) {
        this.commodity_sales = commodity_sales;
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

    public int getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(int msg_count) {
        this.msg_count = msg_count;
    }

    public double getWoodtotalPrice() {
        return woodtotalPrice;
    }

    public void setWoodtotalPrice(double woodtotalPrice) {
        this.woodtotalPrice = woodtotalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

//    private int supplierId;// 供应商ID
//    private String unitName;// 单位名称
//    private String commodityICon;// 商品图片
//    private List<LadderBean> ladderBean;
//    private double unitPrice;// 商品单价
//    private String commodityName;// 商品名称
//    private double woodtotalPrice;// 总价
//    private int purchaseQuantity;// 购物数量
//
//    /*
//     * 与售后有关
//     */
//    private int firstChannelId; // integer,一级频道"
//    private int secodeChannelId; // integer,二级频道"
//
//    public String getCode() {
//        if (code == null) {
//            return "";
//        }
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getSupplierId() {
//        return supplierId;
//    }
//
//    public void setSupplierId(int supplierId) {
//        this.supplierId = supplierId;
//    }
//
//    public String getUnitName() {
//        return unitName;
//    }
//
//    public void setUnitName(String unitName) {
//        this.unitName = unitName;
//    }
//
//    public String getCommodityICon() {
//        return commodityICon;
//    }
//
//    public void setCommodityICon(String commodityICon) {
//        this.commodityICon = commodityICon;
//    }
//
//    public List<LadderBean> getLadderBean() {
//        return ladderBean;
//    }
//
//    public void setLadderBean(List<LadderBean> ladderBean) {
//        this.ladderBean = ladderBean;
//    }
//
//    public double getUnitPrice() {
//        return unitPrice;
//    }
//
//    public void setUnitPrice(double unitPrice) {
//        this.unitPrice = unitPrice;
//    }
//
//    public String getCommodityName() {
//        return commodityName;
//    }
//
//    public void setCommodityName(String commodityName) {
//        this.commodityName = commodityName;
//    }
//
//    public double getWoodtotalPrice() {
//        return woodtotalPrice;
//    }
//
//    public void setWoodtotalPrice(double woodtotalPrice) {
//        this.woodtotalPrice = woodtotalPrice;
//    }
//
//    public int getPurchaseQuantity() {
//        return purchaseQuantity;
//    }
//
//    public void setPurchaseQuantity(int purchaseQuantity) {
//        this.purchaseQuantity = purchaseQuantity;
//    }
//
//    public int getFirstChannelId() {
//        return firstChannelId;
//    }
//
//    public void setFirstChannelId(int firstChannelId) {
//        this.firstChannelId = firstChannelId;
//    }
//
//    public int getSecodeChannelId() {
//        return secodeChannelId;
//    }
//
//    public void setSecodeChannelId(int secodeChannelId) {
//        this.secodeChannelId = secodeChannelId;
//    }

}
