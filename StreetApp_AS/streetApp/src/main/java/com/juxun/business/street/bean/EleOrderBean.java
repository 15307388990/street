package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Lifucheng
 * 电商订单bean
 *
 */
public class EleOrderBean implements Serializable{
	
	private int id;//订单id
	private String order_id;//交易号
	private int status;//状态订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货,7.临时单
	private String total_price;//订单总额 
	private String orderCreateTime;////下单时间
	private String order_end_date;//支付完成时间
	private String delivery_name;//快递公司名称
	private String express_num;//快递单号
	private List<EleGoodListBean>  goodslist;
	public EleOrderBean() {
		super();
		
	}
	public EleOrderBean(int id, String order_id, int status, String total_price,
			String orderCreateTime, String order_end_date,
			String delivery_name, String express_num,
			List<EleGoodListBean> goodslist) {
		super();
		this.id = id;
		this.order_id = order_id;
		this.status = status;
		this.total_price = total_price;
		this.orderCreateTime = orderCreateTime;
		this.order_end_date = order_end_date;
		this.delivery_name = delivery_name;
		this.express_num = express_num;
		this.goodslist = goodslist;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getOrder_end_date() {
		return order_end_date;
	}
	public void setOrder_end_date(String order_end_date) {
		this.order_end_date = order_end_date;
	}
	public String getDelivery_name() {
		return delivery_name;
	}
	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}
	public String getExpress_num() {
		return express_num;
	}
	public void setExpress_num(String express_num) {
		this.express_num = express_num;
	}
	public List<EleGoodListBean> getGoodslist() {
		return goodslist;
	}
	public void setGoodslist(List<EleGoodListBean> goodslist) {
		this.goodslist = goodslist;
	}
	
	

}
