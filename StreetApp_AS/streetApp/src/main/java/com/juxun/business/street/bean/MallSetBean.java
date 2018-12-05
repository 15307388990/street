package com.juxun.business.street.bean;

import java.io.Serializable;

public class MallSetBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 重新提交审核资料获取
	 */
	private String shop_name;// 店铺名称
	private String shop_area_name;// 区域名称
	private String shop_address;// 详情地址
	private String id_number;// 身份证
	private String id_number_positiveandnegative_icon;// 身份证正反面
	private String hand_idnumber_icon;// 手拿身份证
	private String business_licence_icon;// 营业执照
	private String lat;// 纬度
	private String lng;// 经度
	private String shop_icon;// 门头照
	private String partner_name;//联系人

	public String getPartner_name() {
		return partner_name;
	}

	public void setPartner_name(String partner_name) {
		this.partner_name = partner_name;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_area_name() {
		return shop_area_name;
	}

	public void setShop_area_name(String shop_area_name) {
		this.shop_area_name = shop_area_name;
	}

	public String getShop_address() {
		return shop_address;
	}

	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getId_number_positiveandnegative_icon() {
		return id_number_positiveandnegative_icon;
	}

	public void setId_number_positiveandnegative_icon(String id_number_positiveandnegative_icon) {
		this.id_number_positiveandnegative_icon = id_number_positiveandnegative_icon;
	}

	public String getHand_idnumber_icon() {
		return hand_idnumber_icon;
	}

	public void setHand_idnumber_icon(String hand_idnumber_icon) {
		this.hand_idnumber_icon = hand_idnumber_icon;
	}

	public String getBusiness_licence_icon() {
		return business_licence_icon;
	}

	public void setBusiness_licence_icon(String business_licence_icon) {
		this.business_licence_icon = business_licence_icon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getShop_icon() {
		return shop_icon;
	}

	public void setShop_icon(String shop_icon) {
		this.shop_icon = shop_icon;
	}

}
