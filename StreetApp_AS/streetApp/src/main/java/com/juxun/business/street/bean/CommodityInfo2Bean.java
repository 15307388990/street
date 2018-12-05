package com.juxun.business.street.bean;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CommodityInfo2Bean implements Serializable {
	/**
	 * 商品详情
	 */
	private static final long serialVersionUID = 1L;
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
	private int sale_state;//销售状态2.上架3.未上架4.强制下架",
	private double betweenDays;
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
		 betweenDays = (double)((getExpirationTime() - getCurrentTime()) / (1000 * 60 * 60 *24) + 0.5);
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
	
	public long getCurrentTime(){
		long time = System.currentTimeMillis();
		return time;
	}

	
	
	public boolean isExpiration() {
		 betweenDays = (double)((getExpirationTime() - getCurrentTime()) / (1000 * 60 * 60 *24) + 0.5); 
		if (betweenDays <= 30 && betweenDays > 0) {
			return true;
		}else if (getExpirationTime() == 0){
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
