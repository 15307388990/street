package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 支付bean
 * 
 * @author jwen
 *
 */
public class PayBean implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String tradeNo;// 内部产生的订单号
	protected String total_fee;// 交易金额
	protected String notify_url;// 回调通知地址
	protected String orderTime;// 订单时间，格式根据相应支付的需要生成
	protected String sign;// 签名
	private String transactionId;//

	public String getTradeNo() {
		return tradeNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
