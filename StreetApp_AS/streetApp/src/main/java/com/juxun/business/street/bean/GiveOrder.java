/**
 *
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 项目名称：Street 类名称：GiveOrder 类描述： 订单状态 创建人：luoming
 */
public class GiveOrder implements Serializable {

    /**
     * {"id":18,
     * "create_date":1516864717000,
     * "deleted":false,
     * "order_id":"151686471652270200",
     * "order_price":1,
     * "order_state":3,
     * "order_openid":null,
     * "order_pay_type":1,
     * "order_end_date":null,
     * "order_agency":12,
     * "order_admin":17,
     * "order_payorder_id":null,
     * "order_source":2}
     */

    private int id;
    private long create_date;
    private boolean deleted;
    private String order_id;
    private int order_price;
    private int order_state;    //-1 取消订单 1 付款成功 2 付款失败 3 未支付 4 退款
    private String order_openid;
    private int order_pay_type; //1.支付宝 2.微信 3.盒子支付
    private long order_end_date;
    private int order_agency;
    private int order_admin;
    private String order_payorder_id;
    private int order_source;   //0 未知 1、安卓app 2、ios app 3、微信  4、pos机"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getOrder_price() {
        return order_price;
    }

    public void setOrder_price(int order_price) {
        this.order_price = order_price;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public String getOrder_openid() {
        return order_openid;
    }

    public void setOrder_openid(String order_openid) {
        this.order_openid = order_openid;
    }

    public int getOrder_pay_type() {
        return order_pay_type;
    }

    public void setOrder_pay_type(int order_pay_type) {
        this.order_pay_type = order_pay_type;
    }

    public long getOrder_end_date() {
        return order_end_date;
    }

    public void setOrder_end_date(long order_end_date) {
        this.order_end_date = order_end_date;
    }

    public int getOrder_agency() {
        return order_agency;
    }

    public void setOrder_agency(int order_agency) {
        this.order_agency = order_agency;
    }

    public int getOrder_admin() {
        return order_admin;
    }

    public void setOrder_admin(int order_admin) {
        this.order_admin = order_admin;
    }

    public String getOrder_payorder_id() {
        return order_payorder_id;
    }

    public void setOrder_payorder_id(String order_payorder_id) {
        this.order_payorder_id = order_payorder_id;
    }

    public int getOrder_source() {
        return order_source;
    }

    public void setOrder_source(int order_source) {
        this.order_source = order_source;
    }

}
