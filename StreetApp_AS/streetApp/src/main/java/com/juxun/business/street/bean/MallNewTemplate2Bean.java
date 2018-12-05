package com.juxun.business.street.bean;


import java.io.Serializable;
import java.util.List;

public class MallNewTemplate2Bean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 获取商品模板(2.3新)
	 */
	private int id;
	private String commodity_name;// 商品名字
	private int first_level_channel_id;// 一级频道id",
	private int second_level_channel_id;// 二级频道id",
	private int commodity_cost;// 商品成本价",
	private int price_low;// 最低销售价",
	private int price_high;// 最高销售价",
	private String code;// 条形码",
	private String commodity_icon;// 商品图片逗号隔开",
	private String commodity_text;// 富文本",
	private String commodity_desc;// 描述",
	private String unit_name;// 单位名称"
	private String commodity_inventory;//商品库存

	public String getCommodity_inventory() {
		return commodity_inventory;
	}

	public void setCommodity_inventory(String commodity_inventory) {
		this.commodity_inventory = commodity_inventory;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommodity_name() {
		return commodity_name;
	}

	public void setCommodity_name(String commodity_name) {
		this.commodity_name = commodity_name;
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

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

}
