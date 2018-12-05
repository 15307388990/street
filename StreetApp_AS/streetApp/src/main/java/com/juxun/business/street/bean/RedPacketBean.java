package com.juxun.business.street.bean;


import java.io.Serializable;

public class RedPacketBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 红包
     * "order_id": "string,红包使用后对应的订单号",
     * "agency_id": "string, 合伙人机构id",
     * "redpacket_id": "string,红包面值id",
     * "activity_id": "string,活动ID(充值获取时，保存充值活动id)",
     * "redpacket_price": "string, 面值",
     * "full_price": "string,满多少使用",
     * "state": "string,是否使用。2代表使用。1代表没有使用",
     * "draw_souce": "string,获取来源。1平台发送2充值获取",
     * "start_date": "string,使用开始时间",
     * "end_date": "string,过期时间",
     * "use_date": "string, 使用时间",
     * "draw_state": "string,\t * 是否被确认领取 \t * 1、未确认 \t * 2、已确认",
     * "use_channels": "string,使用频道范围(id),以逗号隔开",
     * "use_channel_names": "string,使用频道范围(名字),以逗号隔开"
     *
     * private int iske = 0;// 该红包是否可用 1为可用 0不可用    
     private int isxuan;// 该红包是否选中 1为选中 0未选中    state
     */

    private String order_id;
    private int agency_id;
    private int redpacket_id;
    private int activity_id;
    private int redpacket_price;
    private int full_price;
    private int state;
    private int draw_souce;
    private String start_date;
    private String end_date;
    private long use_date;
    private int draw_state;
    private String use_channels;
    private String use_channel_names;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getRedpacket_id() {
        return redpacket_id;
    }

    public void setRedpacket_id(int redpacket_id) {
        this.redpacket_id = redpacket_id;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getRedpacket_price() {
        return redpacket_price;
    }

    public void setRedpacket_price(int redpacket_price) {
        this.redpacket_price = redpacket_price;
    }

    public int getFull_price() {
        return full_price;
    }

    public void setFull_price(int full_price) {
        this.full_price = full_price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDraw_souce() {
        return draw_souce;
    }

    public void setDraw_souce(int draw_souce) {
        this.draw_souce = draw_souce;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public long getUse_date() {
        return use_date;
    }

    public void setUse_date(long use_date) {
        this.use_date = use_date;
    }

    public int getDraw_state() {
        return draw_state;
    }

    public void setDraw_state(int draw_state) {
        this.draw_state = draw_state;
    }

    public String getUse_channels() {
        return use_channels;
    }

    public void setUse_channels(String use_channels) {
        this.use_channels = use_channels;
    }

    public String getUse_channel_names() {
        return use_channel_names;
    }

    public void setUse_channel_names(String use_channel_names) {
        this.use_channel_names = use_channel_names;
    }
}
