/**
 * 
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 
 * 项目名称：Street 类名称：CouponBean 类描述： 优惠券BEAN 创建人：WuJianhua 创建时间：2015年6月2日
 * 上午12:27:04 修改人：WuJianhua 修改时间：2015年6月2日 上午12:27:04 修改备注：
 * 
 * @version
 * 
 */
@SuppressWarnings("serial")
public class ListarchBean implements Serializable {
	private int id;// id 是
	String card_id;// 卡劵id 是
	String card_qry;// 二维码url 是
	int userid;// 商家id 是

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getCard_qry() {
		return card_qry;
	}

	public void setCard_qry(String card_qry) {
		this.card_qry = card_qry;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

}
