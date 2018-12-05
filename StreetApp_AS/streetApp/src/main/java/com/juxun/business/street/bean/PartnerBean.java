/**
 *
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 合伙人 店铺详情
 */
public class PartnerBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String auth_token;  //将token放置进来
    private int store_id;   //店铺id
    private String store_name;
    private String store_phone;
    private int delivery_full_free; //满多少免运费 单位分"
    private int delivery_price; //运费  单位分
    private double delivery_distance;//配送距离
    private String business_start_date; //
    private String business_end_date;
    private int is_24hour_business;    //1为24小时营业 0为指定营业时间"
    private int business_status;    //当前营业状态
    private String shop_icon;
    private String shop_address;
    private String shop_notice;
    private String safe_phone;  //密保手机
    private int remaining_balance;  //账户余额
    private String cashier_qrcode;  //自助收银二维码
    private String popularize_qrcode; //推广二维码
    private int pay_password;   //0、没有支付密码  1、包含支付密码"
    private String pass_pay;    //设置的支付密码
    private String admin_accout;//店铺账号

    public boolean isSn() {
        return isSn;
    }

    public void setSn(boolean sn) {
        isSn = sn;
    }

    private boolean isSn;// 设备是否为POS机
    private int approval_status;    //0、未审核 1、审核通过 2、审核失败"
    private int red_count;  //可用红包数量
    private int can_withdraw_price; //可提现的金额

    public String getAdmin_accout() {
        return admin_accout;
    }

    public void setAdmin_accout(String admin_accout) {
        this.admin_accout = admin_accout;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_phone() {
        return store_phone;
    }

    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }

    public int getDelivery_full_free() {
        return delivery_full_free;
    }

    public void setDelivery_full_free(int delivery_full_free) {
        this.delivery_full_free = delivery_full_free;
    }

    public int getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(int delivery_price) {
        this.delivery_price = delivery_price;
    }

    public double getDelivery_distance() {
        return delivery_distance;
    }

    public void setDelivery_distance(double delivery_distance) {
        this.delivery_distance = delivery_distance;
    }

    public String getBusiness_start_date() {
        return business_start_date;
    }

    public void setBusiness_start_date(String business_start_date) {
        this.business_start_date = business_start_date;
    }

    public String getBusiness_end_date() {
        return business_end_date;
    }

    public void setBusiness_end_date(String business_end_date) {
        this.business_end_date = business_end_date;
    }

    public int getIs_24hour_business() {
        return is_24hour_business;
    }

    public void setIs_24hour_business(int is_24hour_business) {
        this.is_24hour_business = is_24hour_business;
    }

    public int getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(int business_status) {
        this.business_status = business_status;
    }

    public String getShop_icon() {
        return shop_icon;
    }

    public void setShop_icon(String shop_icon) {
        this.shop_icon = shop_icon;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_notice() {
        return shop_notice;
    }

    public void setShop_notice(String shop_notice) {
        this.shop_notice = shop_notice;
    }

    public String getSafe_phone() {
        return safe_phone;
    }

    public void setSafe_phone(String safe_phone) {
        this.safe_phone = safe_phone;
    }

    public int getRemaining_balance() {
        return remaining_balance;
    }

    public void setRemaining_balance(int remaining_balance) {
        this.remaining_balance = remaining_balance;
    }

    public String getCashier_qrcode() {
        return cashier_qrcode;
    }

    public void setCashier_qrcode(String cashier_qrcode) {
        this.cashier_qrcode = cashier_qrcode;
    }

    public String getPopularize_qrcode() {
        return popularize_qrcode;
    }

    public void setPopularize_qrcode(String popularize_qrcode) {
        this.popularize_qrcode = popularize_qrcode;
    }

    public int getPay_password() {
        return pay_password;
    }

    public void setPay_password(int pay_password) {
        this.pay_password = pay_password;
    }

    public int getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(int approval_status) {
        this.approval_status = approval_status;
    }

    public int getRed_count() {
        return red_count;
    }

    public void setRed_count(int red_count) {
        this.red_count = red_count;
    }

    public int getCan_withdraw_price() {
        return can_withdraw_price;
    }

    public void setCan_withdraw_price(int can_withdraw_price) {
        this.can_withdraw_price = can_withdraw_price;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    @Override
    public String toString() {
        return "PartnerBean{" +
                "auth_token='" + auth_token + '\'' +
                ", store_id=" + store_id +
                ", store_name='" + store_name + '\'' +
                ", store_phone='" + store_phone + '\'' +
                ", delivery_full_free=" + delivery_full_free +
                ", delivery_price=" + delivery_price +
                ", delivery_distance=" + delivery_distance +
                ", business_start_date='" + business_start_date + '\'' +
                ", business_end_date='" + business_end_date + '\'' +
                ", is_24hour_business=" + is_24hour_business +
                ", business_status=" + business_status +
                ", shop_icon='" + shop_icon + '\'' +
                ", shop_address='" + shop_address + '\'' +
                ", shop_notice='" + shop_notice + '\'' +
                ", safe_phone='" + safe_phone + '\'' +
                ", remaining_balance=" + remaining_balance +
                ", cashier_qrcode='" + cashier_qrcode + '\'' +
                ", popularize_qrcode='" + popularize_qrcode + '\'' +
                ", pay_password=" + pay_password +
                ", approval_status=" + approval_status +
                ", red_count=" + red_count +
                ", can_withdraw_price=" + can_withdraw_price +
                '}';
    }

    public String getPass_pay() {
        return pass_pay;
    }

    public void setPass_pay(String pass_pay) {
        this.pass_pay = pass_pay;
    }
}
