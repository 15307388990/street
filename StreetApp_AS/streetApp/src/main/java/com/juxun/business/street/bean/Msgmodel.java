package com.juxun.business.street.bean;

import java.io.Serializable;

public class Msgmodel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 购物车
     */
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 订单id
     */
    private Integer order_id;
    /**
     * 合伙人分佣比例
     */
    private Double commission;//合伙人分佣比例

    /**
     * 成本价
     */
    private Double commodityCost;//成本价
    /**
     * 图片
     */
    private String commodityICon;//图片
    /**
     * 购买数量
     */
    private Integer goodsCount;//购买数量
    /**
     * 商品id
     */
    private Integer msg_id;//商品id
    /**
     * 商品名称
     */
    private String commodityName;//商品
    /**
     * 价格(单价)
     */
    private int price;//价格
    /**
     * 服务商分佣比例
     */
    private Double serviceCommission;//服务商分佣比例
    /**
     * 规格id集合
     */
    private String specIds;//规格id集合
    /**
     * 规格名称集合
     */
    private String specNames;//规格名称集合

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommodityCost() {
        return commodityCost;
    }

    public void setCommodityCost(Integer commodityCost) {
        this.commodityCost = commodityCost / 100.0;
    }

    public String getCommodityICon() {
        return commodityICon;
    }

    public void setCommodityICon(String commodityICon) {
        String[] cover = commodityICon.split(",");

        this.commodityICon = cover[0];
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Integer getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(Integer msg_id) {
        this.msg_id = msg_id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Double getServiceCommission() {
        return serviceCommission;
    }

    public void setServiceCommission(Double serviceCommission) {
        this.serviceCommission = serviceCommission;
    }

    public String getSpecIds() {
        return specIds;
    }

    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    public String getSpecNames() {
        return specNames;
    }

    public void setSpecNames(String specNames) {
        this.specNames = specNames;
    }
}
