package com.juxun.business.street.bean;


import java.io.Serializable;

/**
 * 支付返回
 */
public class ConfirmBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sign;// String,签名,
    private int payId;// integer,支付id,
    private String payOrderId;// String,支付单号,
    private int payPrice;// double,支付金额,
    private String prepay_id;// String,微信预支付,
    private String codeUrl;// String,支付二维码,
    private String partnerId;// String,微信商户号,
    private String appid;// String,微信支付appid,
    private int payType;// integer,支付类型.1微信支付。2支付宝支付3pos4货到付款,
    private String nonce_str;// String,微信随机数,
    private String payTime;// String,支付时间
    private String qr_code;// 二维码地址
    private int trade_status;// 交易状态，当支付类型为5，6时（余额，白条）。交易状态等于0为成功，-1则失败

    public String getSign() {
        return sign;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public int getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(int trade_status) {
        this.trade_status = trade_status;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(int payPrice) {
        this.payPrice = payPrice;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
