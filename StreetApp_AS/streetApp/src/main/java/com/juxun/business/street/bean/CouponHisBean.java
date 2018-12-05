package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * Created by wood121 on 2018/1/16.
 */

public class CouponHisBean implements Serializable {

    /**
     * {
     * "red_activity_id": "string,红包活动id",
     * "red_denomination_id": "string,红包面值id",
     * "draw_state": "string, 状态  1.未使用 2已使用",
     * "member_id": "string,用户id",
     * "use_time": "string, 使用时间（YYYY-MM-dd HH:mm:ss）",
     * "end_time": "string,过期时间（YYYY-MM-dd HH:mm）",
     * "start_time": "string,开始时间（YYYY-MM-dd HH:mm）",
     * "member_phone": "string,用户电话",
     * "order_num": "string,订单号",
     * "member_name": "string,用户名称"
     * }
     */

    private int red_activity_id;
    private int red_denomination_id;
    private int draw_state;
    private int member_id;
    private Object use_time;
    private String end_time;
    private String start_time;
    private String member_phone;
    private String order_num;
    private String member_name;
    private long create_date;

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public int getRed_activity_id() {
        return red_activity_id;
    }

    public void setRed_activity_id(int red_activity_id) {
        this.red_activity_id = red_activity_id;
    }

    public int getRed_denomination_id() {
        return red_denomination_id;
    }

    public void setRed_denomination_id(int red_denomination_id) {
        this.red_denomination_id = red_denomination_id;
    }

    public int getDraw_state() {
        return draw_state;
    }

    public void setDraw_state(int draw_state) {
        this.draw_state = draw_state;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public Object getUse_time() {
        return use_time;
    }

    public void setUse_time(Object use_time) {
        this.use_time = use_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getMember_phone() {
        return member_phone;
    }

    public void setMember_phone(String member_phone) {
        this.member_phone = member_phone;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }
}
