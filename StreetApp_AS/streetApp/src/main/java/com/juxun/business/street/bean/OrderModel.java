package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    /**
     * "community_id":134, "id":704, "pay_id":820,
     * "order_id":"1502257961780610", "member_id":34, "consigneeName":"陈浚",
     * "consigneePhone":"13242056961",
     * "address":"新龙门酒店公寓(高新园店)广东省深圳市南山区大冲新城花园2栋2D-906", "total_price":15.01,
     * "spec": "delivery_name":null, "express_num":null, "delivery_price":0.01,
     * "redPacket":14.98, "goldPrice":0.01, "delivery_type":0,
     * "real_delivery_time":null, "finish_time":"2017-08-09 13:52:51.0",
     * "pay_time":"2017-08-09 13:52:41", "confirm_date":null,
     * "pay_end_time":"2017-08-09 13:52:51.0", "pay_type":1,
     * "transaction_id":"4008312001201708095311360812", "order_state":2,
     * "order_type":1, "create_date":"2017-08-09 13:52:42.0", "remark":"",
     * "buyer_remark":null, "nonce_str":"10Pk55n7LXZbSqT7kr79cepJpKtq58s",
     * "sub_order_spec":null
     */
    private static final long serialVersionUID = 1L;
    // 订单列表
    private int id;// 订单Id
    private int pay_id;// 支付信息Id
    private int community_id;// 社区Id（为0时为直营）
    private int member_id;// 用户id
    private String community_name;// 商店名称
    private String community_phone;// 商店手机
    private String order_id;// 订单号（前端显示）
    private String consignee_name;// 客户名称
    private String consignee_phone;// 客户电话
    private String consignee_address;// 客户地址
    private String delivery_name;// 快递名称
    private int delivery_price;// 运费
    private String express_num;// 快递单号
    private int delivery_type;// 快递类型【1.快递发 货,2.到店自取】
    private Object real_delivery_time;// 实际快递时间
    private Object finish_time;// 完成时间
    private long create_date;// 创建时间
    private int total_price;// 订单总价
    private int order_type;// 订单类型1.普通商品 2.爆品
    private int order_state;// 订单状态【1.待付款,2.待发货,3.已发 货,4.交易完成,5.已取消,6.已退货】
    private String pay_time;// 支付时间
    private String pay_end_time;// 支付结束时间
    private int pay_type;// 支付类型 1微信支付2支付宝支付
    private String transaction_id;// 支付凭证
    private String nonce_str;// 随机字符串
    private String spec;// 商品信息
    private String sub_order_spec;
    private String buyer_remark;
    private Object confirm_date;// 确认完成时间
    private int redPacket;// 红包金额
    private int goldPrice;// 抵扣金币
    private String remark;// 备注
    private String reject_document_remark;// 拒绝理由
    private String order_num;
    private long pay_finish_time;//支付完成时间
    private long close_time;//取消订单时间
    private long confirm_finish_time;//确认完成时间
    private int gold_count;//使用金币数 个",
    private int gold_price;//string,金币抵扣金额 单位：分",
    private int redpacket_price;//红包 金额
    private int agency_id;

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getRedpacket_price() {
        return redpacket_price;
    }

    public void setRedpacket_price(int redpacket_price) {
        this.redpacket_price = redpacket_price;
    }

    public String getOrder_num() {
        return order_num;
    }

    public int getGold_count() {
        return gold_count;
    }

    public void setGold_count(int gold_count) {
        this.gold_count = gold_count;
    }

    public int getGold_price() {
        return gold_price;
    }

    public void setGold_price(int gold_price) {
        this.gold_price = gold_price;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getRemark() {
        if (remark == null) {
            remark = "";
        }
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(int redPacket) {
        this.redPacket = redPacket;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public Object getConfirm_date() {
        return confirm_date;
    }

    public void setConfirm_date(Object confirm_date) {
        this.confirm_date = confirm_date;
    }

    public String getPay_time() {
        if (pay_time == null) {
            pay_time = "";
        }
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getPay_end_time() {
        if (pay_end_time == null) {
            pay_end_time = "";
        }
        return pay_end_time;
    }

    public void setPay_end_time(String pay_end_time) {
        this.pay_end_time = pay_end_time;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPay_id() {
        return pay_id;
    }

    public void setPay_id(int pay_id) {
        this.pay_id = pay_id;
    }

    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getCommunity_phone() {
        return community_phone;
    }

    public void setCommunity_phone(String community_phone) {
        this.community_phone = community_phone;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getConsignee_address() {
        return consignee_address;
    }

    public void setConsignee_address(String consignee_address) {
        this.consignee_address = consignee_address;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public int getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(int delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getExpress_num() {
        return express_num;
    }

    public void setExpress_num(String express_num) {
        this.express_num = express_num;
    }

    public int getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(int delivery_type) {
        this.delivery_type = delivery_type;
    }

    public Object getReal_delivery_time() {
        return real_delivery_time;
    }

    public void setReal_delivery_time(Object real_delivery_time) {
        this.real_delivery_time = real_delivery_time;
    }

    public Object getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Object finish_time) {
        this.finish_time = finish_time;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public long getPay_finish_time() {
        return pay_finish_time;
    }

    public void setPay_finish_time(long pay_finish_time) {
        this.pay_finish_time = pay_finish_time;
    }

    public long getClose_time() {
        return close_time;
    }

    public void setClose_time(long close_time) {
        this.close_time = close_time;
    }

    public long getConfirm_finish_time() {
        return confirm_finish_time;
    }

    public void setConfirm_finish_time(long confirm_finish_time) {
        this.confirm_finish_time = confirm_finish_time;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSub_order_spec() {
        return sub_order_spec;
    }

    public void setSub_order_spec(String sub_order_spec) {
        this.sub_order_spec = sub_order_spec;
    }

    public String getBuyer_remark() {
        return buyer_remark;
    }

    public void setBuyer_remark(String buyer_remark) {
        this.buyer_remark = buyer_remark;
    }

    public String getReject_document_remark() {
        return reject_document_remark;
    }

    public void setReject_document_remark(String reject_document_remark) {
        this.reject_document_remark = reject_document_remark;
    }
}
