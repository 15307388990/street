package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.List;

public class ChannelBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  "id":1,
     "create_date":1513218940000,
     "deleted":false,
     "channel_name":"电器",
     "parent_id":0,
     "channel_info":"",
     "channel_soft":0,
     "channel_icon":null,
     "agency_id":1,
     "channel_type":3,
     "update_date":1513223043000,
     "update_admin_name":"",
     "childChannelList":[
     */
    /**
     * 频道列表
     */
    private int id;
    private long create_date;
    private boolean deleted;
    private String channel_name;// 频道名称
    private int parent_id;
    private String channel_info;// 频道信息
    private int channel_soft;// 排序
    private String channel_icon;// icon
    private int agency_id;
    private int channel_type;
    private long update_date;
    private String update_admin_name;
    //private List<ChannelBean> subList;// 子频道列表【商品管理中的频道】
    private List<ChannelBean> childChannelList;// 子频道列表【供应链中的商品列表】


    private int channel_id;// 频道id
    private String channel_ids;// 包含频道id列

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getChannel_info() {
        return channel_info;
    }

    public void setChannel_info(String channel_info) {
        this.channel_info = channel_info;
    }

    public int getChannel_soft() {
        return channel_soft;
    }

    public void setChannel_soft(int channel_soft) {
        this.channel_soft = channel_soft;
    }

    public String getChannel_icon() {
        return channel_icon;
    }

    public void setChannel_icon(String channel_icon) {
        this.channel_icon = channel_icon;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getChannel_type() {
        return channel_type;
    }

    public void setChannel_type(int channel_type) {
        this.channel_type = channel_type;
    }

    public long getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(long update_date) {
        this.update_date = update_date;
    }

    public String getUpdate_admin_name() {
        return update_admin_name;
    }

    public void setUpdate_admin_name(String update_admin_name) {
        this.update_admin_name = update_admin_name;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_ids() {
        return channel_ids;
    }

    public void setChannel_ids(String channel_ids) {
        this.channel_ids = channel_ids;
    }

//    public List<ChannelBean> getSubList() {
//        return subList;
//    }
//
//    public void setSubList(List<ChannelBean> subList) {
//        this.subList = subList;
//    }

    public List<ChannelBean> getChildChannelList() {
        return childChannelList;
    }

    public void setChildChannelList(List<ChannelBean> childChannelList) {
        this.childChannelList = childChannelList;
    }
}
