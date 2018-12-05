package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 财务结算表
 * 
 * @author hongliu
 * */

public class FinanceSettleBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;// id
	private int settle_stauts = STAUTS_FREEZE;// 结算状态 -1:异常状态 0： 冻结中 1：已解冻
	private int settle_type = TYPE_GENERAL;// 结算类型 1：正常盈利订单 2:特卖分佣订单 3：海淘分佣订单
											// 4:刷卡订单(pos订单)

	private String settle_info = "";// 异常冻结时异常原因
	private String order_id;// 订单id
	private String out_order_id;// 外部订单id
	private Double settle_price;// 需要结算的金额
	private String settle_date;// 参与结算时间
	private Date settle_end_date;// 结束结算时间
	private Integer agency_id;// 外键 机构id
	private Integer pay_type;// 支付方式 //支付类型 1.微信支付.2.支付宝支付 3、盒子支付
	private String order_date;// 订单成交时间
	private String spec;// 结算内容 辅助字段非实体
	public static final int STAUTS_ERROR = -1;// 结算异常
	public static final int STAUTS_FREEZE = 0;// 冻结中
	public static final int STAUTS_THAW = 1;// 已经解冻

	public static final int TYPE_GENERAL = 1;// 普通商品
	public static final int TYPE_DEALS = 2;// 特卖
	public static final int TYPE_SEA_SHOPPING = 3;// 海淘
	public static final int TYPE_CREDIT = 4;// pos刷卡
	
	
	/**
	 * {
  "pay_source": "integer,0 未知 1、安卓app 2、ios app 3、微信  4、pos机",
  "settle_end_date": "string,结算完成时间",
  "settle_stauts": "integer,结算状态 -1:异常状态 0： 冻结中 1：已解冻",
  "out_order_id": "string,外部订单id（盒子、支付宝、微信支付流水号）",
  "appid": "string,支付方式的appid（微信支付，支付宝appid，盒子appid）",
  "settle_date": "string,参与结算时间",
  "agency_name": "string,机构名称",
  "order_id": "string,商城订单号",
  "id": "integer,财务id",
  "agency_id": "integer,结算的机构 id",
  "settle_price": "double,需要结算的的金额",
  "settle_gold_price": "double,金币补贴金额",
  "settle_redenvelopes_price": "double,红包补贴金额",
  "pay_type": "integer,支付方式 //支付类型 1.微信支付 PAY_WEPAY.2.支付宝支付 PAY_ALIPAY 3、盒子支付 PAY_BOX",
  "settle_info": "string,异常冻结时异常原因",
  "order_date": "string,订单成交时间",
  "settle_type": "integer,结算类型 1：正常盈利订单（超市收入） \t * 2:特卖分佣订单  \t * 3：海淘分佣订单  \t * 4:刷卡订单(pos订单)（只有合伙人才有pos订单） \t * 5、直营电商订单（包含海淘，不包含爆款） \t * 6.爆款 \t * 7.快递收款订单 \t * 8.特卖订单"
}
	 * @return
	 */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getSettle_stauts() {
		return settle_stauts;
	}

	public void setSettle_stauts(int settle_stauts) {
		this.settle_stauts = settle_stauts;
	}

	public int getSettle_type() {
		return settle_type;
	}

	public void setSettle_type(int settle_type) {
		this.settle_type = settle_type;
	}

	public String getSettle_info() {
		return settle_info;
	}

	public void setSettle_info(String settle_info) {
		this.settle_info = settle_info;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOut_order_id() {
		return out_order_id;
	}

	public void setOut_order_id(String out_order_id) {
		this.out_order_id = out_order_id;
	}

	public Double getSettle_price() {
		return settle_price;
	}

	public void setSettle_price(Double settle_price) {
		this.settle_price = settle_price;
	}

	public String getSettle_date() {
		return settle_date;
	}

	public void setSettle_date(String settle_date) {
		this.settle_date = settle_date;
	}

	public Date getSettle_end_date() {
		return settle_end_date;
	}

	public void setSettle_end_date(Date settle_end_date) {
		this.settle_end_date = settle_end_date;
	}

	public Integer getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(Integer agency_id) {
		this.agency_id = agency_id;
	}

	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}

	public String getOrder_date() {
		return order_date;
	}

	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

}
