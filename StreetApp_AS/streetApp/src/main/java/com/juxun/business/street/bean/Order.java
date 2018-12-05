package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * Created by George on 16/2/26.
 */
public class Order implements Serializable {
    public static final long serialVersionUID = 42L;

    private String mGoodsName;
    private String mAmount;
    private String mDateTime;
    private String mTradeStatus;
    private String mOutTradeNo;

    private String mCbTradeNo;

    private String mPayType;

    public Order() {
    }

    public String getmPayType() {
        return mPayType;
    }

    public void setmPayType(String mPayType) {
        this.mPayType = mPayType;
    }

    public String getmOutTradeNo() {
        return mOutTradeNo;
    }

    public void setmOutTradeNo(String mOutTradeNo) {
        this.mOutTradeNo = mOutTradeNo;
    }

    public String getmCbTradeNo() {
        return mCbTradeNo;
    }

    public void setmCbTradeNo(String mCbTradeNo) {
        this.mCbTradeNo = mCbTradeNo;
    }

    public String getmGoodsName() {
        return mGoodsName;
    }

    public void setmGoodsName(String mGoodsName) {
        this.mGoodsName = mGoodsName;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getmTradeStatus() {
        return mTradeStatus;
    }

    public void setmTradeStatus(String mTradeStatus) {
        this.mTradeStatus = mTradeStatus;
    }

    @Override public String toString() {
        return "{" +
            "\"mGoodsName\":\"" + mGoodsName + '\"' +
            ", \"mAmount\":\"" + mAmount + '\"' +
            ", \"mDateTime\":\"" + mDateTime + '\"' +
            ", \"mTradeStatus\":\"" + mTradeStatus + '\"' +
            ", \"mOutTradeNo\":\"" + mOutTradeNo + '\"' +
            ", \"mCbTradeNo\":\"" + mCbTradeNo + '\"' +
            ", \"mPayType\":\"" + mPayType + '\"' +
            '}';
    }
}
