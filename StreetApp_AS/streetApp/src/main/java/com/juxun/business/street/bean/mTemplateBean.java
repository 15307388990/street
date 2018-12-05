package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.List;

public class mTemplateBean implements Serializable {
	/**
	 * 获取商品模板详情
	 */
	private static final long serialVersionUID = 1L;
	private int channel_id;// 频道id
	private String code;// 商品编码
	private String cover;// 商品展示图
	private Long create_date;// 创建时间时间戳
	private String icon;// 商品缩略图
	private int is_deleted;// 是否逻辑删除 0存在 1逻辑删除
	private String spec;// 规格
	private String specIds;// 规格主键集合
	private String specValueIds;// 规格值主键集合
	private double template_cost;// 模板商品成本价
	private String template_description;// 模板商品详情
	private String template_info;// 模板商品简介 没有返回说明该字段为空
	private int template_id;// 模板商品id
	private String template_name;// 模板商品名称
	private double template_price;// 模板商品出售价
	private String unit_name;// 单位名称
	private long update_date;// 更新时间时间戳
	private String channelName;// 所述频道名称
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
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public Long getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Long create_date) {
		this.create_date = create_date;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getIs_deleted() {
		return is_deleted;
	}
	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
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
	public double getTemplate_cost() {
		return template_cost;
	}
	public void setTemplate_cost(double template_cost) {
		this.template_cost = template_cost;
	}
	public String getTemplate_description() {
		return template_description;
	}
	public void setTemplate_description(String template_description) {
		this.template_description = template_description;
	}
	public String getTemplate_info() {
		return template_info;
	}
	public void setTemplate_info(String template_info) {
		this.template_info = template_info;
	}
	public int getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(int template_id) {
		this.template_id = template_id;
	}
	public String getTemplate_name() {
		return template_name;
	}
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}
	public double getTemplate_price() {
		return template_price;
	}
	public void setTemplate_price(double template_price) {
		this.template_price = template_price;
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
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

}
