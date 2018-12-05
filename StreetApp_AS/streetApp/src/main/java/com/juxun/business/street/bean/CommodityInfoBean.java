package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 商品详情bean
 */
public class CommodityInfoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private long create_date;
    private boolean deleted;
    private int spec_type;
    private String spec_ids;
    private String spec_value_ids;
    private int first_level_channel_id;
    private int second_level_channel_id;
    private int price_low;
    private int price_high;
    private int code_type;
    private String commodity_text;
    private String commodity_desc;
    private int agency_id;
    private long update_date;
    private int commodity_sort;
    private int vitual_sales;
    private int shelves_status; // "string,上架状态 1.未通过审核  2、下架中 3、上架 4、强制下架",
    private double commission;
    private double service_commission;
    private int start_buy;
    private int can_buy;
    private int group_count;
    private String revoke_reason;
    private String expiration_time;
    private int click_count;
    private String top_date;
    private String supplier_id;
    private int shelving_mode;
    private String first_level_channel_name;
    private String second_level_channel_name;

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

    public long getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(long update_date) {
        this.update_date = update_date;
    }

    public int getCommodity_sort() {
        return commodity_sort;
    }

    public void setCommodity_sort(int commodity_sort) {
        this.commodity_sort = commodity_sort;
    }

    public int getVitual_sales() {
        return vitual_sales;
    }

    public void setVitual_sales(int vitual_sales) {
        this.vitual_sales = vitual_sales;
    }

    public int getShelves_status() {
        return shelves_status;
    }

    public void setShelves_status(int shelves_status) {
        this.shelves_status = shelves_status;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getService_commission() {
        return service_commission;
    }

    public void setService_commission(double service_commission) {
        this.service_commission = service_commission;
    }

    public int getStart_buy() {
        return start_buy;
    }

    public void setStart_buy(int start_buy) {
        this.start_buy = start_buy;
    }

    public int getCan_buy() {
        return can_buy;
    }

    public void setCan_buy(int can_buy) {
        this.can_buy = can_buy;
    }

    public int getGroup_count() {
        return group_count;
    }

    public void setGroup_count(int group_count) {
        this.group_count = group_count;
    }

    public String getRevoke_reason() {
        return revoke_reason;
    }

    public void setRevoke_reason(String revoke_reason) {
        this.revoke_reason = revoke_reason;
    }

    public String getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(String expiration_time) {
        this.expiration_time = expiration_time;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }

    public String getTop_date() {
        return top_date;
    }

    public void setTop_date(String top_date) {
        this.top_date = top_date;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getShelving_mode() {
        return shelving_mode;
    }

    public void setShelving_mode(int shelving_mode) {
        this.shelving_mode = shelving_mode;
    }

    public String getFirst_level_channel_name() {
        return first_level_channel_name;
    }

    public void setFirst_level_channel_name(String first_level_channel_name) {
        this.first_level_channel_name = first_level_channel_name;
    }

    public String getSecond_level_channel_name() {
        return second_level_channel_name;
    }

    public void setSecond_level_channel_name(String second_level_channel_name) {
        this.second_level_channel_name = second_level_channel_name;
    }

    private int channel_id;// 频道id
    private String code;// 商品编码
    // private double commission;// 分佣比例
    private int commodity_cost;// 商品成本
    private String commodity_icon;// 商品缩略图
    private int commodity_inventory;// 商品库存
    private String commodity_name;// 商品名称
    private double commodity_price_high;// 最高价 无规格时low = high = 真实价格
    private double commodity_price_low;// 最低价 无规格时low = high = 真实价格
    private int commodity_sales;// 商品销量
    private int commodity_type;// 商品类型（只会有社区商品）
    private String cover;// 商品图片
    // private String decoration;// 商品详情（不用管）
    private boolean expiration;// 是否过期
    private boolean expirated;// 是否已经过期
    private long expirationTime;// 过期时间（旧商品有可能没有返回）
    private int inventory_id;// 库存id
    private boolean isLessInventory;// 是否少库存
    private String spec;// 规格
    private String specIds;// 规格主键集合
    private String specValueIds;// 规格值主键集合
    private String unit_name;// 商品单位
    private int commodity_id;// ID
    private int commodity_price;// 价格
    private String revokeReason;// 撤销原因
    private double betweenDays;
    private int sale_state;//销售状态2.上架3.未上架4.强制下架",
    private int commodity_state;//,商品状态1.审核中 2.审核通过 3.审核失败 4.撤销",

    public int getCommodity_state() {
        return commodity_state;
    }

    public void setCommodity_state(int commodity_state) {
        this.commodity_state = commodity_state;
    }

    public int getSale_state() {
        return sale_state;
    }

    public void setSale_state(int sale_state) {
        this.sale_state = sale_state;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }


    public int getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(int commodity_id) {
        this.commodity_id = commodity_id;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public String getCode() {
        return code;
    }

    public boolean isExpirated() {
        betweenDays = (double) ((getExpirationTime() - getCurrentTime()) / (1000 * 60 * 60 * 24) + 0.5);
        if (getExpirationTime() == 0) {
            return false;
        }
        if (betweenDays < 0) {
            return true;
        }
        return false;
    }

    public void setExpirated(boolean expirated) {
        this.expirated = expirated;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // public double getCommission() {
    // return commission;
    // }
    //
    // public void setCommission(double commission) {
    // this.commission = commission;
    // }


    public String getCommodity_icon() {
        return commodity_icon;
    }

    public int getCommodity_cost() {
        return commodity_cost;
    }

    public void setCommodity_cost(int commodity_cost) {
        this.commodity_cost = commodity_cost;
    }

    public int getCommodity_price() {
        return commodity_price;
    }

    public void setCommodity_price(int commodity_price) {
        this.commodity_price = commodity_price;
    }

    public void setCommodity_icon(String commodity_icon) {
        this.commodity_icon = commodity_icon;
    }

    public int getCommodity_inventory() {
        return commodity_inventory;
    }

    public void setCommodity_inventory(int commodity_inventory) {
        this.commodity_inventory = commodity_inventory;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public double getCommodity_price_high() {
        return commodity_price_high;
    }

    public void setCommodity_price_high(double commodity_price_high) {
        this.commodity_price_high = commodity_price_high;
    }

    public double getCommodity_price_low() {
        return commodity_price_low;
    }

    public void setCommodity_price_low(double commodity_price_low) {
        this.commodity_price_low = commodity_price_low;
    }

    public int getCommodity_sales() {
        return commodity_sales;
    }

    public void setCommodity_sales(int commodity_sales) {
        this.commodity_sales = commodity_sales;
    }

    public int getCommodity_type() {
        return commodity_type;
    }

    public void setCommodity_type(int commodity_type) {
        this.commodity_type = commodity_type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    // public String getDecoration() {
    // return decoration;
    // }
    //
    // public void setDecoration(String decoration) {
    // this.decoration = decoration;
    // }

    public long getCurrentTime() {
        long time = System.currentTimeMillis();
        return time;
    }


    public boolean isExpiration() {
        betweenDays = (double) ((getExpirationTime() - getCurrentTime()) / (1000 * 60 * 60 * 24) + 0.5);
        if (betweenDays <= 30 && betweenDays > 0) {
            return true;
        } else if (getExpirationTime() == 0) {
            return false;
        }
        return false;
    }

    public void setExpiration(boolean expiration) {
        this.expiration = expiration;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public boolean isLessInventory() {
        if (commodity_inventory <= 10) {
            return true;
        } else {
            return false;
        }

    }

    public void setLessInventory(boolean isLessInventory) {
        this.isLessInventory = isLessInventory;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSpecIds() {
        return specIds;
    }

    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    public String getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

}
