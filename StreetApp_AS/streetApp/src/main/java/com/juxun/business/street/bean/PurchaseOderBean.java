package com.juxun.business.street.bean;


import java.io.Serializable;

/**
 * 订单列表对象
 */
public class PurchaseOderBean implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * "id":29,
     * "create_date":1516674637000,
     * "deleted":false,
     * "pay_id":15,
     * "order_num":"1516674637332405685",
     * "provider_agency_id":3,
     * "partner_agency_id":13,
     * "delivery_name":"",
     * "express_num":"",
     * "consignee_name":"呵呵呵",
     * "consignee_phone":"15815579107",
     * "address":"36号",
     * "commodity_json":"[{"code":"cc123","first_level_channel_id":1,"totalPrice":40,"second_level_channel_id":3,"commodity_icon":"511dda38-4b17-44e3-86c0-5f5bf2a3a42c","id":1,"price_high":40,"supplier_id":1,"price_low":40,"msg_count":1,"commodity_name":"苹果"}]",
     * "total_price":40,
     * "draw_redpacket_id":0,
     * "redpacket_price":0,
     * "delivery_type":2,
     * "real_delivery_date":null,
     * "complete_date":null,
     * "close_date":null,
     * "order_state":2,
     * "abolish_type":null,
     * "abolish":null,
     * "order_explain":null,
     * "pay_type":4,
     * "pay_num":null
     */

    private int id;
    private boolean deleted;
    private int pay_id; //支付id
    private int provider_agency_id;
    private int partner_agency_id;
    private String delivery_name;   //快递名称
    private String express_num;     //快递单号
    private String consignee_name;
    private String consignee_phone;
    private String address;
    private int total_price;
    private int draw_redpacket_id;
    private int redpacket_price;
    private int delivery_type;
    private Object real_delivery_date;
    private String complete_date;
    private String close_date;
    private int order_state;    //1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货,7退款中8退款失败
    private String abolish_type;
    private String abolish;
    private String order_explain;
    private int pay_type;   //1.微信支付.2.支付宝支付 3.pos 4货到付款   5白条支付 6余额支付
    private String pay_num; //支付订单号
    private Object pay_time;
    private Object pay_end_time;//完成时间
    private int reality_refund_price;
    private String buyer_remark;//

    public String getBuyer_remark() {
        return buyer_remark;
    }

    public void setBuyer_remark(String buyer_remark) {
        this.buyer_remark = buyer_remark;
    }

    public int getReality_refund_price() {
        return reality_refund_price;
    }

    public void setReality_refund_price(int reality_refund_price) {
        this.reality_refund_price = reality_refund_price;
    }

    public Object getPay_end_time() {
        return pay_end_time;
    }

    public void setPay_end_time(Object pay_end_time) {
        this.pay_end_time = pay_end_time;
    }

    public Object getPay_time() {
        return pay_time;
    }

    public void setPay_time(Object pay_time) {
        this.pay_time = pay_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getPay_id() {
        return pay_id;
    }

    public void setPay_id(int pay_id) {
        this.pay_id = pay_id;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public int getProvider_agency_id() {
        return provider_agency_id;
    }

    public void setProvider_agency_id(int provider_agency_id) {
        this.provider_agency_id = provider_agency_id;
    }

    public int getPartner_agency_id() {
        return partner_agency_id;
    }

    public void setPartner_agency_id(int partner_agency_id) {
        this.partner_agency_id = partner_agency_id;
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

    public String getConsignee_name() {
        return consignee_name;
    }

    public void setConsignee_name(String consignee_name) {
        this.consignee_name = consignee_name;
    }

    public String getConsignee_phone() {
        return consignee_phone;
    }

    public void setConsignee_phone(String consignee_phone) {
        this.consignee_phone = consignee_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommodity_json() {
        return commodity_json;
    }

    public void setCommodity_json(String commodity_json) {
        this.commodity_json = commodity_json;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getDraw_redpacket_id() {
        return draw_redpacket_id;
    }

    public void setDraw_redpacket_id(int draw_redpacket_id) {
        this.draw_redpacket_id = draw_redpacket_id;
    }

    public int getRedpacket_price() {
        return redpacket_price;
    }

    public void setRedpacket_price(int redpacket_price) {
        this.redpacket_price = redpacket_price;
    }

    public int getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(int delivery_type) {
        this.delivery_type = delivery_type;
    }

    public Object getReal_delivery_date() {
        return real_delivery_date;
    }

    public void setReal_delivery_date(Object real_delivery_date) {
        this.real_delivery_date = real_delivery_date;
    }

    public String getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(String complete_date) {
        this.complete_date = complete_date;
    }

    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(String close_date) {
        this.close_date = close_date;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public String getAbolish_type() {
        return abolish_type;
    }

    public void setAbolish_type(String abolish_type) {
        this.abolish_type = abolish_type;
    }

    public String getAbolish() {
        return abolish;
    }

    public void setAbolish(String abolish) {
        this.abolish = abolish;
    }

    public String getOrder_explain() {
        return order_explain;
    }

    public void setOrder_explain(String order_explain) {
        this.order_explain = order_explain;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_num() {
        return pay_num;
    }

    public void setPay_num(String pay_num) {
        this.pay_num = pay_num;
    }

    //    private int id;// 订单id
//    /*
//     * 与我的订单有关
//     */
//    private String orderNum;// string,订单号",
//    private String createDate;// string,下单时间",
//    private String commodityJson;// string,商品json字符串"
//    private int orderState;// 订单状态1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货",
//    private String deliveryType;// string,运费类型",
//    private String expressNum = "";// string,快递单号",
//    private double totalPrice;// double,订单金额",
//    private String transactionId = "未付款";// 交易订单号",
//    private String tel;// 电话",
//    private String deliveryName;// string,快递名称",
//    private String paytime = "未付款";// 付款时间",
//    private String completeDate = "未完成";// 完成时间",
//    private String realDeliveryDate = "未发货";// 发货时间",
//    private String address;// 收货人地址
//    private String userName;// ,收货人",
//    private double redpacketPrice;// 红包金额"
//    /*
//     * 与售后有关
//     */
    private String order_num; // String,订单号
    private long create_date;// string,下单时间",
    private String commodity_json;// string,商品json字符串"
    private int customer_status; // 7天内参与售后（1可以，2不可以）
    private int can_refund_num; // 该订单所有商品加起来能够退款的数量
    private String customer_service_order_num; // string,供应链采购售后服务单号
    private int customer_service_status; // string,1.审核中2.审核不通过3退款中4退款成功5退款失败6取消售后
    private String refund_remark;
    private String platform_remark;
    private String supplier_order_num;

    public String getSupplier_order_num() {
        return supplier_order_num;
    }

    public void setSupplier_order_num(String supplier_order_num) {
        this.supplier_order_num = supplier_order_num;
    }

    public long getCreate_date() {
        return create_date;
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

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public int getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(int customer_status) {
        this.customer_status = customer_status;
    }

    public int getCan_refund_num() {
        return can_refund_num;
    }

    public void setCan_refund_num(int can_refund_num) {
        this.can_refund_num = can_refund_num;
    }

    public String getCustomer_service_order_num() {
        return customer_service_order_num;
    }

    public void setCustomer_service_order_num(String customer_service_order_num) {
        this.customer_service_order_num = customer_service_order_num;
    }

    public int getCustomer_service_status() {
        return customer_service_status;
    }

    public void setCustomer_service_status(int customer_service_status) {
        this.customer_service_status = customer_service_status;
    }
}

