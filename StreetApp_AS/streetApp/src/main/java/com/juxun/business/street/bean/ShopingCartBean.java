package com.juxun.business.street.bean;


import java.io.Serializable;


public class ShopingCartBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * commission : 0.0
     * commodityCost : 1
     * commodityICon : 4058320170706
     * commodityName : 红烧茄子肉沫
     * first_channelId : 339
     * goodsCount : 1
     * id : 39846
     * price : 1
     * secode_channelId : 0
     * serviceCommission : 0.0
     * specIds :
     * specNames :
     * total_price : 0.01
     */

    private double commission;
    private int commodityCost;
    private String commodityICon;
    private String commodityName;
    private int first_channelId;
    private int goodsCount;
    private int id;
    private int price;
    private int secode_channelId;
    private double serviceCommission;
    private String specIds;
    private String specNames;
    private int total_price;

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public int getCommodityCost() {
        return commodityCost;
    }

    public void setCommodityCost(int commodityCost) {
        this.commodityCost = commodityCost;
    }

    public String getCommodityICon() {
        return commodityICon;
    }

    public void setCommodityICon(String commodityICon) {
        this.commodityICon = commodityICon;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getFirst_channelId() {
        return first_channelId;
    }

    public void setFirst_channelId(int first_channelId) {
        this.first_channelId = first_channelId;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSecode_channelId() {
        return secode_channelId;
    }

    public void setSecode_channelId(int secode_channelId) {
        this.secode_channelId = secode_channelId;
    }

    public double getServiceCommission() {
        return serviceCommission;
    }

    public void setServiceCommission(double serviceCommission) {
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

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
