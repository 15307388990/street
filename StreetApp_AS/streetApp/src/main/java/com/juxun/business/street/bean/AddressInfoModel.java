package com.juxun.business.street.bean;

import java.io.Serializable;

public class AddressInfoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;// 地址对象id",
    private long create_date;
    private boolean deleted;
    private int agency_id;
    private String user_name;
    private String tel;
    private String address;
    private String area_name;
    private int default_address_id; //1默认、0非默认
    private long last_update_date;

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

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public int getDefault_address_id() {
        return default_address_id;
    }

    public void setDefault_address_id(int default_address_id) {
        this.default_address_id = default_address_id;
    }

    public long getLast_update_date() {
        return last_update_date;
    }

    public void setLast_update_date(long last_update_date) {
        this.last_update_date = last_update_date;
    }
}
