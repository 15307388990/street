package com.juxun.business.street.fragment;

import java.io.Serializable;

public class ApplyRecordInfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 售后申请记录详情
	 */
	private int customer_service_status;//1.审核中2.审核不通过3退款中4退款成功5退款失败6取消售后
	private String commodity_json;//退款json
	private int refund_price;//退款金额
	private int pay_type;//支付方式
	private String create_date;//下单时间
	private String customer_service_order_num;//服务单号
	private String supplier_order_num;//采购订单号
	private String refund_remark;//退款原因
	private String platform_remark;//售后留言
	public int getCustomer_service_status() {
		return customer_service_status;
	}
	public void setCustomer_service_status(int customer_service_status) {
		this.customer_service_status = customer_service_status;
	}
	public String getCommodity_json() {
		return commodity_json;
	}
	public void setCommodity_json(String commodity_json) {
		this.commodity_json = commodity_json;
	}
	public int getRefund_price() {
		return refund_price;
	}
	public void setRefund_price(int refund_price) {
		this.refund_price = refund_price;
	}
	public int getPay_type() {
		return pay_type;
	}
	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getCustomer_service_order_num() {
		return customer_service_order_num;
	}
	public void setCustomer_service_order_num(String customer_service_order_num) {
		this.customer_service_order_num = customer_service_order_num;
	}
	public String getSupplier_order_num() {
		return supplier_order_num;
	}
	public void setSupplier_order_num(String supplier_order_num) {
		this.supplier_order_num = supplier_order_num;
	}
	public String getRefund_remark() {
		return refund_remark;
	}
	public void setRefund_remark(String refund_remark) {
		this.refund_remark = refund_remark;
	}
	public String getPlatform_remark() {
		return platform_remark;
	}
	public void setPlatform_remark(String platform_remark) {
		this.platform_remark = platform_remark;
	}
}
