package com.juxun.business.street.bean;

/**
 * 综合电商订单表
 */
public class MallOrderBean {
	private Integer id;
	private Integer pay_id;// 支付ID
	private String order_id;// 订单号
	private Integer member_id;// 用户ID
	private String consigneeName;// 收货人姓名
	private String consigneePhone;// 收货人电话
	private String address;// 地址
	private double total_price;// 总价格（包括红包面值）
	private String spec;// 商品信息json
	private String delivery_name;// 快递名称
	private String express_num;// 快递单号
	private double delivery_price;// 运费价格
	private Integer draw_id;// 领取ID
	private Integer delivery_type;// 运费类型
	private String real_delivery_time;// 发货时间
	private String finish_time;// 支付时间
	private Integer community_id;// 社区ID
	private String community_name;// 社区名称
	private String community_phone;// 社区电话
	private String create_date;// 下单时间
	private String close_date;// 取消时间
	private String confirm_date;// 完成时间
	private Integer order_state;// 订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货】
	private Integer order_type = 1;// 1.普通商品 2.爆品.3。海淘商品
	private Integer is_del = 0;// 删除状态 0.存在 1.删除
	private String remark;// 备注
	private String buyer_remark;// 买家备注
	private Integer source_community;// 买家来源
	private double redPacket;// 红包金额
	private double goldPrice;// 抵扣金币
	private String pay_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPay_id() {
		return pay_id;
	}

	public void setPay_id(Integer pay_id) {
		this.pay_id = pay_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public double getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(double goldPrice) {
		this.goldPrice = goldPrice;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public double getDelivery_price() {
		return delivery_price;
	}

	public void setDelivery_price(double delivery_price) {
		this.delivery_price = delivery_price;
	}

	public Integer getDelivery_type() {
		return delivery_type;
	}

	public void setDelivery_type(Integer delivery_type) {
		this.delivery_type = delivery_type;
	}

	public String getReal_delivery_time() {
		if (real_delivery_time == null) {
			real_delivery_time = "";
		}
		return real_delivery_time;
	}

	public void setReal_delivery_time(String real_delivery_time) {
		this.real_delivery_time = real_delivery_time;
	}

	public String getFinish_time() {
		if (finish_time == null) {
			finish_time = "";
		}
		return finish_time;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public void setFinish_time(String finish_time) {
		this.finish_time = finish_time;
		this.pay_time = finish_time;
	}

	public Integer getCommunity_id() {
		return community_id;
	}

	public void setCommunity_id(Integer community_id) {
		this.community_id = community_id;
	}

	public String getCommunity_name() {
		return community_name;
	}

	public void setCommunity_name(String community_name) {
		this.community_name = community_name;
	}

	public String getCreate_date() {
		if (create_date == null) {
			create_date = "";
		}
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public Integer getOrder_state() {
		return order_state;
	}

	public void setOrder_state(Integer order_state) {
		this.order_state = order_state;
	}

	public Integer getOrder_type() {
		return order_type;
	}

	public void setOrder_type(Integer order_type) {
		this.order_type = order_type;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public String getCommunity_phone() {
		return community_phone;
	}

	public void setCommunity_phone(String community_phone) {
		this.community_phone = community_phone;
	}

	public Integer getIs_del() {
		return is_del;
	}

	public void setIs_del(Integer is_del) {
		this.is_del = is_del;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBuyer_remark() {
		return buyer_remark;
	}

	public void setBuyer_remark(String buyer_remark) {
		this.buyer_remark = buyer_remark;
	}

	public Integer getSource_community() {
		return source_community;
	}

	public void setSource_community(Integer source_community) {
		this.source_community = source_community;
	}

	public double getRedPacket() {
		return redPacket;
	}

	public void setRedPacket(double redPacket) {
		this.redPacket = redPacket;
	}

	public Integer getDraw_id() {
		return draw_id;
	}

	public void setDraw_id(Integer draw_id) {
		this.draw_id = draw_id;
	}

	public String getClose_date() {
		return close_date;
	}

	public void setClose_date(String close_date) {
		this.close_date = close_date;
	}

	public String getConfirm_date() {
		if (confirm_date == null) {
			confirm_date = "";
		}
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

}
