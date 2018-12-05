/**
 * 
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**   
 *    
 * 项目名称：Street   
 * 类名称：CouponBean   
 * 类描述：   优惠券BEAN
 * 创建人：WuJianhua   
 * 创建时间：2015年6月2日 上午12:27:04   
 * 修改人：WuJianhua   
 * 修改时间：2015年6月2日 上午12:27:04   
 * 修改备注：   
 * @version    
 *    
 */
@SuppressWarnings("serial")
public class CouponBean implements Serializable{
	private int coupon_state;	//优惠券状态 0：上线, 1：下线
	private String coupon_name;//优惠券名称
	private int coupon_num;//优惠券数目
	private double coupon_price;//优惠券价格
	private String coupon_icon;	//优惠券图片
	private String coupon_info;//优惠券详情
	private String coupon_use_start_date;//优惠券开始时间
	private String coupon_use_end_date;//优惠券结束时间
	private int coupon_max;//优惠券最低消费
	private int coupon_id;//优惠券ID
	private String qrcode_url;//优惠券二维码路径
	public CouponBean() {
		super();
	}
	public CouponBean(int coupon_state, String coupon_name, int coupon_num,
			double coupon_price, String coupon_icon, String coupon_info,
			String coupon_use_start_date, String coupon_use_end_date,
			int coupon_max, int coupon_id, String qrcode_url) {
		super();
		this.coupon_state = coupon_state;
		this.coupon_name = coupon_name;
		this.coupon_num = coupon_num;
		this.coupon_price = coupon_price;
		this.coupon_icon = coupon_icon;
		this.coupon_info = coupon_info;
		this.coupon_use_start_date = coupon_use_start_date;
		this.coupon_use_end_date = coupon_use_end_date;
		this.coupon_max = coupon_max;
		this.coupon_id = coupon_id;
		this.qrcode_url = qrcode_url;
	}
	public int getCoupon_state() {
		return coupon_state;
	}
	public void setCoupon_state(int coupon_state) {
		this.coupon_state = coupon_state;
	}
	public String getCoupon_name() {
		return coupon_name;
	}
	public void setCoupon_name(String coupon_name) {
		this.coupon_name = coupon_name;
	}
	public int getCoupon_num() {
		return coupon_num;
	}
	public void setCoupon_num(int coupon_num) {
		this.coupon_num = coupon_num;
	}
	public double getCoupon_price() {
		return coupon_price;
	}
	public void setCoupon_price(double coupon_price) {
		this.coupon_price = coupon_price;
	}
	public String getCoupon_icon() {
		return coupon_icon;
	}
	public void setCoupon_icon(String coupon_icon) {
		this.coupon_icon = coupon_icon;
	}
	public String getCoupon_info() {
		return coupon_info;
	}
	public void setCoupon_info(String coupon_info) {
		this.coupon_info = coupon_info;
	}
	public String getCoupon_use_start_date() {
		return coupon_use_start_date;
	}
	public void setCoupon_use_start_date(String coupon_use_start_date) {
		this.coupon_use_start_date = coupon_use_start_date;
	}
	public String getCoupon_use_end_date() {
		return coupon_use_end_date;
	}
	public void setCoupon_use_end_date(String coupon_use_end_date) {
		this.coupon_use_end_date = coupon_use_end_date;
	}
	public int getCoupon_max() {
		return coupon_max;
	}
	public void setCoupon_max(int coupon_max) {
		this.coupon_max = coupon_max;
	}
	public int getCoupon_id() {
		return coupon_id;
	}
	public void setCoupon_id(int coupon_id) {
		this.coupon_id = coupon_id;
	}
	public String getQrcode_url() {
		return qrcode_url;
	}
	public void setQrcode_url(String qrcode_url) {
		this.qrcode_url = qrcode_url;
	}
	
}
