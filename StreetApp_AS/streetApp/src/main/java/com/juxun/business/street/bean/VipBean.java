/**
 * 
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**   
 *    
 * 项目名称：Street   
 * 类名称：VipBean   
 * 类描述：  会员卡
 * 创建人：WuJianhua   
 * 创建时间：2015年5月28日 上午10:08:21   
 * 修改人：WuJianhua   
 * 修改时间：2015年5月28日 上午10:08:21   
 * 修改备注：   
 * @version    
 *    
 */
@SuppressWarnings("serial")
public class VipBean implements Serializable {
	/** 权益id */
	private int interests_id;
	/** 会员卡类型1.银卡，2.金卡。3.钻石卡 */
	private int interests_type;
	/** 权益名称 */
	private String interests_name;
	/** 权益介绍 */
	private String interests_introduction;
	/** 会员卡折扣 */
	private double interests_discount;
	/** 生成二维码的url */
	private String qrcode_url;
	
	public VipBean(int interests_id, int interests_type,
			String interests_name, String interests_introduction,
			double interests_discount, String qrcode_url) {
		super();
		this.interests_id = interests_id;
		this.interests_type = interests_type;
		this.interests_name = interests_name;
		this.interests_introduction = interests_introduction;
		this.interests_discount = interests_discount;
		this.qrcode_url = qrcode_url;
	}

	public VipBean() {
		super();
	}

	public int getInterests_type() {
		return interests_type;
	}

	public void setInterests_type(int interests_type) {
		this.interests_type = interests_type;
	}

	public double getInterests_discount() {
		return interests_discount;
	}

	public void setInterests_discount(double interests_discount) {
		this.interests_discount = interests_discount;
	}

	public String getQrcode_url() {
		return qrcode_url;
	}

	public void setQrcode_url(String qrcode_url) {
		this.qrcode_url = qrcode_url;
	}

	public int getInterests_id() {
		return interests_id;
	}

	public void setInterests_id(int interests_id) {
		this.interests_id = interests_id;
	}

	public String getInterests_name() {
		return interests_name;
	}

	public void setInterests_name(String interests_name) {
		this.interests_name = interests_name;
	}

	public String getInterests_introduction() {
		return interests_introduction;
	}

	public void setInterests_introduction(String interests_introduction) {
		this.interests_introduction = interests_introduction;
	}
	
}
