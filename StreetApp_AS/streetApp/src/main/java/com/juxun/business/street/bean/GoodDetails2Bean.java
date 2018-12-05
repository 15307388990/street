package com.juxun.business.street.bean;


import java.io.Serializable;
import java.util.List;

public class GoodDetails2Bean implements Serializable {
	/**
	 * 商品详情
	 */
	private static final long serialVersionUID = 1L;
	private int agency_id;// 机构id
	private int channel_id;// 频道id
	private String code="";// 商品编码
	private double commission;// 分佣比例
	private double commodity_cost;// 商品成本
	private long commodity_create_date;// 商品创建时间时间戳
	private String commodity_icon;// 商品缩略图
	private int commodity_id;// 商品id
	private int commodity_inventory;// 商品库存
	private String commodity_name;// 商品名称
	private double commodity_price;// 商品价格
	private int commodity_sales;// 商品销量
	private int commodity_state;// 商品状态 1.审核中 2.审核通过 3.审核失败 4.撤销
	private int commodity_type;// 商品类型（只会有社区商品）
	private long commodity_update_date;// 商品更新时间时间戳
	private String cover;// 商品图片
	private String decoration;// 商品详情（不用管）
	private long expirationTime;// 过期时间（旧商品有可能没有返回）
	private String spec;// 规格
	private String specIds;// 规格主键集合
	private String specValueIds;// 规格值主键集合
	private String unit_name;// 商品单位
	private String sale_state;// 销售状态：2.销售中 其他值.下架中
	private String channelName;// 频道名称
	private String commodity_info;//商品摘要
	public String getCommodity_info() {
		return commodity_info;
	}
	public void setCommodity_info(String commodity_info) {
		this.commodity_info = commodity_info;
	}
	public int getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(int agency_id) {
		this.agency_id = agency_id;
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
	public void setCode(String code) {
		this.code = code;
	}
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
	}
	public double getCommodity_cost() {
		return commodity_cost;
	}
	public void setCommodity_cost(double commodity_cost) {
		this.commodity_cost = commodity_cost;
	}
	public long getCommodity_create_date() {
		return commodity_create_date;
	}
	public void setCommodity_create_date(long commodity_create_date) {
		this.commodity_create_date = commodity_create_date;
	}
	public String getCommodity_icon() {
		return commodity_icon;
	}
	public void setCommodity_icon(String commodity_icon) {
		this.commodity_icon = commodity_icon;
	}
	public int getCommodity_id() {
		return commodity_id;
	}
	public void setCommodity_id(int commodity_id) {
		this.commodity_id = commodity_id;
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
	public double getCommodity_price() {
		return commodity_price;
	}
	public void setCommodity_price(double commodity_price) {
		this.commodity_price = commodity_price;
	}
	public int getCommodity_sales() {
		return commodity_sales;
	}
	public void setCommodity_sales(int commodity_sales) {
		this.commodity_sales = commodity_sales;
	}
	public int getCommodity_state() {
		return commodity_state;
	}
	public void setCommodity_state(int commodity_state) {
		this.commodity_state = commodity_state;
	}
	public int getCommodity_type() {
		return commodity_type;
	}
	public void setCommodity_type(int commodity_type) {
		this.commodity_type = commodity_type;
	}
	public long getCommodity_update_date() {
		return commodity_update_date;
	}
	public void setCommodity_update_date(long commodity_update_date) {
		this.commodity_update_date = commodity_update_date;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getDecoration() {
		return decoration;
	}
	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
	public long getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
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
	public String getSale_state() {
		return sale_state;
	}
	public void setSale_state(String sale_state) {
		this.sale_state = sale_state;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


}
